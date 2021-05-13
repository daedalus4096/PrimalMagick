package com.verdantartifice.primalmagic.client.renderers.entity.model;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.entities.misc.FlyingCarpetEntity;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Definition of a 3D model for a flying carpet.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class FlyingCarpetModel extends SegmentedModel<FlyingCarpetEntity> {
    protected final ModelRenderer carpet;
    protected final ImmutableList<ModelRenderer> parts;
    
    public FlyingCarpetModel() {
        this.carpet = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
        this.carpet.addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 1.0F, 0.0F);
        this.carpet.setRotationPoint(0.0F, 3.0F, 1.0F);
        this.carpet.rotateAngleX = ((float)Math.PI / 2F);
        this.parts = ImmutableList.of(this.carpet);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return this.parts;
    }

    @Override
    public void setRotationAngles(FlyingCarpetEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Nothing to do here
    }
}
