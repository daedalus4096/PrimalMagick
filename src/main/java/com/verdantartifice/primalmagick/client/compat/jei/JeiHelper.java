package com.verdantartifice.primalmagick.client.compat.jei;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.arcane_workbench.ShapedArcaneWorkbenchRecipeCategory;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.resources.ResourceLocation;

/**
 * Main class for JEI integration.
 * 
 * @author Daedalus4096
 */
@JeiPlugin
public class JeiHelper implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "jei");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new ShapedArcaneWorkbenchRecipeCategory(guiHelper)
        );
    }
}
