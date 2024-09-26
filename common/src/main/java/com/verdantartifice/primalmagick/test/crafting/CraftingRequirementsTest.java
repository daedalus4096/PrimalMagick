package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.crafting.SimpleCraftingContainer;
import net.minecraftforge.gametest.GameTestHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@GameTestHolder(Constants.MOD_ID + ".crafting_requirements")
public class CraftingRequirementsTest {
    protected static final Logger LOGGER = LogManager.getLogger();

    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void arcane_recipe(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        
        // Get the Mana Salt recipe from the recipe manager
        var container = SimpleCraftingContainer.builder()
                .pattern("RSE")
                .define('R', Items.REDSTONE)
                .define('S', ItemRegistration.REFINED_SALT.get())
                .define('E', ItemRegistration.ESSENCE_DUST_EARTH.get())
                .build();
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
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void ritual_recipe(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        
        // Get the Manafruit recipe from the recipe manager
        var container = SimpleCraftingContainer.builder()
                .pattern("AHM")
                .define('A', Items.APPLE)
                .define('H', Items.HONEY_BOTTLE)
                .define('M', ItemRegistration.MANA_SALTS.get())
                .build();
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
