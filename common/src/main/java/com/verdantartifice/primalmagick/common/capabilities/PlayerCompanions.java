package com.verdantartifice.primalmagick.common.capabilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCompanionsPacket;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the player companion capability.
 * 
 * @author Daedalus4096
 */
public class PlayerCompanions extends AbstractCapability<PlayerCompanions> implements IPlayerCompanions {
    public static final Codec<PlayerCompanions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().<Map<CompanionType, LinkedList<UUID>>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::companionType, Entry::idList)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ).fieldOf("companions").forGetter(c -> c.companions),
            Codec.LONG.optionalFieldOf("syncTimestamp", 0L).forGetter(AbstractCapability::getSyncTimestamp)
        ).apply(instance, PlayerCompanions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCompanions> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::companionType, Entry::idList)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ), c -> c.companions,
            ByteBufCodecs.VAR_LONG, AbstractCapability::getSyncTimestamp,
            PlayerCompanions::new);

    private final Map<CompanionType, LinkedList<UUID>> companions = new ConcurrentHashMap<>();

    public PlayerCompanions() {
        this(Map.of(), 0L);
    }

    protected PlayerCompanions(Map<CompanionType, LinkedList<UUID>> companions, long syncTimestamp) {
        super(syncTimestamp);
        this.companions.putAll(companions);
    }

    @Override
    public Codec<PlayerCompanions> codec() {
        return CODEC;
    }

    @Override
    protected void copyFromInner(@NotNull PlayerCompanions other) {
        this.clear();
        this.companions.putAll(other.companions);
    }

    @Override
    public UUID add(CompanionType type, UUID id) {
        LinkedList<UUID> list = this.companions.computeIfAbsent(type, (t) -> new LinkedList<>());
        if (list.contains(id)) {
            return null;
        } else {
            list.add(id);
            if (list.size() > type.getLimit()) {
                return list.pollFirst();
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean contains(CompanionType type, UUID id) {
        return this.companions.getOrDefault(type, new LinkedList<>()).contains(id);
    }

    @Override
    public List<UUID> get(CompanionType type) {
        return Collections.unmodifiableList(this.companions.getOrDefault(type, new LinkedList<>()));
    }

    @Override
    public boolean remove(CompanionType type, UUID id) {
        return this.companions.getOrDefault(type, new LinkedList<>()).remove(id);
    }

    @Override
    public void clear() {
        this.companions.clear();
    }

    @Override
    public void sync(@NotNull ServerPlayer player) {
        this.sync(player, SyncCompanionsPacket::new);
    }

    protected record Entry(CompanionType companionType, LinkedList<UUID> idList) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                CompanionType.CODEC.fieldOf("companionType").forGetter(Entry::companionType),
                UUIDUtil.CODEC.listOf().xmap(LinkedList::new, ImmutableList::copyOf).fieldOf("idList").forGetter(Entry::idList)
            ).apply(instance, Entry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                CompanionType.STREAM_CODEC, Entry::companionType,
                UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()).map(LinkedList::new, ImmutableList::copyOf), Entry::idList,
                Entry::new
        );
    }
}
