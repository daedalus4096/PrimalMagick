package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom item stack renderer for a forbidden trident.
 * 
 * @author Daedalus4096
 */
public class ForbiddenTridentISTER extends AbstractTieredTridentISTER {
    protected static final ModelResourceLocation MRL = Services.MODEL_RESOURCE_LOCATIONS.createInventory(ResourceUtils.loc("forbidden_trident"));
    protected static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/entity/trident/forbidden_trident.png");

    @Override
    public ModelResourceLocation getModelResourceLocation() {
        return MRL;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
