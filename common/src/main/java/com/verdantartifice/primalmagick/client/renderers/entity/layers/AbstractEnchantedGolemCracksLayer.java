package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.EnchantedGolemRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Crackiness;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Base layer renderer for damage cracks on an enchanted golem.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractEnchantedGolemCracksLayer extends RenderLayer<EnchantedGolemRenderState, EnchantedGolemModel> {
    public AbstractEnchantedGolemCracksLayer(RenderLayerParent<EnchantedGolemRenderState, EnchantedGolemModel> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void submit(@NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, int packedLight, @NotNull EnchantedGolemRenderState renderState, float yRot, float xRot) {
        if (!renderState.isInvisible) {
            Crackiness.Level crackLevel = renderState.crackiness;
            if (crackLevel != Crackiness.Level.NONE) {
                Identifier tex = this.getTextureMap().get(crackLevel);
                renderColoredCutoutModel(this.getParentModel(), tex, matrixStackIn, collector, packedLight, renderState, -1, 1);
            }
        }
    }
    
    protected abstract Map<Crackiness.Level, Identifier> getTextureMap();
}
