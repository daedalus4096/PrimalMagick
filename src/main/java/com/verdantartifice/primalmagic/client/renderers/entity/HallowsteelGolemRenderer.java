package com.verdantartifice.primalmagic.client.renderers.entity;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.entity.layers.HallowsteelGolemCracksLayer;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HallowsteelGolemEntity;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for a hallowsteel golem.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class HallowsteelGolemRenderer extends AbstractEnchantedGolemRenderer<HallowsteelGolemEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/hallowsteel_golem/hallowsteel_golem.png");
    
    public HallowsteelGolemRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager);
        this.addLayer(new HallowsteelGolemCracksLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(HallowsteelGolemEntity entity) {
        return TEXTURE;
    }
}
