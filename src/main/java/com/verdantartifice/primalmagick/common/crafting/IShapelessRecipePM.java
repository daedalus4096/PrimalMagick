package com.verdantartifice.primalmagick.common.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

public interface IShapelessRecipePM<C extends Container> extends Recipe<C> {
    boolean isSimple();
    
    @Override
    default boolean matches(C inv, Level worldIn) {
        StackedContents helper = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        int count = 0;
        
        for (int index = 0; index < inv.getContainerSize(); index++) {
            ItemStack stack = inv.getItem(index);
            if (!stack.isEmpty()) {
                count++;
                if (this.isSimple()) {
                    helper.accountStack(stack, 1);
                } else {
                    inputs.add(stack);
                }
            }
        }
        
        return (count == this.getIngredients().size()) && (this.isSimple() ? helper.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.getIngredients()) != null);
    }

    @Override
    default ItemStack assemble(C pContainer, RegistryAccess pRegistryAccess) {
        return this.getResultItem(pRegistryAccess).copy();
    }

    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return (pWidth * pHeight) >= this.getIngredients().size();
    }
}
