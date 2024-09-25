package com.verdantartifice.primalmagick.common.items.essence;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Item definition for magickal essences.
 * 
 * @author Daedalus4096
 */
public class EssenceItem extends Item {
    protected static final Table<EssenceType, Source, Item> ESSENCES = HashBasedTable.create();
    
    protected final EssenceType type;
    protected final Source source;

    public EssenceItem(@Nonnull EssenceType type, @Nonnull Source source) {
        super(new Item.Properties().rarity(type.getRarity()));
        this.type = type;
        this.source = source;
        register(type, source, this);
    }

    public EssenceType getEssenceType() {
        return this.type;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    protected static void register(@Nonnull EssenceType type, @Nonnull Source source, @Nonnull Item item) {
        ESSENCES.put(type, source, item);
    }
    
    @Nonnull
    public static ItemStack getEssence(@Nullable EssenceType type, @Nullable Source source) {
        return getEssence(type, source, 1);
    }
    
    @Nonnull
    public static ItemStack getEssence(@Nullable EssenceType type, @Nullable Source source, int count) {
        Item item = ESSENCES.get(type, source);
        return (item == null) ? ItemStack.EMPTY : new ItemStack(item, count);
    }
    
    public static Collection<Item> getAllEssences() {
        return Collections.unmodifiableCollection(ESSENCES.values());
    }
}
