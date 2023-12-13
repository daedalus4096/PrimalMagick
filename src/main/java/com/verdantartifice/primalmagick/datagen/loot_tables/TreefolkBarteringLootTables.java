package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.function.BiConsumer;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

/**
 * Data provider for all of the mod's treefolk bartering loot tables.
 * 
 * @author Daedalus4096
 */
public class TreefolkBarteringLootTables extends AbstractGameplayLootTableSubProvider {
    @Override
    protected void addTables(BiConsumer<ResourceLocation, Builder> writer) {
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
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(TreefolkBarteringLootTables::new, LootContextParamSets.PIGLIN_BARTER);
    }
}
