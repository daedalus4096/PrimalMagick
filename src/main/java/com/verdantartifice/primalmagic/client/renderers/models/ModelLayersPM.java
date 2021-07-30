package com.verdantartifice.primalmagic.client.renderers.models;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.RenderingRegistry;

/**
 * Definition of registered model layer locations.
 * 
 * @author Daedalus4096
 */
public class ModelLayersPM {
    public static final ModelLayerLocation PIXIE_BASIC = RenderingRegistry.registerMainModelLayer(new ResourceLocation(PrimalMagic.MODID, "pixie_basic"));
    public static final ModelLayerLocation PIXIE_ROYAL = RenderingRegistry.registerMainModelLayer(new ResourceLocation(PrimalMagic.MODID, "pixie_royal"));
}
