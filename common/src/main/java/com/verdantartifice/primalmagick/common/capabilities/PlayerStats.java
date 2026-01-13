package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncStatsPacket;
import com.verdantartifice.primalmagick.common.stats.Stat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the player statistics capability.
 * 
 * @author Daedalus4096
 */
public class PlayerStats implements IPlayerStats {
    private final Map<Identifier, Integer> stats = new ConcurrentHashMap<>();     // Map of stat locations to values
    private final Set<Long> discoveredShrines = ConcurrentHashMap.newKeySet();          // Set of long-encoded block positions of shrine locations
    private final Set<ResourceKey<Recipe<?>>> craftedRecipes = ConcurrentHashMap.newKeySet(); // Set of IDs of expertise-eligible recipes crafted by the player
    private final Set<Identifier> craftedGroups = ConcurrentHashMap.newKeySet();  // Set of IDs of expertise-eligible recipe groups crafted by the player
    private final Set<Identifier> craftedEnchs = ConcurrentHashMap.newKeySet();   // Set of IDs of expertise-eligible rune enchantments crafted by the player
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag rootTag = new CompoundTag();
        
        // Serialize recorded stat values
        ListTag statList = new ListTag();
        for (Map.Entry<Identifier, Integer> entry : stats.entrySet()) {
            if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                CompoundTag tag = new CompoundTag();
                tag.putString("Key", entry.getKey().toString());
                tag.putInt("Value", entry.getValue());
                statList.add(tag);
            }
        }
        rootTag.put("Stats", statList);
        
        // Serialize discovered shrine locations
        long[] locs = new long[this.discoveredShrines.size()];
        int index = 0;
        for (Long loc : this.discoveredShrines) {
            locs[index++] = loc;
        }
        rootTag.put("ShrineLocations", new LongArrayTag(locs));
        
        // Serialize crafted recipe IDs
        ListTag recipeList = new ListTag();
        for (ResourceKey<Recipe<?>> recipeId : this.craftedRecipes) {
            recipeList.add(StringTag.valueOf(recipeId.toString()));
        }
        rootTag.put("CraftedRecipes", recipeList);
        
        // Serialize crafted recipe group IDs
        ListTag groupList = new ListTag();
        for (Identifier groupId : this.craftedGroups) {
            groupList.add(StringTag.valueOf(groupId.toString()));
        }
        rootTag.put("CraftedGroups", groupList);
        
        // Serialize crafted rune enchantment IDs
        ListTag enchList = new ListTag();
        for (Identifier enchId : this.craftedEnchs) {
            enchList.add(StringTag.valueOf(enchId.toString()));
        }
        rootTag.put("CraftedRuneEnchantments", enchList);
        
        rootTag.putLong("SyncTimestamp", System.currentTimeMillis());
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        if (nbt == null || nbt.getLong("SyncTimestamp") <= this.syncTimestamp) {
            return;
        }

        this.syncTimestamp = nbt.getLong("SyncTimestamp");
        this.clear();
        
        // Deserialize recorded stat values
        ListTag statList = nbt.getList("Stats", Tag.TAG_COMPOUND);
        for (int index = 0; index < statList.size(); index++) {
            CompoundTag tag = statList.getCompound(index);
            Identifier loc = Identifier.parse(tag.getString("Key"));
            Integer value = Integer.valueOf(tag.getInt("Value"));
            this.stats.put(loc, value);
        }
        
        // Deserialize discovered shrine locations
        long[] locs = nbt.getLongArray("ShrineLocations");
        for (long loc : locs) {
            this.discoveredShrines.add(loc);
        }
        
        // Deserialize crafted recipe IDs
        ListTag recipeList = nbt.getList("CraftedRecipes", Tag.TAG_STRING);
        for (int index = 0; index < recipeList.size(); index++) {
            String idStr = recipeList.getString(index);
            this.craftedRecipes.add(Identifier.parse(idStr));
        }
        
        // Deserialize crafted recipe group IDs
        ListTag groupList = nbt.getList("CraftedGroups", Tag.TAG_STRING);
        for (int index = 0; index < groupList.size(); index++) {
            String idStr = groupList.getString(index);
            this.craftedGroups.add(Identifier.parse(idStr));
        }
        
        // Deserialize crafted rune enchantment IDs
        ListTag enchList = nbt.getList("CraftedRuneEnchantments", Tag.TAG_STRING);
        for (int index = 0; index < enchList.size(); index++) {
            String idStr = enchList.getString(index);
            this.craftedEnchs.add(Identifier.parse(idStr));
        }
    }

    @Override
    public void clear() {
        this.stats.clear();
        this.discoveredShrines.clear();
        this.craftedRecipes.clear();
        this.craftedGroups.clear();
        this.craftedEnchs.clear();
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
            return this.discoveredShrines.contains(pos.asLong());
        }
    }

    @Override
    public void setLocationDiscovered(BlockPos pos) {
        if (pos != null) {
            this.discoveredShrines.add(pos.asLong());
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
        return this.craftedEnchs.contains(enchantmentId);
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
        this.craftedEnchs.add(enchantmentId);
    }

    @Override
    public void sync(ServerPlayer player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncStatsPacket(player), player);
        }
    }
}
