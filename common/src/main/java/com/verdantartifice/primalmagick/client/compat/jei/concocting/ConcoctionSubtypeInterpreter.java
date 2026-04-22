package com.verdantartifice.primalmagick.client.compat.jei.concocting;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcoctionSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final ConcoctionSubtypeInterpreter INSTANCE = new ConcoctionSubtypeInterpreter();
    
    private ConcoctionSubtypeInterpreter() {}

    @Override
    @Nullable
    public Object getSubtypeData(ItemStack itemStack, @NotNull UidContext context) {
        PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
        if (contents == null) {
            return null;
        }
        return contents.potion().orElse(null);
    }
}
