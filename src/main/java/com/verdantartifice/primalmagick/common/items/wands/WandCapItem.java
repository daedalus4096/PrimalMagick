package com.verdantartifice.primalmagick.common.items.wands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.verdantartifice.primalmagick.common.wands.WandCap;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

/**
 * Item definition for a wand cap.  May be used to construct modular wands.
 * 
 * @author Daedalus4096
 */
public class WandCapItem extends Item {
    protected static final List<WandCapItem> CAPS = new ArrayList<>();
    
    protected final WandCap cap;

    public WandCapItem(WandCap cap, Properties properties) {
        super(properties);
        this.cap = cap;
        CAPS.add(this);
    }

    public WandCap getWandCap() {
        return this.cap;
    }
    
    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.cap.getRarity();
    }
    
    public static Collection<WandCapItem> getAllCaps() {
        return Collections.unmodifiableCollection(CAPS);
    }
}
