package com.verdantartifice.primalmagick.common.items.essence;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * Item definition for magickal essences.
 * 
 * @author Daedalus4096
 */
public class EssenceItem extends Item {
    protected static final Table<EssenceType, Source, Item> ESSENCES = HashBasedTable.create();
    
    protected final EssenceType type;
    protected final Source source;

    public EssenceItem(@NotNull EssenceType type, @NotNull Source source, @NotNull Item.Properties properties) {
        super(properties.rarity(type.getRarity()));
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
    
    protected static void register(@NotNull EssenceType type, @NotNull Source source, @NotNull Item item) {
        ESSENCES.put(type, source, item);
    }
    
    @NotNull
    public static ItemStack getEssence(@Nullable EssenceType type, @Nullable Source source) {
        return getEssence(type, source, 1);
    }
    
    @NotNull
    public static ItemStack getEssence(@Nullable EssenceType type, @Nullable Source source, int count) {
        Item item = ESSENCES.get(type, source);
        return (item == null) ? ItemStack.EMPTY : new ItemStack(item, count);
    }

    @Nullable
    public static ItemStackTemplate getTemplate(@Nullable EssenceType type, @Nullable Source source) {
        return getTemplate(type, source, 1);
    }

    @Nullable
    public static ItemStackTemplate getTemplate(@Nullable EssenceType type, @Nullable Source source, int count) {
        Item item = ESSENCES.get(type, source);
        return (item == null) ? null : new ItemStackTemplate(item, count);
    }

    public static Collection<Item> getAllEssences() {
        return Collections.unmodifiableCollection(ESSENCES.values());
    }
}
