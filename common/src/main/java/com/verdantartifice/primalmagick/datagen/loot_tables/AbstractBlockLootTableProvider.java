package com.verdantartifice.primalmagick.datagen.loot_tables;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingLogBlock;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Base class for the block loot table provider for the mod.  Handles the infrastructure so that the
 * subclass can just list registrations.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractBlockLootTableProvider extends BlockLootSubProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    
    protected final Set<ResourceLocation> registeredBlocks = new HashSet<>();
    
    public AbstractBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    /**
     * Add the mod's block loot tables to this provider's map for writing.
     */
    protected abstract void generate();
    
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> writer) {
        super.generate(writer);
        this.checkExpectations();
    }

    // This method overrides the getKnownBlocks base method added in patches by loader-specific versions of
    // BlockLootSubProvider. It *should* have the override annotation, but doesn't because those base methods aren't
    // present in the vanilla version of the class. This should still get called correctly via polymorphism by the
    // loader-specific version of the base class.
    protected Iterable<Block> getKnownBlocks() {
        // Limit this data provider to blocks added by the mod
        return Services.BLOCKS_REGISTRY.getEntries().stream().filter(entry -> entry.getKey().location().getNamespace().equals(Constants.MOD_ID)).map(Map.Entry::getValue).toList();
    }

    private void registerLootTableBuilder(Block block, LootTable.Builder builder) {
        this.registeredBlocks.add(Services.BLOCKS_REGISTRY.getKey(block));
        this.add(block, builder);
    }
    
    protected void registerLootTableBuilder(Block block, Function<Block, LootTable.Builder> builderGenerator) {
        this.registerLootTableBuilder(block, builderGenerator.apply(block));
    }
    
    protected void registerEmptyTable(Block block) {
        // Just mark that it's been registered without creating a table builder, to track expectations
        this.registeredBlocks.add(Services.BLOCKS_REGISTRY.getKey(block));
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
        Holder<Enchantment> fortuneHolder = this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        float[] stickFortuneChances = new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F };
        LootItemCondition.Builder shearsOrSilkTouch = Services.ITEM_ABILITIES.makeShearsDigLootCondition().or(hasSilkTouch());
        LootPoolEntryContainer.Builder<?> saplingEntryBuilder = LootItem.lootTableItem(saplingBlock).when(ExplosionCondition.survivesExplosion()).when(BonusLevelTableCondition.bonusLevelFlatChance(fortuneHolder, saplingFortuneChances));
        LootPoolEntryContainer.Builder<?> leavesEntryBuilder = LootItem.lootTableItem(leavesBlock).when(shearsOrSilkTouch);
        LootPool.Builder saplingAndLeavesPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(leavesEntryBuilder.otherwise(saplingEntryBuilder));
        LootPoolEntryContainer.Builder<?> stickEntryBuilder = LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).apply(ApplyExplosionDecay.explosionDecay()).when(BonusLevelTableCondition.bonusLevelFlatChance(fortuneHolder, stickFortuneChances));
        LootPool.Builder stickPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(shearsOrSilkTouch.invert()).add(stickEntryBuilder);
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(saplingAndLeavesPool).withPool(stickPool);
        this.registerLootTableBuilder(leavesBlock, tableBuilder);
    }
    
    protected void registerInfusedStoneTable(Block stoneBlock, Item dustItem) {
        LootPoolEntryContainer.Builder<?> dustEntryBuilder = LootItem.lootTableItem(dustItem).when(ExplosionCondition.survivesExplosion());
        LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(stoneBlock).when(hasSilkTouch()).otherwise(dustEntryBuilder));
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(poolBuilder);
        this.registerLootTableBuilder(stoneBlock, tableBuilder);
    }
    
    protected void registerGemOreTable(Block oreBlock, Item gemItem) {
        Holder<Enchantment> fortuneHolder = this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        LootPoolEntryContainer.Builder<?> gemEntryBuilder = LootItem.lootTableItem(gemItem).apply(ApplyBonusCount.addOreBonusCount(fortuneHolder)).apply(ApplyExplosionDecay.explosionDecay());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(oreBlock).when(hasSilkTouch()).otherwise(gemEntryBuilder)));
        this.registerLootTableBuilder(oreBlock, tableBuilder);
    }
    
    protected void registerMultiGemOreTable(Block oreBlock, Item gemItem, float minGems, float maxGems) {
        Holder<Enchantment> fortuneHolder = this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        LootPoolEntryContainer.Builder<?> gemEntryBuilder = LootItem.lootTableItem(gemItem).apply(SetItemCountFunction.setCount(UniformGenerator.between(minGems, maxGems))).apply(ApplyBonusCount.addUniformBonusCount(fortuneHolder)).apply(ApplyExplosionDecay.explosionDecay());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(oreBlock).when(hasSilkTouch()).otherwise(gemEntryBuilder)));
        this.registerLootTableBuilder(oreBlock, tableBuilder);
    }
    
    protected void registerManaBearingDeviceTable(Block block) {
        LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())))
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
        Holder<Enchantment> fortuneHolder = this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        var builder = LootItem.lootTableItem(splitItem).apply(SetItemCountFunction.setCount(splitCount));
        if (maxWithFortune.isPresent()) {
            builder = builder.apply(ApplyBonusCount.addUniformBonusCount(fortuneHolder)).apply(LimitCount.limitCount(IntRange.upperBound(maxWithFortune.getAsInt())));
        }
        builder = builder.apply(ApplyExplosionDecay.explosionDecay());
        LootTable.Builder tableBuilder = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).when(hasSilkTouch()).otherwise(builder)));
        this.registerLootTableBuilder(block, tableBuilder);
    }

    protected void registerPottedPlant(FlowerPotBlock block) {
        this.registeredBlocks.add(Services.BLOCKS_REGISTRY.getKey(block));
        this.dropPottedContents(block);
    }
    
    private void checkExpectations() {
        // Collect all the resource locations for the blocks defined in this mod
        Set<ResourceLocation> blocks = Services.BLOCKS_REGISTRY.getAllKeys().stream().filter(loc -> loc.getNamespace().equals(Constants.MOD_ID)).collect(Collectors.toSet());
        
        // Warn for each mod block that didn't have a loot table registered
        blocks.removeAll(this.registeredBlocks);
        blocks.forEach(key -> LOGGER.warn("Missing block loot table for {}", key.toString()));
    }
}
