package com.verdantartifice.primalmagic.common.concoctions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;

/**
 * Helper methods for handling concoctions.
 * 
 * @author Daedalus4096
 */
public class ConcoctionUtils {
    @Nullable
    public static ConcoctionType getConcoctionType(@Nonnull ItemStack stack) {
        return ConcoctionType.fromName(stack.getOrCreateTag().getString("ConcoctionType"));
    }
    
    @Nonnull
    public static ItemStack setConcoctionType(@Nonnull ItemStack stack, @Nullable ConcoctionType concoctionType) {
        if (concoctionType != null) {
            stack.getOrCreateTag().putString("ConcoctionType", concoctionType.getString());
            setCurrentDoses(stack, concoctionType.getMaxDoses());
        }
        return stack;
    }
    
    public static int getCurrentDoses(@Nonnull ItemStack stack) {
        return stack.getOrCreateTag().getInt("ConcoctionDoses");
    }
    
    @Nonnull
    public static ItemStack setCurrentDoses(@Nonnull ItemStack stack, int doses) {
        ConcoctionType type = getConcoctionType(stack);
        stack.getOrCreateTag().putInt("ConcoctionDoses", Math.min(type == null ? 1 : type.getMaxDoses(), doses));
        return stack;
    }
    
    public static boolean hasBeneficialEffect(@Nonnull Potion potion) {
        for (EffectInstance instance : potion.getEffects()) {
            if (instance.getPotion().isBeneficial()) {
                return true;
            }
        }
        return false;
    }
}
