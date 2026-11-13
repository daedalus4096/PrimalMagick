package com.verdantartifice.primalmagick.common.concoctions;

import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Helper methods for handling concoctions.
 * 
 * @author Daedalus4096
 */
public class ConcoctionUtils {
    public static ItemStackTemplate newConcoction(Holder<Potion> potion, ConcoctionType type) {
        DataComponentPatch componentPatch = DataComponentPatch.builder()
                .set(DataComponents.POTION_CONTENTS, new PotionContents(potion))
                .set(DataComponentsPM.CONCOCTION_TYPE.get(), type)
                .set(DataComponentsPM.CONCOCTION_DOSES.get(), type.getMaxDoses())
                .build();
        return new ItemStackTemplate(ItemsPM.CONCOCTION.get(), componentPatch);
    }
    
    public static ItemStackTemplate newBomb(Holder<Potion> potion) {
        return newBomb(potion, FuseType.MEDIUM);
    }

    public static ItemStackTemplate newBomb(Holder<Potion> potion, FuseType fuse) {
        DataComponentPatch componentPatch = DataComponentPatch.builder()
                .set(DataComponents.POTION_CONTENTS, new PotionContents(potion))
                .set(DataComponentsPM.CONCOCTION_TYPE.get(), ConcoctionType.BOMB)
                .set(DataComponentsPM.CONCOCTION_DOSES.get(), ConcoctionType.BOMB.getMaxDoses())
                .set(DataComponentsPM.FUSE_TYPE.get(), fuse)
                .build();
        return new ItemStackTemplate(ItemsPM.ALCHEMICAL_BOMB.get(), componentPatch);
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
        stack.set(DataComponentsPM.CONCOCTION_DOSES.get(), Math.min(type.getMaxDoses(), doses));
        return stack;
    }
    
    @Nonnull
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
