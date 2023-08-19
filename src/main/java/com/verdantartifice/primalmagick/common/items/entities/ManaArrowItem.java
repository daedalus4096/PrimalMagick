package com.verdantartifice.primalmagick.common.items.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagick.common.entities.projectiles.ManaArrowEntity;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition of an arrow infused with mana for a secondary effect.
 * 
 * @author Daedalus4096
 */
public class ManaArrowItem extends ArrowItem {
    public static final Map<Source, ManaArrowItem> SOURCE_MAPPING = new HashMap<>();
    
    protected final Source source;
    
    public ManaArrowItem(Source source, Item.Properties properties) {
        super(properties);
        this.source = source;
        SOURCE_MAPPING.put(this.source, this);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        ManaArrowEntity arrow = new ManaArrowEntity(level, shooter);
        arrow.setSource(this.source);
        return arrow;
    }
    
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? this.source.getColor() : 0xFFFFFF;
    }
    
    public static Collection<ManaArrowItem> getManaArrows() {
        return Collections.unmodifiableCollection(SOURCE_MAPPING.values());
    }
}
