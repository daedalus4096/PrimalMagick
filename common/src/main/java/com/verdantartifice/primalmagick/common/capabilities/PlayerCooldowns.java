package com.verdantartifice.primalmagick.common.capabilities;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCooldownsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the player cooldown capability.
 * 
 * @author Daedalus4096
 */
public class PlayerCooldowns extends AbstractCapability<PlayerCooldowns> implements IPlayerCooldowns {
    public static final Codec<PlayerCooldowns> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().<Map<CooldownType, Long>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::type, Entry::recoveryTime)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ).fieldOf("cooldowns").forGetter(c -> c.cooldowns),
            Codec.LONG.optionalFieldOf("syncTimestamp", 0L).forGetter(AbstractCapability::getSyncTimestamp)
        ).apply(instance, PlayerCooldowns::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCooldowns> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::type, Entry::recoveryTime)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ), c -> c.cooldowns,
            ByteBufCodecs.VAR_LONG, AbstractCapability::getSyncTimestamp,
            PlayerCooldowns::new);

    private final Map<CooldownType, Long> cooldowns = new ConcurrentHashMap<>();    // Map of cooldown types to recovery times, in system milliseconds

    public PlayerCooldowns() {
        this(Map.of(), 0L);
    }

    protected PlayerCooldowns(Map<CooldownType, Long> cooldowns, long syncTimestamp) {
        super(syncTimestamp);
        this.cooldowns.putAll(cooldowns);
    }

    @Override
    public Codec<PlayerCooldowns> codec() {
        return CODEC;
    }

    @Override
    protected void copyFromInner(@NotNull PlayerCooldowns other) {
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
    public void sync(@NotNull ServerPlayer player) {
        this.sync(player, SyncCooldownsPacket::new);
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
