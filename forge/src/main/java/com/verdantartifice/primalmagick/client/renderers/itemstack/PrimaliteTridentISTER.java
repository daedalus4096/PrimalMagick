package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom item stack renderer for a primalite trident.
 * 
 * @author Daedalus4096
 */
public class PrimaliteTridentISTER extends AbstractTieredTridentISTER {
    protected static final ModelResourceLocation MRL = new ModelResourceLocation(PrimalMagick.resource("primalite_trident"), "inventory");
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/entity/trident/primalite_trident.png");

    @Override
    public ModelResourceLocation getModelResourceLocation() {
        return MRL;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
