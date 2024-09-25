package com.verdantartifice.primalmagick.common.items.food;

import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Item definition for manafruit.  Manafruit is food that gives you a mana discount buff when
 * eaten.  Normally, this effect could be specified in the food attribute of the item's
 * properties, but due to registration order issues, food items cannot use custom potion effects.
 * 
 * @author Daedalus4096
 */
public class ManafruitItem extends Item {
    protected final int amplifier;
    
    public ManafruitItem(int amplifier, Item.Properties properties) {
        super(properties);
        this.amplifier = amplifier;
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            entityLiving.addEffect(new MobEffectInstance(EffectsPM.MANAFRUIT.getHolder().get(), 6000, this.amplifier));
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        if (stack.getItem() instanceof ManafruitItem) {
            return ((ManafruitItem)stack.getItem()).amplifier > 0;
        } else {
            return super.isFoil(stack);
        }
    }
}
