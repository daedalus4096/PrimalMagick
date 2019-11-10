package com.verdantartifice.primalmagic.datagen;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import net.minecraft.world.storage.loot.functions.ExplosionDecay;
import net.minecraft.world.storage.loot.functions.SetCount;

public abstract class BlockLootTableProvider extends LootTableProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    
    private final DataGenerator generator;

    public BlockLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }

    protected abstract void addTables();
    
    protected void registerBasicTable(Block block) {
        LootPool.Builder poolBuilder = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block))
                .acceptCondition(SurvivesExplosion.builder());
        LootTable.Builder tableBuilder = LootTable.builder().addLootPool(poolBuilder);
        this.lootTables.put(block, tableBuilder);
    }
    
    protected void registerSlabTable(Block block) {
        LootPool.Builder poolBuilder = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block)
                .acceptFunction(SetCount.func_215932_a(ConstantRange.of(2))
                    .acceptCondition(BlockStateProperty.builder(block).with(SlabBlock.TYPE, SlabType.DOUBLE))
                ).acceptFunction(ExplosionDecay.func_215863_b()));
        LootTable.Builder tableBuilder = LootTable.builder().addLootPool(poolBuilder);
        this.lootTables.put(block, tableBuilder);
    }
    
    @Override
    public void act(DirectoryCache cache) {
        this.addTables();
        
        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : this.lootTables.entrySet()) {
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParameterSet(LootParameterSets.BLOCK).build());
        }
        
        this.writeTables(cache, tables);
    }

    private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
            } catch (IOException e) {
                PrimalMagic.LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }
}
