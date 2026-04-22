package com.verdantartifice.primalmagick.common.menus.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.hash.HashCode;
import com.mojang.serialization.DynamicOps;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.ContainerSetVarintDataPacket;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.HashOps;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.inventory.RemoteSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of the Minecraft {@link ContainerSynchronizer} interface that syncs data slots
 * as var-ints rather than shorts to allow for larger data values.
 * 
 * @author Daedalus4096
 */
public class ContainerSynchronizerLarge implements ContainerSynchronizer {
    private final ServerPlayer player;

    private final LoadingCache<TypedDataComponent<?>, Integer> cache = CacheBuilder.newBuilder().maximumSize(256L).build(new CacheLoader<>() {
        private final DynamicOps<HashCode> registryHashOps = ContainerSynchronizerLarge.this.player.registryAccess().createSerializationContext(HashOps.CRC32C_INSTANCE);

        public Integer load(TypedDataComponent<?> component) {
            return component.encodeValue(this.registryHashOps).getOrThrow((message) -> new IllegalArgumentException("Failed to hash " + component + ": " + message)).asInt();
        }
    });

    public ContainerSynchronizerLarge(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void sendInitialData(@NotNull AbstractContainerMenu pContainer, @NotNull List<ItemStack> pItems, @NotNull ItemStack pCarriedItem, int[] pInitialData) {
        this.player.connection.send(new ClientboundContainerSetContentPacket(pContainer.containerId, pContainer.incrementStateId(), pItems, pCarriedItem));
        for (int index = 0; index < pInitialData.length; index++) {
            this.broadcastDataValue(pContainer, index, pInitialData[index]);
        }
    }

    @Override
    public void sendSlotChange(@NotNull AbstractContainerMenu pContainer, int pSlot, @NotNull ItemStack pItemStack) {
        this.player.connection.send(new ClientboundContainerSetSlotPacket(pContainer.containerId, pContainer.incrementStateId(), pSlot, pItemStack));
    }

    @Override
    public void sendCarriedChange(@NotNull AbstractContainerMenu pContainerMenu, @NotNull ItemStack pStack) {
        this.player.connection.send(new ClientboundContainerSetSlotPacket(-1, pContainerMenu.incrementStateId(), -1, pStack));
    }

    @Override
    public void sendDataChange(@NotNull AbstractContainerMenu pContainer, int pId, int pValue) {
        this.broadcastDataValue(pContainer, pId, pValue);
    }
    
    protected void broadcastDataValue(AbstractContainerMenu pContainer, int pId, int pValue) {
        // Send custom packet
        PacketHandler.sendToPlayer(new ContainerSetVarintDataPacket(pContainer.containerId, pId, pValue), this.player);
    }

    @Override
    @NotNull
    public RemoteSlot createSlot() {
        Objects.requireNonNull(this.cache);
        return new RemoteSlot.Synchronized(this.cache::getUnchecked);
    }
}
