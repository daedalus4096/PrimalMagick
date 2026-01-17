package com.verdantartifice.primalmagick.common.capabilities;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.function.Function;

public abstract class AbstractCapability<T extends AbstractCapability<T>> implements INBTSerializablePM<Tag> {
    private static final Logger LOGGER = LogUtils.getLogger();

    private long syncTimestamp;   // Last timestamp at which this capability received a sync from the server

    @SuppressWarnings("unchecked")
    private T self() {
        return (T)this;
    }

    public abstract Codec<T> codec();

    protected long getSyncTimestamp() {
        return this.syncTimestamp;
    }

    protected void setSyncTimestamp(long timestamp) {
        this.syncTimestamp = timestamp;
    }

    public void copyFrom(@Nullable T other) {
        if (other == null || other.getSyncTimestamp() <= this.getSyncTimestamp()) {
            return;
        }
        this.copyFromInner(other);
        this.setSyncTimestamp(other.getSyncTimestamp());
    }

    protected abstract void copyFromInner(@NotNull T other);

    @Override
    public Tag serializeNBT(HolderLookup.Provider registryAccess) {
        RegistryOps<Tag> registryOps = registryAccess.createSerializationContext(NbtOps.INSTANCE);
        this.setSyncTimestamp(System.currentTimeMillis());
        return this.codec().encodeStart(registryOps, this.self())
                .resultOrPartial(LOGGER::error)
                .orElse(null);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registryAccess, Tag nbt) {
        RegistryOps<Tag> registryOps = registryAccess.createSerializationContext(NbtOps.INSTANCE);
        this.codec().parse(registryOps, nbt)
                .resultOrPartial(LOGGER::error)
                .ifPresent(this::copyFrom);
    }

    /**
     * Sync the capability to the given player's client over the network with a supplied packet.
     *
     * @param player the destination player
     * @param packetSource a function that creates the packet instance with which to sync the capability
     */
    protected void sync(@NotNull ServerPlayer player, @NotNull Function<T, ? extends IMessageToClient> packetSource) {
        this.setSyncTimestamp(System.currentTimeMillis());

        // Clone the capability data before passing it to the network
        RegistryOps<Tag> registryOps = player.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        this.codec().encodeStart(registryOps, this.self())
                .resultOrPartial(LOGGER::error)
                .flatMap(tag -> this.codec().parse(registryOps, tag).resultOrPartial(LOGGER::error))
                .ifPresent(cap -> PacketHandler.sendToPlayer(packetSource.apply(cap), player));
    }
}
