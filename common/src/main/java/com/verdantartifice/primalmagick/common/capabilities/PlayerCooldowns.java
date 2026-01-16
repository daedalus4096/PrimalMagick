package com.verdantartifice.primalmagick.common.capabilities;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCooldownsPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the player cooldown capability.
 * 
 * @author Daedalus4096
 */
public class PlayerCooldowns implements IPlayerCooldowns {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Codec<PlayerCooldowns> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().<Map<CooldownType, Long>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::type, Entry::recoveryTime)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ).fieldOf("cooldowns").forGetter(c -> c.cooldowns),
            Codec.LONG.optionalFieldOf("syncTimestamp", 0L).forGetter(c -> c.syncTimestamp)
        ).apply(instance, PlayerCooldowns::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCooldowns> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::type, Entry::recoveryTime)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ), c -> c.cooldowns,
            ByteBufCodecs.VAR_LONG, c -> c.syncTimestamp,
            PlayerCooldowns::new);

    private final Map<CooldownType, Long> cooldowns = new ConcurrentHashMap<>();    // Map of cooldown types to recovery times, in system milliseconds
    private long syncTimestamp;    // Last timestamp at which this capability received a sync from the server

    public PlayerCooldowns() {
        this(Map.of(), 0L);
    }

    protected PlayerCooldowns(Map<CooldownType, Long> cooldowns, long syncTimestamp) {
        this.cooldowns.putAll(cooldowns);
        this.syncTimestamp = syncTimestamp;
    }

    @Override
    public Tag serializeNBT(@NotNull HolderLookup.Provider registries) {
        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        this.syncTimestamp = System.currentTimeMillis();
        return CODEC.encodeStart(registryOps, this)
                .resultOrPartial(msg -> LOGGER.error("Failed to serialize player cooldowns: {}", msg))
                .orElse(null);
    }

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider registries, @NotNull Tag nbt) {
        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        CODEC.parse(registryOps, nbt)
                .resultOrPartial(LOGGER::error)
                .ifPresent(this::copyFrom);
    }

    public void copyFrom(@Nullable PlayerCooldowns other) {
        if (other == null || other.syncTimestamp <= this.syncTimestamp) {
            return;
        }

        this.syncTimestamp = other.syncTimestamp;
        this.clear();

        this.cooldowns.putAll(other.cooldowns);
    }

    @Override
    public void clear() {
        this.cooldowns.clear();
    }

    @Override
    public boolean isOnCooldown(CooldownType type) {
        if (type == null) {
            return false;
        }
        // The cooldown is still active if the stored recovery time is greater than the current system time
        return (this.cooldowns.getOrDefault(type, 0L) > System.currentTimeMillis());
    }
    
    @Override
    public long getRemainingCooldown(CooldownType type) {
        return Math.max(0, this.cooldowns.getOrDefault(type, 0L) - System.currentTimeMillis());
    }

    @Override
    public void setCooldown(CooldownType type, int durationTicks) {
        if (type != null) {
            this.cooldowns.put(type, (System.currentTimeMillis() + (durationTicks * 50L)));
        }
    }

    @Override
    public void sync(ServerPlayer player) {
        if (player != null) {
            this.syncTimestamp = System.currentTimeMillis();

            // Clone this data before passing it to the network
            RegistryOps<Tag> registryOps = player.registryAccess().createSerializationContext(NbtOps.INSTANCE);
            CODEC.encodeStart(registryOps, this)
                    .resultOrPartial(err -> LOGGER.error("Failed to encode cooldown data for syncing"))
                    .flatMap(tag -> CODEC.parse(registryOps, tag)
                            .resultOrPartial(err -> LOGGER.error("Failed to parse cooldown data for syncing")))
                    .ifPresent(cooldowns -> PacketHandler.sendToPlayer(new SyncCooldownsPacket(cooldowns), player));
        }
    }

    protected record Entry(CooldownType type, long recoveryTime) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                CooldownType.CODEC.fieldOf("type").forGetter(Entry::type),
                Codec.LONG.fieldOf("recoveryTime").forGetter(Entry::recoveryTime)
            ).apply(instance, Entry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                CooldownType.STREAM_CODEC, Entry::type,
                ByteBufCodecs.VAR_LONG, Entry::recoveryTime,
                Entry::new);
    }
}
