package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class RepairTests extends AbstractBaseTest {
    public static void earthshatter_hammer_cannot_be_repaired(GameTestHelper helper) {
        // Create a pair of Earthshatter hammers with a point of damage
        ItemStack hammer1 = new ItemStack(ItemsPM.EARTHSHATTER_HAMMER.get());
        hammer1.setDamageValue(1);
        ItemStack hammer2 = hammer1.copy();

        // Get all recipes recognized that use the two hammers as inputs
        CraftingInput input = CraftingInput.of(2, 1, List.of(hammer1, hammer2));
        var recipeOpt = helper.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());

        // Confirm that no such recipes exist
        assertTrue(helper, recipeOpt.isEmpty(), "Found recipes for repairing Earthshatter Hammer when none should exist");
        helper.succeed();
    }
}
