package com.verdantartifice.primalmagic.client.renderers.entity;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.entity.layers.HexiumGolemCracksLayer;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HexiumGolemEntity;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for a hexium golem.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class HexiumGolemRenderer extends AbstractEnchantedGolemRenderer<HexiumGolemEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/hexium_golem/hexium_golem.png");
    
    public HexiumGolemRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager);
        this.addLayer(new HexiumGolemCracksLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(HexiumGolemEntity entity) {
        return TEXTURE;
    }
}
