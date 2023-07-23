package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Data provider for all of the mod's entity loot tables.
 * 
 * @author Daedalus4096
 */
public class EntityLootTables extends EntityLootSubProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    protected final Set<ResourceLocation> registeredEntities = new HashSet<>();
    
    public EntityLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    private void checkExpectations() {
        // Collect all the resource locations for the blocks defined in this mod
        Set<ResourceLocation> entityTypes = ForgeRegistries.ENTITY_TYPES.getKeys().stream().filter(loc -> loc.getNamespace().equals(PrimalMagick.MODID)).collect(Collectors.toSet());
        
        // Warn for each mod entity that didn't have a loot table registered
        entityTypes.removeAll(this.registeredEntities);
        entityTypes.forEach(key -> LOGGER.warn("Missing entity loot table for {}", key.toString()));
    }

    private void registerEmptyLootTable(EntityType<?> type) {
        // Just mark that it's been registered without creating a table builder, to track expectations
        this.registeredEntities.add(ForgeRegistries.ENTITY_TYPES.getKey(type));
    }
    
    private void registerLootTable(EntityType<?> type, LootTable.Builder builder) {
        this.add(type, builder);
        this.registeredEntities.add(ForgeRegistries.ENTITY_TYPES.getKey(type));
    }
    
    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        // Limit this data provider to entity types added by the mod
        return ForgeRegistries.ENTITY_TYPES.getEntries().stream().filter(entry -> entry.getKey().location().getNamespace().equals(PrimalMagick.MODID)).map(entry -> entry.getValue());
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
        super.generate(writer);
        this.checkExpectations();
    }

    @Override
    public void generate() {
        this.registerEmptyLootTable(EntityTypesPM.SPELL_MINE.get());
        this.registerEmptyLootTable(EntityTypesPM.SPELL_PROJECTILE.get());
        this.registerEmptyLootTable(EntityTypesPM.APPLE.get());
        this.registerEmptyLootTable(EntityTypesPM.IGNYX.get());
        this.registerEmptyLootTable(EntityTypesPM.ALCHEMICAL_BOMB.get());
        this.registerEmptyLootTable(EntityTypesPM.MANA_ARROW.get());
        this.registerEmptyLootTable(EntityTypesPM.FISHING_HOOK.get());
        this.registerEmptyLootTable(EntityTypesPM.PRIMALITE_TRIDENT.get());
        this.registerEmptyLootTable(EntityTypesPM.HEXIUM_TRIDENT.get());
        this.registerEmptyLootTable(EntityTypesPM.HALLOWSTEEL_TRIDENT.get());
        this.registerEmptyLootTable(EntityTypesPM.FORBIDDEN_TRIDENT.get());
        this.registerEmptyLootTable(EntityTypesPM.SIN_CRASH.get());
        this.registerEmptyLootTable(EntityTypesPM.SIN_CRYSTAL.get());
        this.registerEmptyLootTable(EntityTypesPM.FLYING_CARPET.get());
        this.registerLootTable(EntityTypesPM.TREEFOLK.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.HEARTWOOD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
        this.registerLootTable(EntityTypesPM.INNER_DEMON.get(), LootTable.lootTable()); // Loot dropped by Inner Demons is special, so use an empty table
        this.registerLootTable(EntityTypesPM.FRIENDLY_WITCH.get(), LootTable.lootTable());  // No loot dropped by Friendly Witches, so use an empty table
        this.registerLootTable(EntityTypesPM.PRIMALITE_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.PRIMALITE_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))));
        this.registerLootTable(EntityTypesPM.HEXIUM_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.HEXIUM_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))));
        this.registerLootTable(EntityTypesPM.HALLOWSTEEL_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.HALLOWSTEEL_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))));
        this.registerLootTable(EntityTypesPM.BASIC_EARTH_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_BASIC_EARTH_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.GRAND_EARTH_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_GRAND_EARTH_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_MAJESTIC_EARTH_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.BASIC_SEA_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_BASIC_SEA_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.GRAND_SEA_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_GRAND_SEA_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_MAJESTIC_SEA_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.BASIC_SKY_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_BASIC_SKY_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.GRAND_SKY_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_GRAND_SKY_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_MAJESTIC_SKY_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.BASIC_SUN_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_BASIC_SUN_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.GRAND_SUN_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_GRAND_SUN_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_MAJESTIC_SUN_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.BASIC_MOON_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_BASIC_MOON_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.GRAND_MOON_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_GRAND_MOON_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_MAJESTIC_MOON_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.BASIC_BLOOD_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_BASIC_BLOOD_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.GRAND_BLOOD_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_GRAND_BLOOD_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_MAJESTIC_BLOOD_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_BASIC_INFERNAL_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_GRAND_INFERNAL_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.MAJESTIC_INFERNAL_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_MAJESTIC_INFERNAL_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.BASIC_VOID_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_BASIC_VOID_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.GRAND_VOID_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_GRAND_VOID_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_MAJESTIC_VOID_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_BASIC_HALLOWED_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_GRAND_HALLOWED_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        this.registerLootTable(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ItemsPM.DRAINED_MAJESTIC_HALLOWED_PIXIE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(EntityLootTables::new, LootContextParamSets.ENTITY);
    }
}
