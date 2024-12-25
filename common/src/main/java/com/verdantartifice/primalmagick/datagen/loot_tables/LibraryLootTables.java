package com.verdantartifice.primalmagick.datagen.loot_tables;

import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Data provider for all of the mod's ancient library loot tables.
 * 
 * @author Daedalus4096
 */
public class LibraryLootTables extends AbstractGameplayLootTableSubProvider {
    public LibraryLootTables(HolderLookup.Provider registries) {
        super(registries, LootTablesPM::library);
    }
    
    @Override
    protected void addTables(BiConsumer<ResourceKey<LootTable>, Builder> writer) {
        // Register top level loot tables
        this.registerLootTable(writer, LootTablesPM.LIBRARY_EARTH, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_EARTH).setWeight(40))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SKY).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SUN).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(40))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_SEA, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SEA).setWeight(40))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SKY).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_MOON).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(40))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_SKY, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SKY).setWeight(40))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_EARTH).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SEA).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(40))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_SUN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SUN).setWeight(40))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_MOON).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_EARTH).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(40))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_MOON, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_MOON).setWeight(40))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SUN).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SEA).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(40))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_FORBIDDEN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(5))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(40))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(20))
                .add(EmptyLootItem.emptyItem().setWeight(20))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_WELCOME, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1F))
                .add(autoTranslatingBook(this.registries, BooksPM.WELCOME))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_WARNING, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1F))
                .add(autoTranslatingBook(this.registries, BooksPM.WARNING))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_HIDDEN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(5F, 10F))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(5))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(40))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(20))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_ARCHAEOLOGY, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1F))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_EPIC_EARTH, BookLanguagesPM.EARTH, 1))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_EPIC_SEA, BookLanguagesPM.SEA, 1))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_EPIC_SKY, BookLanguagesPM.SKY, 1))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_EPIC_SUN, BookLanguagesPM.SUN, 1))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_EPIC_MOON, BookLanguagesPM.MOON, 1))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_EPIC_TRADE, BookLanguagesPM.TRADE, 1))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_EPIC_FORBIDDEN, BookLanguagesPM.FORBIDDEN, 1))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_EPIC_HALLOWED, BookLanguagesPM.HALLOWED, 1))));
        
        // Register culture component loot tables
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_EARTH, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.EARTH, 12))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.EARTH, 6))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.EARTH, 2))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_SEA, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.SEA, 12))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.SEA, 6))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.SEA, 2))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_SKY, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.SKY, 12))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.SKY, 6))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.SKY, 2))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_SUN, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.SUN, 12))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.SUN, 6))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.SUN, 2))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_MOON, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.MOON, 12))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.MOON, 6))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.MOON, 2))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_TRADE, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.TRADE, 12))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.TRADE, 6))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.TRADE, 2))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_FORBIDDEN, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.FORBIDDEN, 6))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.FORBIDDEN, 10))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.FORBIDDEN, 4))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_HALLOWED, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.HALLOWED, 4))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.HALLOWED, 6))
                .add(catalog(this.registries, LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.HALLOWED, 10))
                .add(NestedLootTable.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));

        // Register common catalog loot table
        LootPool.Builder commonPool = LootPool.lootPool()
                .add(book(this.registries, BooksPM.FIVE_CULTURES_PRELUDE, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_INTRODUCTION, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_EARTH_PART_1, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_EARTH_INNOVATIONS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_EARTH_RELATIONS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SEA_PART_1, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SEA_PART_2, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SEA_RELATIONS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SKY_PART_1, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SKY_PART_2, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SKY_RELATIONS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SUN_PART_2, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SUN_INNOVATIONS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SUN_RELATIONS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_MOON_PART_1, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_MOON_PART_2, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_MOON_INNOVATIONS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.TRAVEL_DIARY_SEVEN_PILLARS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.HELP, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.JOURNEYS_ARCH, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.TOURNAMENT_FLYER, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.AVAST, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.RECIPES_SEAWEED, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.TRAVEL_DIARY_YERRAN, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.JOURNEYS_CURRENT, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.TRAVEL_DIARY_FLYING, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.OUR_DIPLOMATIC_MISSION, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.JOURNEYS_MOTION, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.CALL_TO_ARMS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.TRAVEL_DIARY_ELDE, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.RECIPES_SUN_TEA, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.JOURNEYS_WINDOWS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.TRAVEL_DIARY_CUSP, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.RECIPES_MUSHROOMS, Rarity.COMMON, 1))
                .add(book(this.registries, BooksPM.JOURNEYS_SHAPES, Rarity.COMMON, 1))
                ;
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_COMMON, LootTable.lootTable().withPool(commonPool));
        
        // Generate uncommon catalog loot table
        LootPool.Builder uncommonPool = LootPool.lootPool()
                .add(book(this.registries, BooksPM.SOURCE_PRIMER, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_EARTH_PART_2, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SEA_INNOVATIONS, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SKY_INNOVATIONS, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_SUN_PART_1, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_MOON_RELATIONS, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_FORBIDDEN_MAGICK, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_FORBIDDEN_INNOVATIONS, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.TRAVEL_DIARY_BEYOND, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.MAGICKAL_TOME_THREE_STONES, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.MAGICKAL_TOME_ON_THE_BEACH, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.MAGICKAL_TOME_LIGHTNING_IN_MY_GRASP, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.MAGICKAL_TOME_NEAR_DEATH_EXPERIENCE, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.MAGICKAL_TOME_IN_DREAMS, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.MAGICKAL_TOME_SOMETHING_MOMENTOUS, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.DELAY, Rarity.UNCOMMON, 1))
                .add(book(this.registries, BooksPM.JOURNEYS_EDITORS_NOTE, Rarity.UNCOMMON, 1))
                ;
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, LootTable.lootTable().withPool(uncommonPool));
        
        // Generate rare catalog loot table
        LootPool.Builder rarePool = LootPool.lootPool()
                .add(book(this.registries, BooksPM.FIVE_CULTURES_HEAVENLY_MAGICK, Rarity.RARE, 1))
                .add(book(this.registries, BooksPM.FIVE_CULTURES_HEAVENLY_INNOVATIONS, Rarity.RARE, 1))
                .add(book(this.registries, BooksPM.A_CURIOUS_EXCHANGE, Rarity.RARE, 1))
                .add(book(this.registries, BooksPM.MAGICKAL_TOME_TRANSGRESSIONS, Rarity.RARE, 1))
                .add(book(this.registries, BooksPM.SHELTER_FROM_THE_STORMS, Rarity.RARE, 1))
                .add(book(this.registries, BooksPM.JOURNEYS_ANEW, Rarity.RARE, 1))
                ;
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_RARE, LootTable.lootTable().withPool(rarePool));
        
        // Generate epic catalog loot tables for unique archaeology tablets
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_EPIC_EARTH, LootTable.lootTable().withPool(LootPool.lootPool().add(book(this.registries, BooksPM.BESIEGED, Rarity.EPIC, 1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_EPIC_SEA, LootTable.lootTable().withPool(LootPool.lootPool().add(book(this.registries, BooksPM.CAPTAINS_LOG, Rarity.EPIC, 1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_EPIC_SKY, LootTable.lootTable().withPool(LootPool.lootPool().add(book(this.registries, BooksPM.RUNNING, Rarity.EPIC, 1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_EPIC_SUN, LootTable.lootTable().withPool(LootPool.lootPool().add(book(this.registries, BooksPM.IN_GLORIOUS_MEMORY, Rarity.EPIC, 1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_EPIC_MOON, LootTable.lootTable().withPool(LootPool.lootPool().add(book(this.registries, BooksPM.EVACUATION, Rarity.EPIC, 1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_EPIC_TRADE, LootTable.lootTable().withPool(LootPool.lootPool().add(book(this.registries, BooksPM.END_OF_ALL_THINGS, Rarity.EPIC, 1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_EPIC_FORBIDDEN, LootTable.lootTable().withPool(LootPool.lootPool().add(book(this.registries, BooksPM.VICTORY, Rarity.EPIC, 1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_EPIC_HALLOWED, LootTable.lootTable().withPool(LootPool.lootPool().add(book(this.registries, BooksPM.OUR_FAILURE, Rarity.EPIC, 1))));

        // Generate treasure catalog loot table
        // FIXME Figure out why serialization can't find the mod's "enchantable" item tags
        LootPool.Builder enchPool = LootPool.lootPool();
//        var enchLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
//        enchLookup.listElements().filter(enchRef -> enchRef.key().location().getNamespace().equals(Constants.MOD_ID)).forEach(enchRef -> {
//            enchPool.add(enchantedBook(enchRef));
//        });
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_TREASURE, LootTable.lootTable().withPool(enchPool));
    }
    
    protected static LootPoolEntryContainer.Builder<?> catalog(HolderLookup.Provider registries, ResourceKey<LootTable> lootTable, ResourceKey<BookLanguage> languageKey, int weight) {
        Holder<BookLanguage> langHolder = registries.lookupOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getOrThrow(languageKey);
        return NestedLootTable.lootTableReference(lootTable).setWeight(weight).apply(SetComponentsFunction.setComponent(DataComponentsPM.BOOK_LANGUAGE.get(), langHolder));
    }
    
    protected static LootPoolEntryContainer.Builder<?> book(HolderLookup.Provider registries, ResourceKey<BookDefinition> bookDefKey, Rarity rarity, int weight) {
        Supplier<StaticBookItem> itemSupplier = switch (rarity) {
            case UNCOMMON -> ItemRegistration.STATIC_BOOK_UNCOMMON;
            case RARE -> ItemRegistration.STATIC_BOOK_RARE;
            case EPIC -> ItemRegistration.STATIC_TABLET;
            default -> ItemRegistration.STATIC_BOOK;
        };
        Holder<BookDefinition> bookHolder = registries.lookupOrThrow(RegistryKeysPM.BOOKS).getOrThrow(bookDefKey);
        return LootItem.lootTableItem(itemSupplier.get()).setWeight(weight).apply(SetComponentsFunction.setComponent(DataComponentsPM.BOOK_DEFINITION.get(), bookHolder));
    }
    
    protected static LootPoolEntryContainer.Builder<?> enchantedBook(Holder<Enchantment> ench) {
        return LootItem.lootTableItem(Items.BOOK).setWeight(ench.value().definition().weight()).apply(new EnchantRandomlyFunction.Builder().withEnchantment(ench));
    }
    
    protected static LootPoolEntryContainer.Builder<?> autoTranslatingBook(HolderLookup.Provider registries, ResourceKey<BookDefinition> bookKey) {
        Holder<BookDefinition> bookHolder = registries.lookupOrThrow(RegistryKeysPM.BOOKS).getOrThrow(bookKey);
        Holder<BookLanguage> langHolder = registries.lookupOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getOrThrow(BookLanguagesPM.BABELTONGUE);
        return LootItem.lootTableItem(ItemsPM.STATIC_BOOK.get()).setWeight(1).apply(SetComponentsFunction.setComponent(DataComponentsPM.BOOK_LANGUAGE.get(), langHolder))
                .apply(SetComponentsFunction.setComponent(DataComponentsPM.BOOK_DEFINITION.get(), bookHolder));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(LibraryLootTables::new, LootContextParamSets.CHEST);
    }
}
