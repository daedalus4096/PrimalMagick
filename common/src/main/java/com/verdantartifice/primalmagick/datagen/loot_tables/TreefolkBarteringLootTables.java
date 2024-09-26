package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.function.BiConsumer;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

/**
 * Data provider for all of the mod's treefolk bartering loot tables.
 * 
 * @author Daedalus4096
 */
public class TreefolkBarteringLootTables extends AbstractGameplayLootTableSubProvider {
    public TreefolkBarteringLootTables(HolderLookup.Provider registries) {
        super(registries, LootTablesPM::treefolkBartering);
    }
    
    @Override
    protected void addTables(BiConsumer<ResourceKey<LootTable>, Builder> writer) {
        HolderGetter<Enchantment> enchLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        this.registerLootTable(writer, LootTablesPM.TREEFOLK_BARTERING, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(NestedLootTable.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_FOOD).setWeight(30))
                .add(NestedLootTable.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_SAPLINGS).setWeight(25))
                .add(NestedLootTable.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_SEEDS).setWeight(25))
                .add(NestedLootTable.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_JUNK).setWeight(15))
                .add(NestedLootTable.lootTableReference(LootTablesPM.TREEFOLK_BARTERING_TREASURE).setWeight(5))));
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
                .add(LootItem.lootTableItem(ItemRegistration.SUNWOOD_SAPLING.get()).setWeight(5))
                .add(LootItem.lootTableItem(ItemRegistration.MOONWOOD_SAPLING.get()).setWeight(5))
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
                .add(LootItem.lootTableItem(ItemRegistration.FOUR_LEAF_CLOVER.get()).setWeight(10))
                .add(LootItem.lootTableItem(ItemRegistration.TREEFOLK_SEED.get()).setWeight(20))
                .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(5))
                .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(new EnchantRandomlyFunction.Builder().withEnchantment(enchLookup.getOrThrow(EnchantmentsPM.VERDANT))))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchLookup.getOrThrow(EnchantmentsPM.BOUNTY))))));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(TreefolkBarteringLootTables::new, LootContextParamSets.PIGLIN_BARTER);
    }
}
