package com.verdantartifice.primalmagic.client.renderers.entity.model;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicEarthPixieEntity;

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
public class BasicEarthPixieModel extends SegmentedModel<BasicEarthPixieEntity> {
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer rightWing;
    private final ModelRenderer leftWing;
    private final ModelRenderer leftArm;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightLeg;

    public BasicEarthPixieModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.head = new ModelRenderer(this);
        this.head.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.head.setTextureOffset(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, true);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.body.setTextureOffset(0, 13).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 11.0F, 4.0F, 0.0F, true);

        this.rightWing = new ModelRenderer(this);
        this.rightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightWing.setTextureOffset(22, 15).addBox(2.0F, -10.0F, 2.5F, 18.0F, 35.0F, 1.0F, 0.0F, true);

        this.leftWing = new ModelRenderer(this);
        this.leftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftWing.setTextureOffset(22, 15).addBox(-20.0F, -10.0F, 2.5F, 18.0F, 35.0F, 1.0F, 0.0F, false);

        this.leftArm = new ModelRenderer(this);
        this.leftArm.setRotationPoint(-4.0F, 4.0F, 0.0F);
        this.leftArm.setTextureOffset(25, 0).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, 0.0F, false);

        this.rightArm = new ModelRenderer(this);
        this.rightArm.setRotationPoint(4.0F, 4.0F, 0.0F);
        this.rightArm.setTextureOffset(25, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, 0.0F, true);

        this.leftLeg = new ModelRenderer(this);
        this.leftLeg.setRotationPoint(-2.0F, 15.0F, 0.0F);
        this.leftLeg.setTextureOffset(38, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        this.rightLeg = new ModelRenderer(this);
        this.rightLeg.setRotationPoint(2.0F, 15.0F, 0.0F);
        this.rightLeg.setTextureOffset(38, 0).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, true);
        
        this.body.addChild(this.rightWing);
        this.body.addChild(this.leftWing);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
    }

    @Override
    public void setRotationAngles(BasicEarthPixieEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.head.rotateAngleZ = 0.0F;
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.rotateAngleY = 0.0F;
        this.rightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightWing.rotateAngleY = MathHelper.cos(ageInTicks * 1.3F) * (float)Math.PI * 0.25F;
        this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
    }
}
