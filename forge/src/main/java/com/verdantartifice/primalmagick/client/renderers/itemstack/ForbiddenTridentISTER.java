package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom item stack renderer for a forbidden trident.
 * 
 * @author Daedalus4096
 */
public class ForbiddenTridentISTER extends AbstractTieredTridentISTER {
    protected static final ModelResourceLocation MRL = new ModelResourceLocation(PrimalMagick.resource("forbidden_trident"), "inventory");
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/entity/trident/forbidden_trident.png");

    @Override
    public ModelResourceLocation getModelResourceLocation() {
        return MRL;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
