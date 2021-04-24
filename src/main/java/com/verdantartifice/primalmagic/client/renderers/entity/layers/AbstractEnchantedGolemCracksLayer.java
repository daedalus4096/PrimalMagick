package com.verdantartifice.primalmagic.client.renderers.entity.layers;

import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagic.common.entities.companions.golems.AbstractEnchantedGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.AbstractEnchantedGolemEntity.Cracks;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * Base layer renderer for damage cracks on an enchanted golem.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractEnchantedGolemCracksLayer<T extends AbstractEnchantedGolemEntity> extends LayerRenderer<T, EnchantedGolemModel<T>> {
    public AbstractEnchantedGolemCracksLayer(IEntityRenderer<T, EnchantedGolemModel<T>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isInvisible()) {
            Cracks cracks = entitylivingbaseIn.getCrackLevel();
            if (cracks != Cracks.NONE) {
                ResourceLocation tex = this.getTextureMap().get(cracks);
                renderCutoutModel(this.getEntityModel(), tex, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, 1.0F, 1.0F);
            }
        }
    }
    
    protected abstract Map<Cracks, ResourceLocation> getTextureMap();
}
