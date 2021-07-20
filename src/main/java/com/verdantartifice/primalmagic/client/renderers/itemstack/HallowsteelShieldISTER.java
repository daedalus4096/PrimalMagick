package com.verdantartifice.primalmagic.client.renderers.itemstack;

import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom item stack renderer for a hallowsteel shield.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class HallowsteelShieldISTER extends AbstractTieredShieldISTER {
    public static final ResourceLocation TEXTURE_SHIELD_BASE = new ResourceLocation("primalmagic:entity/shield/hallowsteel_shield_base");
    public static final ResourceLocation TEXTURE_SHIELD_NO_PATTERN = new ResourceLocation("primalmagic:entity/shield/hallowsteel_shield_base_nopattern");
    @SuppressWarnings("deprecation")
    protected static final RenderMaterial LOCATION_SHIELD_BASE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE_SHIELD_BASE);
    @SuppressWarnings("deprecation")
    protected static final RenderMaterial LOCATION_SHIELD_NO_PATTERN = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE_SHIELD_NO_PATTERN);

    @Override
    protected RenderMaterial getRenderMaterial(boolean hasPattern) {
        return hasPattern ? LOCATION_SHIELD_BASE : LOCATION_SHIELD_NO_PATTERN;
    }
}
