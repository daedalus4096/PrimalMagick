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

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.ConstantIntValue;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Base class for the block loot table provider for the mod.  Handles the infrastructure so that the
 * subclass can just list registrations.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractBlockLootTableProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();

    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    protected final Set<ResourceLocation> registeredBlocks = new HashSet<>();
    
    private final DataGenerator generator;

    public AbstractBlockLootTableProvider(DataGenerator dataGeneratorIn) {
        this.generator = dataGeneratorIn;
    }

    /**
     * Add the mod's block loot tables to this provider's map for writing.
     */
    protected abstract void addTables();
    
    private void registerLootTableBuiler(Block block, LootTable.Builder builder) {
        this.registeredBlocks.add(block.getRegistryName());
        this.lootTables.put(block, builder);
    }
    
    protected void registerEmptyTable(Block block) {
        // Just mark that it's been registered without creating a table builder, to track expectations
        this.registeredBlocks.add(block.getRegistryName());
    }
    
    protected void registerBasicTable(Block block) {
        LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(block))
                .when(ExplosionCondition.survivesExplosion());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(poolBuilder);
        this.registerLootTableBuiler(block, tableBuilder);
    }
    
    protected void registerSlabTable(Block block) {
        LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(block)
                .apply(SetItemCountFunction.setCount(ConstantIntValue.exactly(2))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                ).apply(ApplyExplosionDecay.explosionDecay()));
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(poolBuilder);
        this.registerLootTableBuiler(block, tableBuilder);
    }
    
    protected void registerLeavesTable(Block leavesBlock, Block saplingBlock) {
        float[] saplingFortuneChances = new float[] { 0.05F, 0.0625F, 0.083333336F, 0.1F };
        float[] stickFortuneChances = new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F };
        LootItemCondition.Builder shearsOrSilkTouch = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))
                .or(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))));
        LootPoolEntryContainer.Builder<?> saplingEntryBuilder = LootItem.lootTableItem(saplingBlock).when(ExplosionCondition.survivesExplosion()).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, saplingFortuneChances));
        LootPoolEntryContainer.Builder<?> leavesEntryBuilder = LootItem.lootTableItem(leavesBlock).when(shearsOrSilkTouch);
        LootPool.Builder saplingAndLeavesPool = LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(leavesEntryBuilder.otherwise(saplingEntryBuilder));
        LootPoolEntryContainer.Builder<?> stickEntryBuilder = LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))).apply(ApplyExplosionDecay.explosionDecay()).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, stickFortuneChances));
        LootPool.Builder stickPool = LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).when(shearsOrSilkTouch.invert()).add(stickEntryBuilder);
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(saplingAndLeavesPool).withPool(stickPool);
        this.registerLootTableBuiler(leavesBlock, tableBuilder);
    }
    
    protected void registerInfusedStoneTable(Block stoneBlock, Item dustItem) {
        LootPool.Builder stonePoolBuilder = LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(Blocks.COBBLESTONE))
                .when(ExplosionCondition.survivesExplosion());
        LootPool.Builder dustPoolBuilder = LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(dustItem))
                .when(ExplosionCondition.survivesExplosion());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(stonePoolBuilder).withPool(dustPoolBuilder);
        this.registerLootTableBuiler(stoneBlock, tableBuilder);
    }
    
    protected void registerGemOreTable(Block oreBlock, Item gemItem) {
        LootItemCondition.Builder silkTouch = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
        LootPoolEntryContainer.Builder<?> gemEntryBuilder = LootItem.lootTableItem(gemItem).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).apply(ApplyExplosionDecay.explosionDecay());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(oreBlock).when(silkTouch).otherwise(gemEntryBuilder)));
        this.registerLootTableBuiler(oreBlock, tableBuilder);
    }
    
    protected void registerMultiGemOreTable(Block oreBlock, Item gemItem, float minGems, float maxGems) {
        LootItemCondition.Builder silkTouch = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
        LootPoolEntryContainer.Builder<?> gemEntryBuilder = LootItem.lootTableItem(gemItem).apply(SetItemCountFunction.setCount(RandomValueBounds.between(minGems, maxGems))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)).apply(ApplyExplosionDecay.explosionDecay());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(oreBlock).when(silkTouch).otherwise(gemEntryBuilder)));
        this.registerLootTableBuiler(oreBlock, tableBuilder);
    }
    
    protected void registerManaBearingDeviceTable(Block block) {
        LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(block).apply(CopyNbtFunction.copyData(CopyNbtFunction.DataSource.BLOCK_ENTITY).copy("ManaStorage.Mana.Sources", "ManaContainerTag.Sources")))
                .when(ExplosionCondition.survivesExplosion());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(poolBuilder);
        this.registerLootTableBuiler(block, tableBuilder);
    }
    
    @Override
    public void run(HashCache cache) {
        // Register all the loot tables with this provider
        this.addTables();
        
        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : this.lootTables.entrySet()) {
            // For each entry in the map, build the loot table and associate it with the block's loot table location
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParamSet(LootContextParamSets.BLOCK).build());
        }
        
        // Write out the loot table files to disk
        this.writeTables(cache, tables);
        
        // Check the registered loot tables against the registered block set for the mod
        this.checkExpectations();
    }

    private void writeTables(HashCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                DataProvider.save(GSON, cache, LootTables.serialize(lootTable), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }
    
    private void checkExpectations() {
        // Collect all the resource locations for the blocks defined in this mod
        Set<ResourceLocation> blocks = ForgeRegistries.BLOCKS.getKeys().stream().filter(loc -> loc.getNamespace().equals(PrimalMagic.MODID)).collect(Collectors.toSet());
        
        // Warn for each mod block that didn't have a loot table registered
        blocks.removeAll(this.registeredBlocks);
        blocks.forEach(key -> LOGGER.warn("Missing block loot table for {}", key.toString()));
    }
}
