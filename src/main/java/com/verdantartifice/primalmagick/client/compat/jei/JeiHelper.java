package com.verdantartifice.primalmagick.client.compat.jei;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.arcane_workbench.ArcaneWorkbenchRecipeCategory;
import com.verdantartifice.primalmagick.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.containers.ArcaneWorkbenchContainer;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
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

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        this.arcaneCategory = new ArcaneWorkbenchRecipeCategory(guiHelper);
        registration.addRecipeCategories(
                this.arcaneCategory
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (this.arcaneCategory == null) {
            throw new IllegalStateException("Arcane workbench recipe category is null");
        }
        
        CategoryRecipes categoryRecipes = new CategoryRecipes();
        registration.addRecipes(categoryRecipes.getArcaneRecipes(this.arcaneCategory), ArcaneWorkbenchRecipeCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.ARCANE_WORKBENCH.get()), ArcaneWorkbenchRecipeCategory.UID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ArcaneWorkbenchContainer.class, ArcaneWorkbenchRecipeCategory.UID, 1, 9, 11, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ArcaneWorkbenchScreen.class, 104, 52, 22, 15, ArcaneWorkbenchRecipeCategory.UID);
    }
}
