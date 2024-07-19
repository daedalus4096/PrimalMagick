package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagick.common.entities.companions.golems.AbstractEnchantedGolemEntity;
import com.verdantartifice.primalmagick.common.entities.companions.golems.AbstractEnchantedGolemEntity.Cracks;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Base layer renderer for damage cracks on an enchanted golem.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractEnchantedGolemCracksLayer<T extends AbstractEnchantedGolemEntity> extends RenderLayer<T, EnchantedGolemModel<T>> {
    public AbstractEnchantedGolemCracksLayer(RenderLayerParent<T, EnchantedGolemModel<T>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isInvisible()) {
            Cracks cracks = entitylivingbaseIn.getCrackLevel();
            if (cracks != Cracks.NONE) {
                ResourceLocation tex = this.getTextureMap().get(cracks);
                renderColoredCutoutModel(this.getParentModel(), tex, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, -1);
            }
        }
    }
    
    protected abstract Map<Cracks, ResourceLocation> getTextureMap();
}
