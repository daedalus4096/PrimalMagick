package com.verdantartifice.primalmagic.common.items.food;

import com.verdantartifice.primalmagic.common.effects.EffectsPM;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

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
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.addPotionEffect(new EffectInstance(EffectsPM.MANAFRUIT.get(), 6000, this.amplifier));
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) {
        if (stack.getItem() instanceof ManafruitItem) {
            return ((ManafruitItem)stack.getItem()).amplifier > 0;
        } else {
            return super.hasEffect(stack);
        }
    }
}
