package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;

import net.minecraft.Util;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
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
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Data provider for all of the mod's ancient library loot tables.
 * 
 * @author Daedalus4096
 */
public class LibraryLootTables extends AbstractGameplayLootTableSubProvider {
    public LibraryLootTables() {
        super(LootTablesPM::library);
    }
    
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
        this.registerLootTable(writer, LootTablesPM.LIBRARY_FORBIDDEN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(5))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(20))
                .add(EmptyLootItem.emptyItem().setWeight(10))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_WELCOME, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1F))
                .add(autoTranslatingBook(BooksPM.WELCOME))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_WARNING, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1F))
                .add(autoTranslatingBook(BooksPM.WARNING))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_HIDDEN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(5F, 10F))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_TRADE).setWeight(5))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_FORBIDDEN).setWeight(40))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CULTURE_HALLOWED).setWeight(20))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_ARCHAEOLOGY, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1F))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_EPIC, BookLanguagesPM.EARTH, 1))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_EPIC, BookLanguagesPM.SEA, 1))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_EPIC, BookLanguagesPM.SKY, 1))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_EPIC, BookLanguagesPM.SUN, 1))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_EPIC, BookLanguagesPM.MOON, 1))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_EPIC, BookLanguagesPM.TRADE, 1))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_EPIC, BookLanguagesPM.FORBIDDEN, 1))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_EPIC, BookLanguagesPM.HALLOWED, 1))));
        
        // Register culture component loot tables
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_EARTH, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.EARTH, 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.EARTH, 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.EARTH, 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_SEA, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.SEA, 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.SEA, 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.SEA, 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_SKY, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.SKY, 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.SKY, 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.SKY, 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_SUN, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.SUN, 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.SUN, 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.SUN, 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_MOON, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.MOON, 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.MOON, 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.MOON, 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_TRADE, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.TRADE, 12))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.TRADE, 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.TRADE, 2))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_FORBIDDEN, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.FORBIDDEN, 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.FORBIDDEN, 10))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.FORBIDDEN, 4))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CULTURE_HALLOWED, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_COMMON, BookLanguagesPM.HALLOWED, 4))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_UNCOMMON, BookLanguagesPM.HALLOWED, 6))
                .add(catalog(LootTablesPM.LIBRARY_CATALOG_RARE, BookLanguagesPM.HALLOWED, 10))
                .add(LootTableReference.lootTableReference(LootTablesPM.LIBRARY_CATALOG_TREASURE).setWeight(1))));

        // Register catalog component loot tables
        LootPool.Builder commonPool = LootPool.lootPool().add(book(BooksPM.SOURCE_PRIMER, Rarity.COMMON, 1));
        IntStream.rangeClosed(21, 50).forEach(index -> commonPool.add(book(BooksPM.create("loremipsum" + index), Rarity.COMMON, 1)));  // FIXME Remove once library testing is complete
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_COMMON, LootTable.lootTable().withPool(commonPool));
        
        // Generate uncommon catalog loot table
        LootPool.Builder uncommonPool = LootPool.lootPool().add(book(BooksPM.DREAM_JOURNAL, Rarity.UNCOMMON, 1));
        IntStream.rangeClosed(6, 20).forEach(index -> uncommonPool.add(book(BooksPM.create("loremipsum" + index), Rarity.UNCOMMON, 1)));  // FIXME Remove once library testing is complete
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_UNCOMMON, LootTable.lootTable().withPool(uncommonPool));
        
        // Generate rare catalog loot table
        LootPool.Builder rarePool = LootPool.lootPool().add(book(BooksPM.TEST_BOOK, Rarity.RARE, 1));
        IntStream.rangeClosed(1, 5).forEach(index -> rarePool.add(book(BooksPM.create("loremipsum" + index), Rarity.RARE, 1)));  // FIXME Remove once library testing is complete
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_RARE, LootTable.lootTable().withPool(rarePool));
        
        // Generate epic catalog loot table for archaeology tablets
        LootPool.Builder epicPool = LootPool.lootPool().add(book(BooksPM.TEST_BOOK, Rarity.EPIC, 1));
        IntStream.rangeClosed(0, 0).forEach(index -> epicPool.add(book(BooksPM.create("loremipsum" + index), Rarity.EPIC, 100)));  // FIXME Remove once library testing is complete
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_EPIC, LootTable.lootTable().withPool(epicPool));

        // Generate treasure catalog loot table
        LootPool.Builder enchPool = LootPool.lootPool();
        ForgeRegistries.ENCHANTMENTS.getEntries().stream().filter(e -> e.getKey().location().getNamespace().equals(PrimalMagick.MODID)).map(e -> e.getValue()).forEach(ench -> {
            enchPool.add(enchantedBook(ench));
        });
        this.registerLootTable(writer, LootTablesPM.LIBRARY_CATALOG_TREASURE, LootTable.lootTable().withPool(enchPool));
    }
    
    @SuppressWarnings("deprecation")
    protected static LootPoolEntryContainer.Builder<?> catalog(ResourceLocation lootTable, ResourceKey<BookLanguage> languageKey, int weight) {
        CompoundTag tag = Util.make(new CompoundTag(), t -> t.putString(StaticBookItem.TAG_BOOK_LANGUAGE_ID, languageKey.location().toString()));
        return LootTableReference.lootTableReference(lootTable).setWeight(weight).apply(SetNbtFunction.setTag(tag));
    }
    
    @SuppressWarnings("deprecation")
    protected static LootPoolEntryContainer.Builder<?> book(ResourceKey<BookDefinition> bookDefKey, Rarity rarity, int weight) {
        Supplier<StaticBookItem> itemSupplier = switch (rarity) {
            case UNCOMMON -> ItemsPM.STATIC_BOOK_UNCOMMON;
            case RARE -> ItemsPM.STATIC_BOOK_RARE;
            case EPIC -> ItemsPM.STATIC_TABLET;
            default -> ItemsPM.STATIC_BOOK;
        };
        CompoundTag tag = Util.make(new CompoundTag(), t -> t.putString(StaticBookItem.TAG_BOOK_ID, bookDefKey.location().toString()));
        return LootItem.lootTableItem(itemSupplier.get()).setWeight(weight).apply(SetNbtFunction.setTag(tag));
    }
    
    protected static LootPoolEntryContainer.Builder<?> enchantedBook(Enchantment ench) {
        return LootItem.lootTableItem(Items.BOOK).setWeight(ench.getRarity().getWeight()).apply(new EnchantRandomlyFunction.Builder().withEnchantment(ench));
    }
    
    @SuppressWarnings("deprecation")
    protected static LootPoolEntryContainer.Builder<?> autoTranslatingBook(ResourceKey<BookDefinition> bookKey) {
        CompoundTag tag = Util.make(new CompoundTag(), t -> {
            t.putString(StaticBookItem.TAG_BOOK_ID, bookKey.location().toString());
            t.putString(StaticBookItem.TAG_BOOK_LANGUAGE_ID, BookLanguagesPM.BABELTONGUE.location().toString());
        });
        return LootItem.lootTableItem(ItemsPM.STATIC_BOOK.get()).setWeight(1).apply(SetNbtFunction.setTag(tag));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(LibraryLootTables::new, LootContextParamSets.CHEST);
    }
}
