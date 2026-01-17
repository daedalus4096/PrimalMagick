package com.verdantartifice.primalmagick.common.capabilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncStatsPacket;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.util.IdentifiedScoreEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the player statistics capability.
 * 
 * @author Daedalus4096
 */
public class PlayerStats extends AbstractCapability<PlayerStats> implements IPlayerStats {
    public static final Codec<PlayerStats> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IdentifiedScoreEntry.CODEC.listOf().<Map<Identifier, Integer>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(IdentifiedScoreEntry::id, IdentifiedScoreEntry::score)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new IdentifiedScoreEntry(e.getKey(), e.getValue())).toList()
            ).fieldOf("stats").forGetter(s -> s.stats),
            BlockPos.CODEC.listOf().<Set<BlockPos>>xmap(ImmutableSet::copyOf, ImmutableList::copyOf).fieldOf("discoveredShrines").forGetter(s -> s.discoveredShrines),
            ResourceKey.codec(Registries.RECIPE).listOf().<Set<ResourceKey<Recipe<?>>>>xmap(ImmutableSet::copyOf, ImmutableList::copyOf).fieldOf("craftedRecipes").forGetter(s -> s.craftedRecipes),
            Identifier.CODEC.listOf().<Set<Identifier>>xmap(ImmutableSet::copyOf, ImmutableList::copyOf).fieldOf("craftedGroups").forGetter(s -> s.craftedGroups),
            Identifier.CODEC.listOf().<Set<Identifier>>xmap(ImmutableSet::copyOf, ImmutableList::copyOf).fieldOf("craftedEnchants").forGetter(s -> s.craftedEnchants),
            Codec.LONG.fieldOf("syncTimestamp").forGetter(AbstractCapability::getSyncTimestamp)
        ).apply(instance, PlayerStats::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerStats> STREAM_CODEC = StreamCodec.composite(
            IdentifiedScoreEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(IdentifiedScoreEntry::id, IdentifiedScoreEntry::score)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new IdentifiedScoreEntry(e.getKey(), e.getValue())).toList()
            ), s -> s.stats,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ImmutableSet::copyOf, ImmutableList::copyOf), s -> s.discoveredShrines,
            ResourceKey.streamCodec(Registries.RECIPE).apply(ByteBufCodecs.list()).map(ImmutableSet::copyOf, ImmutableList::copyOf), s -> s.craftedRecipes,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ImmutableSet::copyOf, ImmutableList::copyOf), s -> s.craftedGroups,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ImmutableSet::copyOf, ImmutableList::copyOf), s -> s.craftedEnchants,
            ByteBufCodecs.VAR_LONG, AbstractCapability::getSyncTimestamp,
            PlayerStats::new);

    private final Map<Identifier, Integer> stats = new ConcurrentHashMap<>();       // Map of stat locations to values
    private final Set<BlockPos> discoveredShrines = ConcurrentHashMap.newKeySet();              // Set of long-encoded block positions of shrine locations
    private final Set<ResourceKey<Recipe<?>>> craftedRecipes = ConcurrentHashMap.newKeySet();   // Set of IDs of expertise-eligible recipes crafted by the player
    private final Set<Identifier> craftedGroups = ConcurrentHashMap.newKeySet();    // Set of IDs of expertise-eligible recipe groups crafted by the player
    private final Set<Identifier> craftedEnchants = ConcurrentHashMap.newKeySet();  // Set of IDs of expertise-eligible rune enchantments crafted by the player

    public PlayerStats() {
        this(Map.of(), Set.of(), Set.of(), Set.of(), Set.of(), 0L);
    }

    protected PlayerStats(Map<Identifier, Integer> stats, Set<BlockPos> discoveredShrines, Set<ResourceKey<Recipe<?>>> craftedRecipes,
                          Set<Identifier> craftedGroups, Set<Identifier> craftedEnchants, long syncTimestamp) {
        this.stats.putAll(stats);
        this.discoveredShrines.addAll(discoveredShrines);
        this.craftedRecipes.addAll(craftedRecipes);
        this.craftedGroups.addAll(craftedGroups);
        this.craftedEnchants.addAll(craftedEnchants);
        this.setSyncTimestamp(syncTimestamp);
    }

    @Override
    public void copyFromInner(@NotNull PlayerStats other) {
        this.clear();
        this.stats.putAll(other.stats);
        this.discoveredShrines.addAll(other.discoveredShrines);
        this.craftedRecipes.addAll(other.craftedRecipes);
        this.craftedGroups.addAll(other.craftedGroups);
        this.craftedEnchants.addAll(other.craftedEnchants);
    }

    @Override
    public void clear() {
        this.stats.clear();
        this.discoveredShrines.clear();
        this.craftedRecipes.clear();
        this.craftedGroups.clear();
        this.craftedEnchants.clear();
    }

    @Override
    public Codec<PlayerStats> codec() {
        return CODEC;
    }

    @Override
    public int getValue(Stat stat) {
        if (stat == null) {
            return 0;
        } else {
            return this.stats.getOrDefault(stat.key(), 0);
        }
    }

    @Override
    public void setValue(Stat stat, int value) {
        if (stat != null) {
            this.stats.put(stat.key(), value);
        }
    }

    @Override
    public boolean isLocationDiscovered(BlockPos pos) {
        if (pos == null) {
            return false;
        } else {
            return this.discoveredShrines.contains(pos);
        }
    }

    @Override
    public void setLocationDiscovered(BlockPos pos) {
        if (pos != null) {
            this.discoveredShrines.add(pos);
        }
    }

    @Override
    public boolean isRecipeCrafted(ResourceKey<Recipe<?>> recipeId) {
        return this.craftedRecipes.contains(recipeId);
    }

    @Override
    public boolean isRecipeGroupCrafted(Identifier groupId) {
        return this.craftedGroups.contains(groupId);
    }

    @Override
    public boolean isRuneEnchantmentCrafted(Identifier enchantmentId) {
        return this.craftedEnchants.contains(enchantmentId);
    }

    @Override
    public void setRecipeCrafted(ResourceKey<Recipe<?>> recipeId) {
        this.craftedRecipes.add(recipeId);
    }

    @Override
    public void setRecipeGroupCrafted(Identifier groupId) {
        this.craftedGroups.add(groupId);
    }

    @Override
    public void setRuneEnchantmentCrafted(Identifier enchantmentId) {
        this.craftedEnchants.add(enchantmentId);
    }

    @Override
    public void sync(ServerPlayer player) {
        this.sync(player, SyncStatsPacket::new);
    }
}
