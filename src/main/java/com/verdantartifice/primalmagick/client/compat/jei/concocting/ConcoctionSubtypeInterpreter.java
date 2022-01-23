package com.verdantartifice.primalmagick.client.compat.jei.concocting;

import java.util.List;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

public class ConcoctionSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final ConcoctionSubtypeInterpreter INSTANCE = new ConcoctionSubtypeInterpreter();
    
    private ConcoctionSubtypeInterpreter() {}

    @Override
    public String apply(ItemStack itemStack, UidContext context) {
        if (!itemStack.hasTag()) {
            return IIngredientSubtypeInterpreter.NONE;
        }
        
        Potion potion = PotionUtils.getPotion(itemStack);
        ConcoctionType type = ConcoctionUtils.getConcoctionType(itemStack);
        String potionTypeString = potion.getName("");
        StringBuilder stringBuilder = new StringBuilder(potionTypeString + ";" + type.getSerializedName());
        List<MobEffectInstance> effects = PotionUtils.getMobEffects(itemStack);
        for (MobEffectInstance effect : effects) {
            stringBuilder.append(";").append(effect);
        }

        return stringBuilder.toString();
    }
}
