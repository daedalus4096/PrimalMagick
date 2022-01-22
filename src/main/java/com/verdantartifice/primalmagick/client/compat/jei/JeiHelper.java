package com.verdantartifice.primalmagick.client.compat.jei;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.arcane_crafting.ArcaneWorkbenchRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.concocting.ConcocterRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.concocting.ConcoctionSubtypeInterpreter;
import com.verdantartifice.primalmagick.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagick.client.gui.ConcocterScreen;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.containers.ArcaneWorkbenchContainer;
import com.verdantartifice.primalmagick.common.containers.ConcocterContainer;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
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
        this.arcaneCategory = new ArcaneWorkbenchRecipeCategory(guiHelper);
        this.concoctingCategory = new ConcocterRecipeCategory(guiHelper);
        registration.addRecipeCategories(
            this.arcaneCategory,
            this.concoctingCategory
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        CategoryRecipes categoryRecipes = new CategoryRecipes();
        if (this.arcaneCategory != null) {
            registration.addRecipes(categoryRecipes.getArcaneRecipes(this.arcaneCategory), ArcaneWorkbenchRecipeCategory.UID);
        }
        if (this.concoctingCategory != null) {
            registration.addRecipes(categoryRecipes.getConcoctingRecipes(this.concoctingCategory), ConcocterRecipeCategory.UID);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.ARCANE_WORKBENCH.get()), ArcaneWorkbenchRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ItemsPM.CONCOCTER.get()), ConcocterRecipeCategory.UID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ArcaneWorkbenchContainer.class, ArcaneWorkbenchRecipeCategory.UID, 1, 9, 11, 36);
        registration.addRecipeTransferHandler(ConcocterContainer.class, ConcocterRecipeCategory.UID, 1, 9, 11, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ArcaneWorkbenchScreen.class, 104, 52, 22, 15, ArcaneWorkbenchRecipeCategory.UID);
        registration.addRecipeClickArea(ConcocterScreen.class, 104, 35, 22, 15, ConcocterRecipeCategory.UID);
    }
}
