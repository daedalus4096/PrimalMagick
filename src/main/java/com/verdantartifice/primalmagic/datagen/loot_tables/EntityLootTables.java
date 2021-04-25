package com.verdantartifice.primalmagic.datagen.loot_tables;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Data provider for all of the mod's entity loot tables.
 * 
 * @author Daedalus4096
 */
public class EntityLootTables implements IDataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();

    protected final Map<EntityType<?>, LootTable.Builder> lootTables = new HashMap<>();
    protected final Set<ResourceLocation> registeredEntities = new HashSet<>();
    
    private final DataGenerator generator;

    public EntityLootTables(DataGenerator dataGeneratorIn) {
        this.generator = dataGeneratorIn;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        // Register all the loot tables with this provider
        this.addTables();

        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<EntityType<?>, LootTable.Builder> entry : this.lootTables.entrySet()) {
            // For each entry in the map, build the loot table and associate it with the entity's loot table location
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParameterSet(LootParameterSets.ENTITY).build());
        }
        
        // Write out the loot table files to disk
        this.writeTables(cache, tables);
        
        // Check the registered loot tables against the registered block set for the mod
        this.checkExpectations();
    }

    private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }
    
    private void checkExpectations() {
        // Collect all the resource locations for the blocks defined in this mod
        Set<ResourceLocation> entityTypes = ForgeRegistries.ENTITIES.getKeys().stream().filter(loc -> loc.getNamespace().equals(PrimalMagic.MODID)).collect(Collectors.toSet());
        
        // Warn for each mod entity that didn't have a loot table registered
        entityTypes.removeAll(this.registeredEntities);
        entityTypes.forEach(key -> LOGGER.warn("Missing entity loot table for {}", key.toString()));
    }

    @Override
    public String getName() {
        return "Primal Magic Entity Loot Tables";
    }
    
    private void registerEmptyLootTable(EntityType<?> type) {
        // Just mark that it's been registered without creating a table builder, to track expectations
        this.registeredEntities.add(type.getRegistryName());
    }
    
    private void registerLootTable(EntityType<?> type, LootTable.Builder builder) {
        this.lootTables.put(type, builder);
        this.registeredEntities.add(type.getRegistryName());
    }
    
    protected void addTables() {
        this.registerEmptyLootTable(EntityTypesPM.SPELL_MINE.get());
        this.registerEmptyLootTable(EntityTypesPM.SPELL_PROJECTILE.get());
        this.registerLootTable(EntityTypesPM.PRIMALITE_GOLEM.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(ItemsPM.PRIMALITE_INGOT.get()).acceptFunction(SetCount.builder(RandomValueRange.of(3.0F, 5.0F))))));
        this.registerLootTable(EntityTypesPM.HEXIUM_GOLEM.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(ItemsPM.HEXIUM_INGOT.get()).acceptFunction(SetCount.builder(RandomValueRange.of(3.0F, 5.0F))))));
        this.registerLootTable(EntityTypesPM.HALLOWSTEEL_GOLEM.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(ItemsPM.HALLOWSTEEL_INGOT.get()).acceptFunction(SetCount.builder(RandomValueRange.of(3.0F, 5.0F))))));
    }
}
