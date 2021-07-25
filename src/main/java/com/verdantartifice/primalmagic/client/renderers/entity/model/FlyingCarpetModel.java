package com.verdantartifice.primalmagic.client.renderers.entity.model;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.entities.misc.FlyingCarpetEntity;

import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Definition of a 3D model for a flying carpet.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class FlyingCarpetModel extends ListModel<FlyingCarpetEntity> {
    protected final ModelPart carpet;
    protected final ImmutableList<ModelPart> parts;
    
    public FlyingCarpetModel() {
        this.carpet = (new ModelPart(this, 0, 0)).setTexSize(128, 64);
        this.carpet.addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 1.0F, 0.0F);
        this.carpet.setPos(0.0F, 3.0F, 1.0F);
        this.carpet.xRot = ((float)Math.PI / 2F);
        this.parts = ImmutableList.of(this.carpet);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return this.parts;
    }

    @Override
    public void setupAnim(FlyingCarpetEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Nothing to do here
    }
}
