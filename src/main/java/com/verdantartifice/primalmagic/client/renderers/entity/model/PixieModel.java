package com.verdantartifice.primalmagic.client.renderers.entity.model;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.AbstractPixieEntity;

import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Definition of a 3D model for a basic earth pixie.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class PixieModel extends ListModel<AbstractPixieEntity> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public PixieModel(boolean showCrown) {
        this.texWidth = 64;
        this.texHeight = 64;

        this.head = new ModelPart(this);
        this.head.setPos(0.0F, 1.0F, 0.0F);
        this.head.texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, true);
        
        if (showCrown) {
            ModelPart crown = new ModelPart(this);
            crown.setPos(0.0F, 0.0F, 0.0F);
            this.head.addChild(crown);
            crown.texOffs(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);
            crown.texOffs(0, 32).addBox(-1.5F, -4.5F, -3.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            crown.texOffs(1, 29).addBox(-0.5F, -5.5F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelPart rim4 = new ModelPart(this);
            rim4.setPos(0.0F, 0.0F, 0.0F);
            crown.addChild(rim4);
            rim4.yRot = -1.5708F;
            rim4.texOffs(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);

            ModelPart rim3 = new ModelPart(this);
            rim3.setPos(0.0F, 0.0F, 0.0F);
            crown.addChild(rim3);
            rim3.yRot = 3.1416F;
            rim3.texOffs(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);

            ModelPart rim2 = new ModelPart(this);
            rim2.setPos(0.0F, 0.0F, 0.0F);
            crown.addChild(rim2);
            rim2.yRot = 1.5708F;
            rim2.texOffs(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        }

        this.body = new ModelPart(this);
        this.body.setPos(0.0F, 4.0F, 0.0F);
        this.body.texOffs(0, 13).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 11.0F, 4.0F, 0.0F, true);

        this.rightWing = new ModelPart(this);
        this.rightWing.setPos(0.0F, -4.0F, 0.0F);
        this.body.addChild(this.rightWing);
        this.rightWing.texOffs(22, 15).addBox(2.0F, -10.0F, 2.5F, 18.0F, 35.0F, 1.0F, 0.0F, true);

        this.leftWing = new ModelPart(this);
        this.leftWing.setPos(0.0F, -4.0F, 0.0F);
        this.body.addChild(this.leftWing);
        this.leftWing.texOffs(22, 15).addBox(-20.0F, -10.0F, 2.5F, 18.0F, 35.0F, 1.0F, 0.0F, false);

        this.leftArm = new ModelPart(this);
        this.leftArm.setPos(-4.0F, 0.0F, 0.0F);
        this.body.addChild(this.leftArm);
        this.leftArm.texOffs(25, 0).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, 0.0F, false);

        this.rightArm = new ModelPart(this);
        this.rightArm.setPos(4.0F, 0.0F, 0.0F);
        this.body.addChild(this.rightArm);
        this.rightArm.texOffs(25, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, 0.0F, true);

        this.leftLeg = new ModelPart(this);
        this.leftLeg.setPos(-2.0F, 11.0F, 0.0F);
        this.body.addChild(this.leftLeg);
        this.leftLeg.texOffs(38, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        this.rightLeg = new ModelPart(this);
        this.rightLeg.setPos(2.0F, 11.0F, 0.0F);
        this.body.addChild(this.rightLeg);
        this.rightLeg.texOffs(38, 0).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, true);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(this.head, this.body);
    }

    @Override
    public void setupAnim(AbstractPixieEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.zRot = 0.0F;
        this.head.setPos(0.0F, 0.0F, 0.0F);
        if (entityIn.getDeltaMovement().lengthSqr() > 0.0F) {
            this.body.xRot = ((float)Math.PI / 8F);
        }
        this.body.yRot = 0.0F;
        this.rightWing.setPos(0.0F, 0.0F, 0.0F);
        this.leftWing.setPos(0.0F, 0.0F, 0.0F);
        this.rightWing.yRot = Mth.cos(ageInTicks * 1.3F) * (float)Math.PI * 0.25F;
        this.leftWing.yRot = -this.rightWing.yRot;
    }
}
