package com.verdantartifice.primalmagick.datagen.loot_tables;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Base data provider for all of the mod's gameplay loot tables.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractGameplayLootTableSubProvider implements LootTableSubProvider {
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final HolderLookup.Provider registries;
    protected final Map<ResourceKey<LootTable>, LootTable.Builder> lootTables = new HashMap<>();
    protected final Set<ResourceKey<LootTable>> registeredLootTableTypes = new HashSet<>();
    protected final Supplier<Set<ResourceKey<LootTable>>> expectedTableSupplier;
    
    protected AbstractGameplayLootTableSubProvider(HolderLookup.Provider registries, Supplier<Set<ResourceKey<LootTable>>> expectedTableSupplier) {
        this.registries = registries;
        this.expectedTableSupplier = expectedTableSupplier;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> pOutput) {
        this.addTables(pOutput);
        this.checkExpectations();
    }

    private void checkExpectations() {
        // Collect all the resource locations for the blocks defined in this mod
        Set<ResourceKey<LootTable>> types = new HashSet<>(this.expectedTableSupplier.get());
        
        // Warn for each mod entity that didn't have a loot table registered
        types.removeAll(this.registeredLootTableTypes);
        types.forEach(key -> LOGGER.warn("Missing gameplay loot table for {}", key.toString()));
    }

    protected void registerEmptyLootTable(ResourceKey<LootTable> loc) {
        // Just mark that it's been registered without creating a table builder, to track expectations
        this.registeredLootTableTypes.add(loc);
    }
    
    protected void registerLootTable(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> writer, ResourceKey<LootTable> loc, LootTable.Builder builder) {
        writer.accept(loc, builder);
        this.registeredLootTableTypes.add(loc);
    }
    
    protected abstract void addTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> pOutput);
}
