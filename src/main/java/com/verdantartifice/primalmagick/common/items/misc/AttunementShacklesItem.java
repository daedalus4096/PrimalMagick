package com.verdantartifice.primalmagick.common.items.misc;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.item.Item;

/**
 * Item definition for attunement shackles.  An item that, when carried in a player's inventory, will
 * suppress the effects of any lesser or greater attunements they may have achieved.  Minor attunements
 * still function.
 * 
 * @author Daedalus4096
 */
public class AttunementShacklesItem extends Item {
    protected static final Map<Source, AttunementShacklesItem> SHACKLES = new HashMap<>();
    
    protected final Source source;
    
    public AttunementShacklesItem(Source source, Item.Properties properties) {
        super(properties);
        this.source = source;
        SHACKLES.put(source, this);
    }
    
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? 0xFFFFFF : this.source.getColor();
    }
    
    @Nullable
    public static AttunementShacklesItem getShackles(Source source) {
        return SHACKLES.getOrDefault(source, null);
    }
    
    public static Iterable<AttunementShacklesItem> getAllShackles() {
        return Iterables.unmodifiableIterable(SHACKLES.values());
    }
}
