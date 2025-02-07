package com.verdantartifice.primalmagick.common.items.wands;

import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Item definition for a wand gem.  May be used to construct modular wands.
 * 
 * @author Daedalus4096
 */
public class WandGemItem extends Item {
    protected static final List<WandGemItem> GEMS = new ArrayList<>();
    
    protected final WandGem gem;

    public WandGemItem(WandGem gem, Properties properties) {
        super(properties.rarity(gem.getRarity()));
        this.gem = gem;
        GEMS.add(this);
    }

    public WandGem getWandGem() {
        return this.gem;
    }
    
    public static Collection<WandGemItem> getAllGems() {
        return Collections.unmodifiableCollection(GEMS);
    }
}
