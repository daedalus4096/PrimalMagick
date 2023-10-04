package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.Collections;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingLogBlock;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.loot.CanToolPerformAction;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Base class for the block loot table provider for the mod.  Handles the infrastructure so that the
 * subclass can just list registrations.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractBlockLootTableProvider extends BlockLootSubProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    
    protected final Set<ResourceLocation> registeredBlocks = new HashSet<>();
    
    public AbstractBlockLootTableProvider() {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
    }

    /**
     * Add the mod's block loot tables to this provider's map for writing.
     */
    protected abstract void generate();
    
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
        super.generate(writer);
        this.checkExpectations();
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        // Limit this data provider to blocks added by the mod
        return ForgeRegistries.BLOCKS.getEntries().stream().filter(entry -> entry.getKey().location().getNamespace().equals(PrimalMagick.MODID)).map(entry -> entry.getValue()).toList();
    }

    private void registerLootTableBuilder(Block block, LootTable.Builder builder) {
        this.registeredBlocks.add(ForgeRegistries.BLOCKS.getKey(block));
        this.add(block, builder);
    }
    
    protected void registerLootTableBuilder(Block block, Function<Block, LootTable.Builder> builderGenerator) {
        this.registerLootTableBuilder(block, builderGenerator.apply(block));
    }
    
    protected void registerEmptyTable(Block block) {
        // Just mark that it's been registered without creating a table builder, to track expectations
        this.registeredBlocks.add(ForgeRegistries.BLOCKS.getKey(block));
    }
    
    protected void registerBasicTable(Block block) {
        LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block))
                .when(ExplosionCondition.survivesExplosion());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(poolBuilder);
        this.registerLootTableBuilder(block, tableBuilder);
    }
    
    protected void registerSlabTable(Block block) {
        LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                ).apply(ApplyExplosionDecay.explosionDecay()));
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(poolBuilder);
        this.registerLootTableBuilder(block, tableBuilder);
    }
    
    protected void registerLeavesTable(Block leavesBlock, Block saplingBlock, float[] saplingFortuneChances) {
        float[] stickFortuneChances = new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F };
        LootItemCondition.Builder shearsOrSilkTouch = CanToolPerformAction.canToolPerformAction(ToolActions.SHEARS_DIG).or(HAS_SILK_TOUCH);
        LootPoolEntryContainer.Builder<?> saplingEntryBuilder = LootItem.lootTableItem(saplingBlock).when(ExplosionCondition.survivesExplosion()).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, saplingFortuneChances));
        LootPoolEntryContainer.Builder<?> leavesEntryBuilder = LootItem.lootTableItem(leavesBlock).when(shearsOrSilkTouch);
        LootPool.Builder saplingAndLeavesPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(leavesEntryBuilder.otherwise(saplingEntryBuilder));
        LootPoolEntryContainer.Builder<?> stickEntryBuilder = LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).apply(ApplyExplosionDecay.explosionDecay()).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, stickFortuneChances));
        LootPool.Builder stickPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(shearsOrSilkTouch.invert()).add(stickEntryBuilder);
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(saplingAndLeavesPool).withPool(stickPool);
        this.registerLootTableBuilder(leavesBlock, tableBuilder);
    }
    
    protected void registerInfusedStoneTable(Block stoneBlock, Item dustItem) {
        LootPoolEntryContainer.Builder<?> dustEntryBuilder = LootItem.lootTableItem(dustItem).when(ExplosionCondition.survivesExplosion());
        LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(stoneBlock).when(HAS_SILK_TOUCH).otherwise(dustEntryBuilder));
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(poolBuilder);
        this.registerLootTableBuilder(stoneBlock, tableBuilder);
    }
    
    protected void registerGemOreTable(Block oreBlock, Item gemItem) {
        LootPoolEntryContainer.Builder<?> gemEntryBuilder = LootItem.lootTableItem(gemItem).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).apply(ApplyExplosionDecay.explosionDecay());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(oreBlock).when(HAS_SILK_TOUCH).otherwise(gemEntryBuilder)));
        this.registerLootTableBuilder(oreBlock, tableBuilder);
    }
    
    protected void registerMultiGemOreTable(Block oreBlock, Item gemItem, float minGems, float maxGems) {
        LootPoolEntryContainer.Builder<?> gemEntryBuilder = LootItem.lootTableItem(gemItem).apply(SetItemCountFunction.setCount(UniformGenerator.between(minGems, maxGems))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)).apply(ApplyExplosionDecay.explosionDecay());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(oreBlock).when(HAS_SILK_TOUCH).otherwise(gemEntryBuilder)));
        this.registerLootTableBuilder(oreBlock, tableBuilder);
    }
    
    protected void registerManaBearingDeviceTable(Block block) {
        LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("ManaStorage.Mana.Sources", "ManaContainerTag.Sources")))
                .when(ExplosionCondition.survivesExplosion());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(poolBuilder);
        this.registerLootTableBuilder(block, tableBuilder);
    }
    
    protected void registerPulsingLogTable(Block block) {
        LootPool.Builder logBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block)).when(ExplosionCondition.survivesExplosion());
        LootPool.Builder pulseBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ItemsPM.HEARTWOOD.get()))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractPhasingLogBlock.PULSING, true)));
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(logBuilder).withPool(pulseBuilder);
        this.registerLootTableBuilder(block, tableBuilder);
    }
    
    protected void registerSplittingTable(Block block, Item splitItem, NumberProvider splitCount, OptionalInt maxWithFortune) {
        var builder = LootItem.lootTableItem(splitItem).apply(SetItemCountFunction.setCount(splitCount));
        if (maxWithFortune.isPresent()) {
            builder = builder.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)).apply(LimitCount.limitCount(IntRange.upperBound(maxWithFortune.getAsInt())));
        }
        builder = builder.apply(ApplyExplosionDecay.explosionDecay());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).when(HAS_SILK_TOUCH).otherwise(builder)));
        this.registerLootTableBuilder(block, tableBuilder);
    }
    
    private void checkExpectations() {
        // Collect all the resource locations for the blocks defined in this mod
        Set<ResourceLocation> blocks = ForgeRegistries.BLOCKS.getKeys().stream().filter(loc -> loc.getNamespace().equals(PrimalMagick.MODID)).collect(Collectors.toSet());
        
        // Warn for each mod block that didn't have a loot table registered
        blocks.removeAll(this.registeredBlocks);
        blocks.forEach(key -> LOGGER.warn("Missing block loot table for {}", key.toString()));
    }
}
