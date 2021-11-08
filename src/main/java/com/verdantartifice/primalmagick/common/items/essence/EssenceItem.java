package com.verdantartifice.primalmagick.common.items.essence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Item definition for magical essences.
 * 
 * @author Daedalus4096
 */
public class EssenceItem extends Item {
    protected static final Map<EssenceType, Map<Source, Item>> ESSENCES = new HashMap<>();
    
    protected final EssenceType type;
    protected final Source source;

    public EssenceItem(@Nonnull EssenceType type, @Nonnull Source source) {
        super(new Item.Properties().tab(PrimalMagic.ITEM_GROUP).rarity(type.getRarity()));
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
        ESSENCES.computeIfAbsent(type, k -> new HashMap<>()).put(source, item);
    }
    
    @Nonnull
    public static ItemStack getEssence(@Nullable EssenceType type, @Nullable Source source) {
        return getEssence(type, source, 1);
    }
    
    @Nonnull
    public static ItemStack getEssence(@Nullable EssenceType type, @Nullable Source source, int count) {
        Item item = ESSENCES.getOrDefault(type, Collections.emptyMap()).get(source);
        return (item == null) ? ItemStack.EMPTY : new ItemStack(item, count);
    }
}
