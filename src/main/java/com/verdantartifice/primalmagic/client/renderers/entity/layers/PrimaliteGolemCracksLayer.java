package com.verdantartifice.primalmagic.client.renderers.entity.layers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.entity.model.PrimaliteGolemModel;
import com.verdantartifice.primalmagic.common.entities.companions.golems.PrimaliteGolemEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * Layer renderer for damage cracks on a primalite golem.
 * 
 * @author Daedalus4096
 */
public class PrimaliteGolemCracksLayer extends LayerRenderer<PrimaliteGolemEntity, PrimaliteGolemModel<PrimaliteGolemEntity>> {
    protected static final Map<PrimaliteGolemEntity.Cracks, ResourceLocation> TEXTURES = ImmutableMap.<PrimaliteGolemEntity.Cracks, ResourceLocation>builder()
            .put(PrimaliteGolemEntity.Cracks.LOW, new ResourceLocation(PrimalMagic.MODID, "textures/entity/golem/primalite_golem_crackiness_low.png"))
            .put(PrimaliteGolemEntity.Cracks.MEDIUM, new ResourceLocation(PrimalMagic.MODID, "textures/entity/golem/primalite_golem_crackiness_medium.png"))
            .put(PrimaliteGolemEntity.Cracks.HIGH, new ResourceLocation(PrimalMagic.MODID, "textures/entity/golem/primalite_golem_crackiness_high.png"))
            .build();
    
    public PrimaliteGolemCracksLayer(IEntityRenderer<PrimaliteGolemEntity, PrimaliteGolemModel<PrimaliteGolemEntity>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, PrimaliteGolemEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isInvisible()) {
            PrimaliteGolemEntity.Cracks cracks = entitylivingbaseIn.getCrackLevel();
            if (cracks != PrimaliteGolemEntity.Cracks.NONE) {
                ResourceLocation tex = TEXTURES.get(cracks);
                renderCutoutModel(this.getEntityModel(), tex, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
