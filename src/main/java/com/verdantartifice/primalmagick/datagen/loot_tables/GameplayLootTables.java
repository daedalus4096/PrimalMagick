package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Data provider for all of the mod's gameplay loot tables (e.g. treefolk bartering).
 * 
 * @author Daedalus4096
 */
public class GameplayLootTables implements LootTableSubProvider {
    private static final Logger LOGGER = LogManager.getLogger();

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

    @SuppressWarnings("unused")
    private void registerEmptyLootTable(ResourceLocation loc) {
        // Just mark that it's been registered without creating a table builder, to track expectations
        this.registeredLootTableTypes.add(loc);
    }
    
    private void registerLootTable(BiConsumer<ResourceLocation, LootTable.Builder> writer, ResourceLocation loc, LootTable.Builder builder) {
        writer.accept(loc, builder);
        this.registeredLootTableTypes.add(loc);
    }
    
    protected void addTables(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
        this.registerLootTable(writer, LootTablesPM.TREEFOLK_BARTERING, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootTableReference.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_FOOD).setWeight(30))
                .add(LootTableReference.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_SAPLINGS).setWeight(25))
                .add(LootTableReference.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_SEEDS).setWeight(25))
                .add(LootTableReference.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_JUNK).setWeight(15))
                .add(LootTableReference.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_TREASURE).setWeight(5))));
        this.registerLootTable(writer, LootTablesPM.TREEFOLK_BARTERING_FOOD, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.CARROT).setWeight(10))
                .add(LootItem.lootTableItem(Items.POTATO).setWeight(10))
                .add(LootItem.lootTableItem(Items.SWEET_BERRIES).setWeight(10))
                .add(LootItem.lootTableItem(Items.GLOW_BERRIES).setWeight(5))
                .add(LootItem.lootTableItem(Items.APPLE).setWeight(20))));
        this.registerLootTable(writer, LootTablesPM.TREEFOLK_BARTERING_SAPLINGS, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.OAK_SAPLING).setWeight(10))
                .add(LootItem.lootTableItem(Items.SPRUCE_SAPLING).setWeight(10))
                .add(LootItem.lootTableItem(Items.BIRCH_SAPLING).setWeight(10))
                .add(LootItem.lootTableItem(Items.JUNGLE_SAPLING).setWeight(10))
                .add(LootItem.lootTableItem(Items.ACACIA_SAPLING).setWeight(10))
                .add(LootItem.lootTableItem(Items.DARK_OAK_SAPLING).setWeight(10))
                .add(LootItem.lootTableItem(Items.MANGROVE_PROPAGULE).setWeight(10))
                .add(LootItem.lootTableItem(ItemsPM.SUNWOOD_SAPLING.get()).setWeight(5))
                .add(LootItem.lootTableItem(ItemsPM.MOONWOOD_SAPLING.get()).setWeight(5))
                .add(LootItem.lootTableItem(Items.BAMBOO).setWeight(10))));
        this.registerLootTable(writer, LootTablesPM.TREEFOLK_BARTERING_SEEDS, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(10))
                .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(10))
                .add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(10))
                .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10))
                .add(LootItem.lootTableItem(Items.COCOA_BEANS).setWeight(10))));
        this.registerLootTable(writer, LootTablesPM.TREEFOLK_BARTERING_JUNK, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.AZALEA).setWeight(10))
                .add(LootItem.lootTableItem(Items.FLOWERING_AZALEA).setWeight(10))
                .add(LootItem.lootTableItem(Items.LILY_PAD).setWeight(10))));
        this.registerLootTable(writer, LootTablesPM.TREEFOLK_BARTERING_TREASURE, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(10))
                .add(LootItem.lootTableItem(ItemsPM.FOUR_LEAF_CLOVER.get()).setWeight(10))
                .add(LootItem.lootTableItem(ItemsPM.TREEFOLK_SEED.get()).setWeight(20))
                .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(5))
                .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(new EnchantRandomlyFunction.Builder().withEnchantment(EnchantmentsPM.VERDANT.get())))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(EnchantmentsPM.BOUNTY.get())))));
        
        this.registerLootTable(writer, LootTablesPM.THEORYCRAFTING_TRADE, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).setWeight(7))
                .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(9))
                .add(LootItem.lootTableItem(Items.RABBIT_STEW).setWeight(64))
                .add(LootItem.lootTableItem(Items.COOKED_PORKCHOP).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F))))
                .add(LootItem.lootTableItem(Items.COOKED_CHICKEN).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(8.0F))))
                .add(LootItem.lootTableItem(Items.MAP).setWeight(9))
                .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))
                .add(LootItem.lootTableItem(Items.BREAD).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F))))
                .add(LootItem.lootTableItem(Items.APPLE).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))))
                .add(LootItem.lootTableItem(Items.COOKED_COD).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F))))
                .add(LootItem.lootTableItem(Items.ARROW).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(16.0F))))
                .add(LootItem.lootTableItem(Items.FLINT).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(10.0F))))
                .add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS).setWeight(21))
                .add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE).setWeight(9))
                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(1.0F, 15.0F))))
                .add(LootItem.lootTableItem(Items.BOOKSHELF).setWeight(7))
                .add(LootItem.lootTableItem(Items.BRICK).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(10.0F))))
                .add(LootItem.lootTableItem(Items.CHISELED_STONE_BRICKS).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))))
                .add(LootItem.lootTableItem(Items.SHEARS).setWeight(32))
                .add(LootItem.lootTableItem(Items.WHITE_WOOL).setWeight(64))
                .add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(64))
                .add(LootItem.lootTableItem(Items.STONE_SHOVEL).setWeight(64))
                .add(LootItem.lootTableItem(Items.STONE_PICKAXE).setWeight(64))
                .add(LootItem.lootTableItem(Items.STONE_SWORD).setWeight(64))));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(GameplayLootTables::new, LootContextParamSets.PIGLIN_BARTER);
    }
}
