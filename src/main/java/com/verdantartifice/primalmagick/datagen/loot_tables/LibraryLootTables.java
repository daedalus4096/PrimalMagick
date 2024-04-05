package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.function.BiConsumer;

import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;

import net.minecraft.Util;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Data provider for all of the mod's ancient library loot tables.
 * 
 * @author Daedalus4096
 */
public class LibraryLootTables extends AbstractGameplayLootTableSubProvider {
    @Override
    protected void addTables(BiConsumer<ResourceLocation, Builder> writer) {
        // Register top level loot tables
        this.registerLootTable(writer, LootTablesPM.LIBRARY_EARTH, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_EARTH).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SKY).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SUN).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(20))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_SEA, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SEA).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SKY).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_MOON).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(20))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_SKY, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SKY).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_EARTH).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SEA).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(20))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_SUN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SUN).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_MOON).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_EARTH).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(20))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_MOON, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_MOON).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SUN).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_SEA).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(20))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(10))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(5))
                .add(EmptyLootItem.emptyItem().setWeight(20))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_WELCOME, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1F))
                .add(welcomeBook())));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_HIDDEN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(5F, 10F))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(5))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(20))));

        // Register culture component loot tables
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_EARTH, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.EARTH.get(), 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.EARTH.get(), 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.EARTH.get(), 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_SEA, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.SEA.get(), 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.SEA.get(), 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.SEA.get(), 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_SKY, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.SKY.get(), 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.SKY.get(), 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.SKY.get(), 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_SUN, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.SUN.get(), 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.SUN.get(), 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.SUN.get(), 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_MOON, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.MOON.get(), 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.MOON.get(), 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.MOON.get(), 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_TRADE, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.TRADE.get(), 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.TRADE.get(), 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.TRADE.get(), 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_FORBIDDEN, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.FORBIDDEN.get(), 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.FORBIDDEN.get(), 10))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.FORBIDDEN.get(), 4))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_HALLOWED, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.HALLOWED.get(), 4))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.HALLOWED.get(), 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.HALLOWED.get(), 10))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));

        // Register catalog component loot tables
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_COMMON, LootTable.lootTable().withPool(LootPool.lootPool()
                // TODO Populate table with real books
                .add(book(BooksPM.SOURCE_PRIMER, 1))
                ));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, LootTable.lootTable().withPool(LootPool.lootPool()
                // TODO Populate table with real books
                .add(book(BooksPM.DREAM_JOURNAL, 1))
                ));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_RARE, LootTable.lootTable().withPool(LootPool.lootPool()
                // TODO Populate table with real books
                .add(book(BooksPM.TEST_BOOK, 1))
                ));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_TREASURE, LootTable.lootTable().withPool(LootPool.lootPool()
                // TODO Populate table with enchanted books
                .add(enchantedBook(EnchantmentsPM.VERDANT.get(), 1))
                ));
    }
    
    @SuppressWarnings("deprecation")
    protected static LootPoolEntryContainer.Builder<?> catalog(ResourceLocation lootTable, BookLanguage language, int weight) {
        CompoundTag tag = Util.make(new CompoundTag(), t -> t.putString(StaticBookItem.TAG_BOOK_LANGUAGE_ID, BookLanguagesPM.LANGUAGES.get().getKey(language).toString()));
        return LootTableReference.lootTableReference(lootTable).setWeight(weight).apply(SetNbtFunction.setTag(tag));
    }
    
    @SuppressWarnings("deprecation")
    protected static LootPoolEntryContainer.Builder<?> book(ResourceKey<BookDefinition> bookDefKey, int weight) {
        CompoundTag tag = Util.make(new CompoundTag(), t -> t.putString(StaticBookItem.TAG_BOOK_ID, bookDefKey.location().toString()));
        return LootItem.lootTableItem(ItemsPM.STATIC_BOOK.get()).setWeight(weight).apply(SetNbtFunction.setTag(tag));
    }
    
    protected static LootPoolEntryContainer.Builder<?> enchantedBook(Enchantment ench, int weight) {
        return LootItem.lootTableItem(Items.BOOK).setWeight(weight).apply(new EnchantRandomlyFunction.Builder().withEnchantment(ench));
    }
    
    @SuppressWarnings("deprecation")
    protected static LootPoolEntryContainer.Builder<?> welcomeBook() {
        CompoundTag tag = Util.make(new CompoundTag(), t -> {
            t.putString(StaticBookItem.TAG_BOOK_ID, BooksPM.WELCOME.location().toString());
            t.putString(StaticBookItem.TAG_BOOK_LANGUAGE_ID, BookLanguagesPM.BABELTONGUE.getId().toString());
        });
        return LootItem.lootTableItem(ItemsPM.STATIC_BOOK.get()).setWeight(1).apply(SetNbtFunction.setTag(tag));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(LibraryLootTables::new, LootContextParamSets.CHEST);
    }
}
