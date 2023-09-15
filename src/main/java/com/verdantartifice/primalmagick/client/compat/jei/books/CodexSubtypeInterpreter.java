package com.verdantartifice.primalmagick.client.compat.jei.books;

import com.verdantartifice.primalmagick.common.items.books.LinguisticsGainItem;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;

public class CodexSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final CodexSubtypeInterpreter INSTANCE = new CodexSubtypeInterpreter();
    
    private CodexSubtypeInterpreter() {}

    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        if (!ingredient.hasTag() || !(ingredient.getItem() instanceof LinguisticsGainItem codexItem)) {
            return IIngredientSubtypeInterpreter.NONE;
        }
        return LinguisticsGainItem.getBookLanguage(ingredient).languageId().toString();
    }
}
