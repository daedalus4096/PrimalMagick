package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom item stack renderer for a hallowsteel trident.
 * 
 * @author Daedalus4096
 */
public class HallowsteelTridentISTER extends AbstractTieredTridentISTER {
    protected static final ModelResourceLocation MRL = Services.MODEL_RESOURCE_LOCATIONS.createInventory(ResourceUtils.loc("hallowsteel_trident"));
    protected static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/entity/trident/hallowsteel_trident.png");

    @Override
    public ModelResourceLocation getModelResourceLocation() {
        return MRL;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
