package com.verdantartifice.primalmagick.common.items.entities;

import com.verdantartifice.primalmagick.common.entities.projectiles.ManaArrowEntity;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public @NotNull AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        Item pickupItem = SOURCE_MAPPING.containsKey(this.source) ? SOURCE_MAPPING.get(this.source) : Items.ARROW;
        return new ManaArrowEntity(level, shooter, this.source, new ItemStack(pickupItem), weapon);
    }

    @Override
    public @NotNull Projectile asProjectile(Level pLevel, Position pPos, ItemStack pStack, Direction pDirection) {
        ManaArrowEntity arrow = new ManaArrowEntity(pLevel, pPos.x(), pPos.y(), pPos.z(), this.source, pStack.copyWithCount(1), null);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    public Source getSource() {
        return this.source;
    }
    
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? this.source.getColor() : 0xFFFFFF;
    }
    
    public static Collection<ManaArrowItem> getManaArrows() {
        return Collections.unmodifiableCollection(SOURCE_MAPPING.values());
    }
}
