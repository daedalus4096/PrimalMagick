package com.verdantartifice.primalmagick.client.renderers.entity.model;

import com.verdantartifice.primalmagick.client.renderers.entity.state.PixieRenderState;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.AbstractPixieEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

/**
 * Definition of a 3D model for a pixie.
 * 
 * @author Daedalus4096
 */
public class PixieModel extends EntityModel<PixieRenderState> {
    protected final ModelPart head;
    protected final ModelPart body;
    protected final ModelPart rightWing;
    protected final ModelPart leftWing;

    public PixieModel(ModelPart modelPart) {
        super(modelPart);
        this.head = modelPart.getChild("head");
        this.body = modelPart.getChild("body");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
    }
    
    public static LayerDefinition createBodyLayer(boolean includeCrown) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootPart = mesh.getRoot();
        PartDefinition headPart = rootPart.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 1.0F, 0.0F));
        if (includeCrown) {
            PartDefinition crownPart = headPart.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F).texOffs(0, 32).addBox(-1.5F, -4.5F, -3.5F, 3.0F, 1.0F, 1.0F).texOffs(1, 29).addBox(-0.5F, -5.5F, -3.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
            crownPart.addOrReplaceChild("rim4", CubeListBuilder.create().texOffs(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5708F, 0.0F));
            crownPart.addOrReplaceChild("rim3", CubeListBuilder.create().texOffs(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1416F, 0.0F));
            crownPart.addOrReplaceChild("rim2", CubeListBuilder.create().texOffs(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5708F, 0.0F));
        }
        PartDefinition bodyPart = rootPart.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 11.0F, 4.0F), PartPose.offset(0.0F, 4.0F, 0.0F));
        bodyPart.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(22, 15).mirror().addBox(2.0F, -10.0F, 2.5F, 18.0F, 35.0F, 1.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
        bodyPart.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(22, 15).addBox(-20.0F, -10.0F, 2.5F, 18.0F, 35.0F, 1.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
        bodyPart.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(25, 0).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(-4.0F, 0.0F, 0.0F));
        bodyPart.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(25, 0).mirror().addBox(-1.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(4.0F, 0.0F, 0.0F));
        bodyPart.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(38, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F), PartPose.offset(-2.0F, 11.0F, 0.0F));
        bodyPart.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(38, 0).mirror().addBox(-2.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F), PartPose.offset(2.0F, 11.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(@Nullable PixieRenderState renderState) {
        super.setupAnim(renderState);
        this.head.xRot = renderState == null ? 0F : renderState.xRot * ((float)Math.PI / 180F);
        this.head.yRot = renderState == null ? 0F : renderState.yRot * ((float)Math.PI / 180F);
        this.head.zRot = 0.0F;
        if (renderState == null) {
            this.head.setPos(0.0F, 1.0F, 0.0F);
        } else {
            this.head.setPos(0.0F, 0.0F, 0.0F);
        }
        if (renderState == null || renderState.walkAnimationSpeed > 0.0F) {
            this.body.xRot = ((float)Math.PI / 8F);
        }
        this.body.yRot = 0.0F;
        this.rightWing.setPos(0.0F, 0.0F, 0.0F);
        this.leftWing.setPos(0.0F, 0.0F, 0.0F);
        this.rightWing.yRot = renderState == null ? 0F : Mth.cos(renderState.ageInTicks * 1.3F) * (float)Math.PI * 0.25F;
        this.leftWing.yRot = -this.rightWing.yRot;
    }
}
