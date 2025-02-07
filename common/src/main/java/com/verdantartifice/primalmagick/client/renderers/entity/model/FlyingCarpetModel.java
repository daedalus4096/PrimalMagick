package com.verdantartifice.primalmagick.client.renderers.entity.model;

import com.verdantartifice.primalmagick.common.entities.misc.FlyingCarpetEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Definition of a 3D model for a flying carpet.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetModel extends HierarchicalModel<FlyingCarpetEntity> {
    protected final ModelPart root;
    
    public FlyingCarpetModel(ModelPart modelPart) {
        this.root = modelPart;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootPart = mesh.getRoot();
        rootPart.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, (float)Math.PI / 2F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 64);
    }
    
    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(FlyingCarpetEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Nothing to do here
    }
}
