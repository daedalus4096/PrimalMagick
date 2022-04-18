package com.verdantartifice.primalmagick.client.compat.jei;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.arcane_crafting.ArcaneCraftingRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.concocting.ConcoctingRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.concocting.ConcoctionSubtypeInterpreter;
import com.verdantartifice.primalmagick.client.compat.jei.dissolution.DissolutionRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.ritual.RitualRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.runecarving.RunecarvingRecipeCategory;
import com.verdantartifice.primalmagick.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagick.client.gui.ConcocterScreen;
import com.verdantartifice.primalmagick.client.gui.DissolutionChamberScreen;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.containers.ArcaneWorkbenchContainer;
import com.verdantartifice.primalmagick.common.containers.ConcocterContainer;
import com.verdantartifice.primalmagick.common.containers.DissolutionChamberContainer;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Main class for JEI integration.
 * 
 * @author Daedalus4096
 */
@JeiPlugin
public class JeiHelper implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "jei");
    
    @Nullable
    private IRecipeCategory<IArcaneRecipe> arcaneCategory;
    @Nullable
    private IRecipeCategory<IConcoctingRecipe> concoctingCategory;
    @Nullable
    private IRecipeCategory<IRunecarvingRecipe> runecarvingCategory;
    @Nullable
    private IRecipeCategory<IDissolutionRecipe> dissolutionCategory;
    @Nullable
    private IRecipeCategory<IRitualRecipe> ritualCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ItemsPM.CONCOCTION.get(), ConcoctionSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ItemsPM.ALCHEMICAL_BOMB.get(), ConcoctionSubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        this.arcaneCategory = new ArcaneCraftingRecipeCategory(guiHelper);
        this.concoctingCategory = new ConcoctingRecipeCategory(guiHelper);
        this.runecarvingCategory = new RunecarvingRecipeCategory(guiHelper);
        this.dissolutionCategory = new DissolutionRecipeCategory(guiHelper);
        this.ritualCategory = new RitualRecipeCategory(guiHelper);
        registration.addRecipeCategories(
            this.arcaneCategory,
            this.concoctingCategory,
            this.runecarvingCategory,
            this.dissolutionCategory,
            this.ritualCategory
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        CategoryRecipes categoryRecipes = new CategoryRecipes();
        if (this.arcaneCategory != null) {
            registration.addRecipes(RecipeTypesPM.ARCANE_CRAFTING, categoryRecipes.getArcaneRecipes(this.arcaneCategory));
        }
        if (this.concoctingCategory != null) {
            registration.addRecipes(categoryRecipes.getConcoctingRecipes(this.concoctingCategory), ConcoctingRecipeCategory.UID);
        }
        if (this.runecarvingCategory != null) {
            registration.addRecipes(categoryRecipes.getRunecarvingRecipes(this.runecarvingCategory), RunecarvingRecipeCategory.UID);
        }
        if (this.dissolutionCategory != null) {
            registration.addRecipes(categoryRecipes.getDissolutionRecipes(this.dissolutionCategory), DissolutionRecipeCategory.UID);
        }
        if (this.ritualCategory != null) {
            registration.addRecipes(categoryRecipes.getRitualRecipes(this.ritualCategory), RitualRecipeCategory.UID);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.ARCANE_WORKBENCH.get()), RecipeTypesPM.ARCANE_CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.CONCOCTER.get()), ConcoctingRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.RUNECARVING_TABLE.get()), RunecarvingRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.DISSOLUTION_CHAMBER.get()), DissolutionRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.RITUAL_ALTAR.get()), RitualRecipeCategory.UID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ArcaneWorkbenchContainer.class, RecipeTypesPM.ARCANE_CRAFTING, 1, 9, 11, 36);
        registration.addRecipeTransferHandler(ConcocterContainer.class, ConcoctingRecipeCategory.UID, 1, 9, 11, 36);
        registration.addRecipeTransferHandler(DissolutionChamberContainer.class, DissolutionRecipeCategory.UID, 1, 1, 3, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ArcaneWorkbenchScreen.class, 104, 52, 22, 15, RecipeTypesPM.ARCANE_CRAFTING);
        registration.addRecipeClickArea(ConcocterScreen.class, 104, 35, 22, 15, ConcoctingRecipeCategory.UID);
        registration.addRecipeClickArea(DissolutionChamberScreen.class, 79, 35, 22, 15, DissolutionRecipeCategory.UID);
    }
}
