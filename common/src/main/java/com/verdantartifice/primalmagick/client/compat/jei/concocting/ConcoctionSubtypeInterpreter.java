package com.verdantartifice.primalmagick.client.compat.jei.concocting;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public class ConcoctionSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final ConcoctionSubtypeInterpreter INSTANCE = new ConcoctionSubtypeInterpreter();
    
    private ConcoctionSubtypeInterpreter() {}

    @Override
    public Object getSubtypeData(ItemStack itemStack, UidContext context) {
        ConcoctionType type = ConcoctionUtils.getConcoctionType(itemStack);
        if (!itemStack.has(DataComponents.POTION_CONTENTS)) {
            return null;
        }
        
        PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
        String potionTypeString = Potion.getName(contents.potion(), "");
        StringBuilder stringBuilder = new StringBuilder(potionTypeString + ";" + type.getSerializedName());
        contents.forEachEffect(effect -> {
            stringBuilder.append(";").append(effect);
        });

        return stringBuilder.toString();
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return "";
    }
}
