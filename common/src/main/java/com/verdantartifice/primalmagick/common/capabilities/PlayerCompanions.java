package com.verdantartifice.primalmagick.common.capabilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCompanionsPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
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
public class PlayerCompanions implements IPlayerCompanions {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Codec<PlayerCompanions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().<Map<CompanionType, LinkedList<UUID>>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::companionType, Entry::idList)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ).fieldOf("companions").forGetter(c -> c.companions),
            Codec.LONG.optionalFieldOf("syncTimestamp", 0L).forGetter(c -> c.syncTimestamp)
        ).apply(instance, PlayerCompanions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCompanions> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::companionType, Entry::idList)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ), c -> c.companions,
            ByteBufCodecs.VAR_LONG, a -> a.syncTimestamp,
            PlayerCompanions::new);

    private final Map<CompanionType, LinkedList<UUID>> companions = new ConcurrentHashMap<>();
    private long syncTimestamp;    // Last timestamp at which this capability received a sync from the server

    public PlayerCompanions() {
        this(Map.of(), 0L);
    }

    protected PlayerCompanions(Map<CompanionType, LinkedList<UUID>> companions, long syncTimestamp) {
        this.companions.putAll(companions);
        this.syncTimestamp = syncTimestamp;
    }

    @Override
    public Tag serializeNBT(@NotNull HolderLookup.Provider registries) {
        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        this.syncTimestamp = System.currentTimeMillis();
        return CODEC.encodeStart(registryOps, this)
                .resultOrPartial(msg -> LOGGER.error("Failed to serialize player attunements: {}", msg))
                .orElse(null);
    }

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider registries, @NotNull Tag nbt) {
        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        CODEC.parse(registryOps, nbt)
                .resultOrPartial(LOGGER::error)
                .ifPresent(this::copyFrom);
    }

    public void copyFrom(@Nullable PlayerCompanions other) {
        if (other == null || other.syncTimestamp <= this.syncTimestamp) {
            return;
        }

        this.syncTimestamp = other.syncTimestamp;
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
    public void sync(ServerPlayer player) {
        if (player != null) {
            this.syncTimestamp = System.currentTimeMillis();

            // Clone this data before passing it to the network
            RegistryOps<Tag> registryOps = player.level().registryAccess().createSerializationContext(NbtOps.INSTANCE);
            CODEC.encodeStart(registryOps, this)
                    .resultOrPartial(err -> LOGGER.error("Failed to encode companion data for syncing"))
                    .flatMap(tag -> CODEC.parse(registryOps, tag)
                            .resultOrPartial(err -> LOGGER.error("Failed to parse companion data for syncing")))
                    .ifPresent(companions -> PacketHandler.sendToPlayer(new SyncCompanionsPacket(companions), player));
        }
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
