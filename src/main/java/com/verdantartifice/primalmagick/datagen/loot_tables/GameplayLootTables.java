package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

/**
 * Data provider for all of the mod's gameplay loot tables (e.g. treefolk bartering).
 * 
 * @author Daedalus4096
 */
public class GameplayLootTables implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    protected final Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();
    protected final Set<ResourceLocation> registeredLootTableTypes = new HashSet<>();
    private final DataGenerator generator;

    public GameplayLootTables(DataGenerator dataGeneratorIn) {
        this.generator = dataGeneratorIn;
    }

    @Override
    public void run(CachedOutput pOutput) throws IOException {
        // Register all the loot tables with this provider
        this.addTables();

        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<ResourceLocation, LootTable.Builder> entry : this.lootTables.entrySet()) {
            // For each entry in the map, build the loot table and associate it with the entity's loot table location
            tables.put(entry.getKey(), entry.getValue().build());
        }
        
        // Write out the loot table files to disk
        this.writeTables(pOutput, tables);
        
        // Check the registered loot tables against the registered set for the mod
        this.checkExpectations();
    }

    private void writeTables(CachedOutput cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                DataProvider.saveStable(cache, LootTables.serialize(lootTable), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }
    
    private void checkExpectations() {
        // Collect all the resource locations for the blocks defined in this mod
        Set<ResourceLocation> types = new HashSet<>(LootTablesPM.all());
        
        // Warn for each mod entity that didn't have a loot table registered
        types.removeAll(this.registeredLootTableTypes);
        types.forEach(key -> LOGGER.warn("Missing gameplay loot table for {}", key.toString()));
    }

    @Override
    public String getName() {
        return "Primal Magick Gameplay Loot Tables";
    }

    @SuppressWarnings("unused")
    private void registerEmptyLootTable(ResourceLocation loc) {
        // Just mark that it's been registered without creating a table builder, to track expectations
        this.registeredLootTableTypes.add(loc);
    }
    
    private void registerLootTable(ResourceLocation loc, LootTable.Builder builder) {
        this.lootTables.put(loc, builder);
        this.registeredLootTableTypes.add(loc);
    }
    
    protected void addTables() {
        this.registerLootTable(LootTablesPM.TREEFOLK_BARTERING, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootTableReference.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_FOOD).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_SEEDS).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_JUNK).setWeight(15))
                .add(LootTableReference.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_TREASURE).setWeight(5))));
        this.registerLootTable(LootTablesPM.TREEFOLK_BARTERING_FOOD, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.APPLE).setWeight(20))));
        this.registerLootTable(LootTablesPM.TREEFOLK_BARTERING_SEEDS, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.MANGROVE_PROPAGULE).setWeight(10))
                .add(LootItem.lootTableItem(Items.COCOA_BEANS).setWeight(10))));
        this.registerLootTable(LootTablesPM.TREEFOLK_BARTERING_JUNK, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.LILY_PAD).setWeight(10))));
        this.registerLootTable(LootTablesPM.TREEFOLK_BARTERING_TREASURE, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(10))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(new EnchantRandomlyFunction.Builder().withEnchantment(EnchantmentsPM.VERDANT.get())))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(EnchantmentsPM.BOUNTY.get())))));
    }
}
