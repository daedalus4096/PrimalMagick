package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.loot.LootTablesPM;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Base data provider for all of the mod's gameplay loot tables.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractGameplayLootTableSubProvider implements LootTableSubProvider {
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();
    protected final Set<ResourceLocation> registeredLootTableTypes = new HashSet<>();

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
        this.addTables(writer);
        this.checkExpectations();
    }

    private void checkExpectations() {
        // Collect all the resource locations for the blocks defined in this mod
        Set<ResourceLocation> types = new HashSet<>(LootTablesPM.all());
        
        // Warn for each mod entity that didn't have a loot table registered
        types.removeAll(this.registeredLootTableTypes);
        types.forEach(key -> LOGGER.warn("Missing gameplay loot table for {}", key.toString()));
    }

    protected void registerEmptyLootTable(ResourceLocation loc) {
        // Just mark that it's been registered without creating a table builder, to track expectations
        this.registeredLootTableTypes.add(loc);
    }
    
    protected void registerLootTable(BiConsumer<ResourceLocation, LootTable.Builder> writer, ResourceLocation loc, LootTable.Builder builder) {
        writer.accept(loc, builder);
        this.registeredLootTableTypes.add(loc);
    }
    
    protected abstract void addTables(BiConsumer<ResourceLocation, LootTable.Builder> writer);
}
