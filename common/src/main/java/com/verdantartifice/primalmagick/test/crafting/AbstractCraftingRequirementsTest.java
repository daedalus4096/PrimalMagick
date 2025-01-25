package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.GameType;

import java.util.List;

public abstract class AbstractCraftingRequirementsTest extends AbstractBaseTest {
    public void arcane_recipe(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        
        // Get the Mana Salt recipe from the recipe manager
        var container = CraftingInput.of(3, 1, List.of(new ItemStack(Items.REDSTONE), new ItemStack(ItemsPM.REFINED_SALT.get()), new ItemStack(ItemsPM.ESSENCE_DUST_EARTH.get())));
        var recipe = helper.getLevel().getRecipeManager().getRecipeFor(RecipeTypesPM.ARCANE_CRAFTING.get(), container, helper.getLevel());
        helper.assertTrue(recipe.isPresent(), "Recipe not found when expected");
        
        // Confirm that it has a requirement which the mock player does not yet meet
        var reqOpt = recipe.get().value().getRequirement();
        helper.assertTrue(reqOpt.isPresent(), "Recipe requirement not found when expected");
        helper.assertFalse(reqOpt.get().isMetBy(player), "Player meets requirement without research");
        
        // Grant the required research and confirm that the requirement is then met
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.MANA_SALTS);
        helper.assertTrue(reqOpt.get().isMetBy(player), "Player does not meet requirement after being granted required research");
        helper.succeed();
    }
    
    public void ritual_recipe(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        
        // Get the Manafruit recipe from the recipe manager
        var container = CraftingInput.of(3, 1, List.of(new ItemStack(Items.APPLE), new ItemStack(Items.HONEY_BOTTLE), new ItemStack(ItemsPM.MANA_SALTS.get())));
        var recipe = helper.getLevel().getRecipeManager().getRecipeFor(RecipeTypesPM.RITUAL.get(), container, helper.getLevel());
        helper.assertTrue(recipe.isPresent(), "Recipe not found when expected");
        
        // Confirm that it has a requirement which the mock player does not yet meet
        var reqOpt = recipe.get().value().getRequirement();
        helper.assertTrue(reqOpt.isPresent(), "Recipe requirement not found when expected");
        helper.assertFalse(reqOpt.get().isMetBy(player), "Player meets requirement without research");
        
        // Grant the required research and confirm that the requirement is then met
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.MANAFRUIT);
        helper.assertTrue(reqOpt.get().isMetBy(player), "Player does not meet requirement after being granted required research");
        helper.succeed();
    }
}
