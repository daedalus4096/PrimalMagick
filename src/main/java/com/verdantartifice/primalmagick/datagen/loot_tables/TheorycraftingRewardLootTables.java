package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.function.BiConsumer;

import com.verdantartifice.primalmagick.common.loot.LootTablesPM;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Data provider for all of the mod's theorycrafting reward loot tables.
 * 
 * @author Daedalus4096
 */
public class TheorycraftingRewardLootTables extends AbstractGameplayLootTableSubProvider {
    @Override
    protected void addTables(BiConsumer<ResourceLocation, Builder> writer) {
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
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(1.0F, 15.0F))))
                .add(LootItem.lootTableItem(Items.BOOKSHELF).setWeight(7))
                .add(LootItem.lootTableItem(Items.BRICK).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(10.0F))))
                .add(LootItem.lootTableItem(Items.CHISELED_STONE_BRICKS).setWeight(64).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))))
                .add(LootItem.lootTableItem(Items.SHEARS).setWeight(32))
                .add(LootItem.lootTableItem(Items.WHITE_WOOL).setWeight(64))
                .add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(64))
                .add(LootItem.lootTableItem(Items.STONE_SHOVEL).setWeight(64))
                .add(LootItem.lootTableItem(Items.STONE_PICKAXE).setWeight(64))
                .add(LootItem.lootTableItem(Items.STONE_SWORD).setWeight(64))));
        this.registerLootTable(writer, LootTablesPM.THEORYCRAFTING_PROSPEROUS_TRADE, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.BELL).setWeight(1))
                .add(LootItem.lootTableItem(Items.SHIELD).setWeight(7))
                .add(LootItem.lootTableItem(Items.COOKED_MUTTON).setWeight(36).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F))))
                .add(LootItem.lootTableItem(Items.COOKED_BEEF).setWeight(36).apply(SetItemCountFunction.setCount(ConstantValue.exactly(8.0F))))
                .add(LootItem.lootTableItem(Items.MAP).setWeight(3).apply(ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecoration.Type.RED_X).setZoom((byte)1).setSkipKnownStructures(true)).apply(SetNameFunction.setName(Component.translatable("filled_map.buried_treasure"))))
                .add(LootItem.lootTableItem(Items.MAP).setWeight(3).apply(ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_OCEAN_EXPLORER_MAPS).setMapDecoration(MapDecoration.Type.RED_X).setZoom((byte)1).setSkipKnownStructures(true)).apply(SetNameFunction.setName(Component.translatable("filled_map.monument"))))
                .add(LootItem.lootTableItem(Items.MAP).setWeight(3).apply(ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_WOODLAND_EXPLORER_MAPS).setMapDecoration(MapDecoration.Type.RED_X).setZoom((byte)1).setSkipKnownStructures(true)).apply(SetNameFunction.setName(Component.translatable("filled_map.mansion"))))
                .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(36))
                .add(LootItem.lootTableItem(Items.GLOWSTONE).setWeight(9))
                .add(LootItem.lootTableItem(Items.COOKIE).setWeight(12).apply(SetItemCountFunction.setCount(ConstantValue.exactly(18.0F))))
                .add(LootItem.lootTableItem(Items.CAKE).setWeight(36))
                .add(LootItem.lootTableItem(Items.COOKED_SALMON).setWeight(36).apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F))))
                .add(LootItem.lootTableItem(Items.BOW).setWeight(18))
                .add(LootItem.lootTableItem(Items.CROSSBOW).setWeight(12))
                .add(LootItem.lootTableItem(Items.LEATHER_HORSE_ARMOR).setWeight(6))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(16.0F, 30.0F))))
                .add(LootItem.lootTableItem(Items.LANTERN).setWeight(36))
                .add(LootItem.lootTableItem(Items.GLASS).setWeight(36).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))))
                .add(LootItem.lootTableItem(Items.DRIPSTONE_BLOCK).setWeight(36).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))))
                .add(LootItem.lootTableItem(Items.WHITE_BED).setWeight(12))
                .add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.IRON_SHOVEL).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))));
        this.registerLootTable(writer, LootTablesPM.THEORYCRAFTING_RICH_TRADE, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.SUSPICIOUS_STEW).setWeight(70).apply(SetStewEffectFunction.stewEffect().withEffect(MobEffects.NIGHT_VISION, UniformGenerator.between(7.0F, 10.0F)).withEffect(MobEffects.JUMP, UniformGenerator.between(7.0F, 10.0F)).withEffect(MobEffects.SATURATION, UniformGenerator.between(7.0F, 10.0F))))
                .add(LootItem.lootTableItem(Items.ITEM_FRAME).setWeight(10))
                .add(LootItem.lootTableItem(Items.GLOBE_BANNER_PATTERN).setWeight(9))
                .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(14))
                .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(23))
                .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(23).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))))
                .add(LootItem.lootTableItem(Items.GLISTERING_MELON_SLICE).setWeight(18).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))))
                .add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.BOW).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.CROSSBOW).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.SADDLE).setWeight(12))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(31.0F, 45.0F))))
                .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(4))
                .add(LootItem.lootTableItem(Items.QUARTZ_BLOCK).setWeight(70))
                .add(LootItem.lootTableItem(Items.PAINTING).setWeight(35).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))))
                .add(LootItem.lootTableItem(Items.DIAMOND_AXE).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.DIAMOND_SHOVEL).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))
                .add(LootItem.lootTableItem(Items.DIAMOND_SWORD).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(5.0F, 19.0F))))));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(TheorycraftingRewardLootTables::new, LootContextParamSets.ADVANCEMENT_REWARD);
    }
}
