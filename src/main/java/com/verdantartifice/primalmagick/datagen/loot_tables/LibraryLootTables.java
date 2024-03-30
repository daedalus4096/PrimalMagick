package com.verdantartifice.primalmagick.datagen.loot_tables;

import java.util.function.BiConsumer;

import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

/**
 * Data provider for all of the mod's ancient library loot tables.
 * 
 * @author Daedalus4096
 */
public class LibraryLootTables extends AbstractGameplayLootTableSubProvider {
    @SuppressWarnings("deprecation")
    @Override
    protected void addTables(BiConsumer<ResourceLocation, Builder> writer) {
        // TODO Auto-generated method stub
        CompoundTag testBookTag = new CompoundTag();
        testBookTag.putString(StaticBookItem.TAG_BOOK_ID, BooksPM.TEST_BOOK.getId().toString());
        testBookTag.putString(StaticBookItem.TAG_BOOK_LANGUAGE_ID, BookLanguagesPM.DEFAULT.getId().toString());
        
        this.registerLootTable(writer, LootTablesPM.LIBRARY_TEST, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(6.0F))
                .add(EmptyLootItem.emptyItem().setWeight(2))
                .add(LootItem.lootTableItem(ItemsPM.STATIC_BOOK.get()).setWeight(4).apply(SetNbtFunction.setTag(testBookTag)))));
    }
    
    public static LootTableProvider.SubProviderEntry getSubProviderEntry() {
        return new LootTableProvider.SubProviderEntry(LibraryLootTables::new, LootContextParamSets.CHEST);
    }
}
