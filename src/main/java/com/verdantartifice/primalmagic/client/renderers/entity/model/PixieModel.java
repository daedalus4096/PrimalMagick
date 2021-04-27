package com.verdantartifice.primalmagic.client.renderers.entity.model;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.AbstractPixieEntity;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Definition of a 3D model for a basic earth pixie.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class PixieModel extends SegmentedModel<AbstractPixieEntity> {
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer rightWing;
    private final ModelRenderer leftWing;
    private final ModelRenderer leftArm;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightLeg;

    public PixieModel(boolean showCrown) {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.head = new ModelRenderer(this);
        this.head.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.head.setTextureOffset(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, true);
        
        if (showCrown) {
            ModelRenderer crown = new ModelRenderer(this);
            crown.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.head.addChild(crown);
            crown.setTextureOffset(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);
            crown.setTextureOffset(0, 32).addBox(-1.5F, -4.5F, -3.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            crown.setTextureOffset(1, 29).addBox(-0.5F, -5.5F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer rim4 = new ModelRenderer(this);
            rim4.setRotationPoint(0.0F, 0.0F, 0.0F);
            crown.addChild(rim4);
            rim4.rotateAngleY = -1.5708F;
            rim4.setTextureOffset(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer rim3 = new ModelRenderer(this);
            rim3.setRotationPoint(0.0F, 0.0F, 0.0F);
            crown.addChild(rim3);
            rim3.rotateAngleY = 3.1416F;
            rim3.setTextureOffset(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer rim2 = new ModelRenderer(this);
            rim2.setRotationPoint(0.0F, 0.0F, 0.0F);
            crown.addChild(rim2);
            rim2.rotateAngleY = 1.5708F;
            rim2.setTextureOffset(0, 29).addBox(-3.5F, -3.5F, -3.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        }

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.body.setTextureOffset(0, 13).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 11.0F, 4.0F, 0.0F, true);

        this.rightWing = new ModelRenderer(this);
        this.rightWing.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.body.addChild(this.rightWing);
        this.rightWing.setTextureOffset(22, 15).addBox(2.0F, -10.0F, 2.5F, 18.0F, 35.0F, 1.0F, 0.0F, true);

        this.leftWing = new ModelRenderer(this);
        this.leftWing.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.body.addChild(this.leftWing);
        this.leftWing.setTextureOffset(22, 15).addBox(-20.0F, -10.0F, 2.5F, 18.0F, 35.0F, 1.0F, 0.0F, false);

        this.leftArm = new ModelRenderer(this);
        this.leftArm.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.body.addChild(this.leftArm);
        this.leftArm.setTextureOffset(25, 0).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, 0.0F, false);

        this.rightArm = new ModelRenderer(this);
        this.rightArm.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.body.addChild(this.rightArm);
        this.rightArm.setTextureOffset(25, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, 0.0F, true);

        this.leftLeg = new ModelRenderer(this);
        this.leftLeg.setRotationPoint(-2.0F, 11.0F, 0.0F);
        this.body.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(38, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        this.rightLeg = new ModelRenderer(this);
        this.rightLeg.setRotationPoint(2.0F, 11.0F, 0.0F);
        this.body.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(38, 0).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, true);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.head, this.body);
    }

    @Override
    public void setRotationAngles(AbstractPixieEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.head.rotateAngleZ = 0.0F;
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        if (entityIn.getMotion().lengthSquared() > 0.0F) {
            this.body.rotateAngleX = ((float)Math.PI / 8F);
        }
        this.body.rotateAngleY = 0.0F;
        this.rightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightWing.rotateAngleY = MathHelper.cos(ageInTicks * 1.3F) * (float)Math.PI * 0.25F;
        this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
    }
}
