package com.verdantartifice.primalmagic.common.concoctions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;

/**
 * Helper methods for handling concoctions.
 * 
 * @author Daedalus4096
 */
public class ConcoctionUtils {
    public static ItemStack newConcoction(Potion potion, ConcoctionType type) {
        return setConcoctionType(PotionUtils.addPotionToItemStack(new ItemStack(ItemsPM.CONCOCTION.get()), potion), type);
    }
    
    public static ItemStack newBomb(Potion potion, FuseType fuse) {
        return setFuseType(newConcoction(potion, ConcoctionType.BOMB), fuse);
    }
    
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
    
    @Nullable
    public static FuseType getFuseType(@Nonnull ItemStack stack) {
        return FuseType.fromName(stack.getOrCreateTag().getString("BombFuse"));
    }
    
    @Nonnull
    public static ItemStack setFuseType(@Nonnull ItemStack stack, @Nullable FuseType fuseType) {
        if (fuseType != null) {
            stack.getOrCreateTag().putString("BombFuse", fuseType.getString());
        }
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
