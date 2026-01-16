package com.verdantartifice.primalmagick.common.capabilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncAttunementsPacket;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the player attunements capability.
 * 
 * @author Daedalus4096
 */
public class PlayerAttunements implements IPlayerAttunements {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Codec<PlayerAttunements> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().<Map<Source, Map<AttunementType, Integer>>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::source, Entry::typeVals)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ).fieldOf("attunements").forGetter(a -> a.attunements),
            Source.CODEC.listOf().<Set<Source>>xmap(ImmutableSet::copyOf, ImmutableList::copyOf).fieldOf("suppressions").forGetter(a -> a.suppressions),
            Codec.LONG.optionalFieldOf("syncTimestamp", 0L).forGetter(a -> a.syncTimestamp)
    ).apply(instance, PlayerAttunements::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerAttunements> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(Entry::source, Entry::typeVals)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList()
            ), a -> a.attunements,
            Source.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ImmutableSet::copyOf, ImmutableList::copyOf), a -> a.suppressions,
            ByteBufCodecs.VAR_LONG, a -> a.syncTimestamp,
            PlayerAttunements::new
    );

    // Nested map of sources to attunement types to values
    private final Map<Source, Map<AttunementType, Integer>> attunements = new ConcurrentHashMap<>();
    
    // Set of sources currently having their attunements suppressed
    private final Set<Source> suppressions = ConcurrentHashMap.newKeySet();
    
    private long syncTimestamp;    // Last timestamp at which this capability received a sync from the server

    public PlayerAttunements() {
        this(Map.of(), Set.of(), 0L);
    }

    protected PlayerAttunements(Map<Source, Map<AttunementType, Integer>> attunements, Set<Source> suppressions, long syncTimestamp) {
        this.attunements.putAll(attunements);
        this.suppressions.addAll(suppressions);
        this.syncTimestamp = syncTimestamp;
    }

    @Override
    public Tag serializeNBT(@NotNull HolderLookup.Provider registries) {
        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        this.syncTimestamp = System.currentTimeMillis();
        return CODEC.encodeStart(registryOps, this)
                .resultOrPartial(msg -> LOGGER.error("Failed to serialize player knowledge: {}", msg))
                .orElse(null);
    }

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider registries, @NotNull Tag nbt) {
        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        CODEC.parse(registryOps, nbt)
                .resultOrPartial(LOGGER::error)
                .ifPresent(this::copyFrom);
    }

    public void copyFrom(@Nullable PlayerAttunements other) {
        if (other == null || other.syncTimestamp <= this.syncTimestamp) {
            return;
        }

        this.syncTimestamp = other.syncTimestamp;
        this.clear();

        this.attunements.putAll(other.attunements);
        this.suppressions.addAll(other.suppressions);
    }

    @Override
    public void clear() {
        this.attunements.clear();
        this.suppressions.clear();
    }

    @Override
    public int getValue(Source source, AttunementType type) {
        return this.attunements.getOrDefault(source, Collections.emptyMap()).getOrDefault(type, 0);
    }

    @Override
    public void setValue(Source source, AttunementType type, int value) {
        // Don't allow null keys or values in the data
        if (source != null && type != null) {
            // Get the map of types to values for the source, creating an empty one if it doesn't exist
            Map<AttunementType, Integer> typeMap = this.attunements.computeIfAbsent(source, k -> new ConcurrentHashMap<>());
            
            // Determine if the value to be set must be capped
            int toSet = type.isCapped() ? Mth.clamp(value, 0, type.getMaximum()) : Math.max(0, value);
            
            // Add the given value to the type map
            typeMap.put(type, toSet);
        }
    }

    @Override
    public boolean isSuppressed(Source source) {
        return this.suppressions.contains(source);
    }

    @Override
    public void setSuppressed(Source source, boolean value) {
        // Don't allow null keys in the set
        if (source != null) {
            boolean present = this.isSuppressed(source);
            if (!present && value) {
                this.suppressions.add(source);
            } else if (present && !value) {
                this.suppressions.remove(source);
            }
        }
    }

    @Override
    public void sync(ServerPlayer player) {
        if (player != null) {
            this.syncTimestamp = System.currentTimeMillis();

            // Clone this data before passing it to the network
            RegistryOps<Tag> registryOps = player.level().registryAccess().createSerializationContext(NbtOps.INSTANCE);
            CODEC.encodeStart(registryOps, this)
                    .resultOrPartial(err -> LOGGER.error("Failed to encode attunement data for syncing"))
                    .flatMap(tag -> CODEC.parse(registryOps, tag)
                            .resultOrPartial(err -> LOGGER.error("Failed to parse attunement data for syncing")))
                    .ifPresent(attunements -> PacketHandler.sendToPlayer(new SyncAttunementsPacket(attunements), player));
        }
    }

    protected record Entry(Source source, Map<AttunementType, Integer> typeVals) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Source.CODEC.fieldOf("source").forGetter(Entry::source),
                SubEntry.CODEC.listOf().<Map<AttunementType, Integer>>xmap(
                        subEntryList -> subEntryList.stream().collect(ImmutableMap.toImmutableMap(SubEntry::type, SubEntry::value)),
                        subEntryMap -> subEntryMap.entrySet().stream().map(e -> new SubEntry(e.getKey(), e.getValue())).toList()
                ).fieldOf("typeVals").forGetter(e -> e.typeVals)
        ).apply(instance, Entry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                Source.STREAM_CODEC, Entry::source,
                SubEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                        subEntryList -> subEntryList.stream().collect(ImmutableMap.toImmutableMap(SubEntry::type, SubEntry::value)),
                        subEntryMap -> subEntryMap.entrySet().stream().map(e -> new SubEntry(e.getKey(), e.getValue())).toList()
                ), Entry::typeVals,
                Entry::new
        );
    }

    protected record SubEntry(AttunementType type, int value) {
        public static final Codec<SubEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                AttunementType.CODEC.fieldOf("type").forGetter(SubEntry::type),
                Codec.INT.fieldOf("value").forGetter(SubEntry::value)
        ).apply(instance, SubEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SubEntry> STREAM_CODEC = StreamCodec.composite(
                AttunementType.STREAM_CODEC, SubEntry::type,
                ByteBufCodecs.VAR_INT, SubEntry::value,
                SubEntry::new
        );
    }
}
