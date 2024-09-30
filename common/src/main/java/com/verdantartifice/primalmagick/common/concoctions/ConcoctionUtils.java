package com.verdantartifice.primalmagick.common.concoctions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

/**
 * Helper methods for handling concoctions.
 * 
 * @author Daedalus4096
 */
public class ConcoctionUtils {
    public static ItemStack newConcoction(Holder<Potion> potion, ConcoctionType type) {
        return setConcoctionType(PotionContents.createItemStack(ItemsPM.CONCOCTION.get(), potion), type);
    }
    
    public static ItemStack newBomb(Holder<Potion> potion) {
        return newBomb(potion, FuseType.MEDIUM);
    }
    
    public static ItemStack newBomb(Holder<Potion> potion, FuseType fuse) {
        return setFuseType(setConcoctionType(PotionContents.createItemStack(ItemsPM.ALCHEMICAL_BOMB.get(), potion), ConcoctionType.BOMB), fuse);
    }
    
    @Nonnull
    public static ConcoctionType getConcoctionType(@Nonnull ItemStack stack) {
        return stack.getOrDefault(DataComponentsPM.CONCOCTION_TYPE.get(), ConcoctionType.WATER);
    }
    
    @Nonnull
    public static ItemStack setConcoctionType(@Nonnull ItemStack stack, @Nullable ConcoctionType concoctionType) {
        if (concoctionType != null) {
            stack.set(DataComponentsPM.CONCOCTION_TYPE.get(), concoctionType);
            setCurrentDoses(stack, concoctionType.getMaxDoses());
        }
        return stack;
    }
    
    public static int getCurrentDoses(@Nonnull ItemStack stack) {
        return stack.getOrDefault(DataComponentsPM.CONCOCTION_DOSES.get(), 0);
    }
    
    @Nonnull
    public static ItemStack setCurrentDoses(@Nonnull ItemStack stack, int doses) {
        ConcoctionType type = getConcoctionType(stack);
        stack.set(DataComponentsPM.CONCOCTION_DOSES.get(), Math.min(type == null ? 1 : type.getMaxDoses(), doses));
        return stack;
    }
    
    @Nullable
    public static FuseType getFuseType(@Nonnull ItemStack stack) {
        return stack.getOrDefault(DataComponentsPM.FUSE_TYPE.get(), FuseType.MEDIUM);
    }
    
    @Nonnull
    public static ItemStack setFuseType(@Nonnull ItemStack stack, @Nullable FuseType fuseType) {
        if (fuseType != null) {
            stack.set(DataComponentsPM.FUSE_TYPE.get(), fuseType);
        }
        return stack;
    }
    
    public static boolean hasBeneficialEffect(@Nonnull Potion potion) {
        for (MobEffectInstance instance : potion.getEffects()) {
            if (instance.getEffect().value().isBeneficial()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBomb(@Nonnull ItemStack stack) {
        return stack.getItem() == ItemsPM.ALCHEMICAL_BOMB.get();
    }
}
