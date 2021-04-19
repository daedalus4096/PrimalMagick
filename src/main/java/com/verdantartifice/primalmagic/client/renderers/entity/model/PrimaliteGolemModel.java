package com.verdantartifice.primalmagic.client.renderers.entity.model;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.entities.companions.PrimaliteGolemEntity;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Definition of a 3D model for a primalite golem.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class PrimaliteGolemModel<T extends PrimaliteGolemEntity> extends SegmentedModel<T> {
    protected final ModelRenderer golemHead;
    protected final ModelRenderer golemBody;
    protected final ModelRenderer golemRightArm;
    protected final ModelRenderer golemLeftArm;
    protected final ModelRenderer golemLeftLeg;
    protected final ModelRenderer golemRightLeg;
    
    public PrimaliteGolemModel() {
        this.golemHead = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.golemHead.setRotationPoint(0.0F, -7.0F, -2.0F);
        this.golemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F);
        this.golemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, 0.0F);
        this.golemBody = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.golemBody.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.golemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F, 0.0F);
        this.golemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, 0.5F);
        this.golemRightArm = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.golemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.golemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F);
        this.golemLeftArm = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.golemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.golemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F);
        this.golemLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
        this.golemLeftLeg.setRotationPoint(-4.0F, 11.0F, 0.0F);
        this.golemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F);
        this.golemRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
        this.golemRightLeg.mirror = true;
        this.golemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 11.0F, 0.0F);
        this.golemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.golemHead, this.golemBody, this.golemLeftLeg, this.golemRightLeg, this.golemRightArm, this.golemLeftArm);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.golemHead.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.golemHead.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.golemLeftLeg.rotateAngleX = -1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F) * limbSwingAmount;
        this.golemRightLeg.rotateAngleX = 1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F) * limbSwingAmount;
        this.golemLeftLeg.rotateAngleY = 0.0F;
        this.golemRightLeg.rotateAngleY = 0.0F;
    }

    @Override
    public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        int timer = entityIn.getAttackTimer();
        if (timer > 0) {
            this.golemRightArm.rotateAngleX = -2.0F + 1.5F * MathHelper.func_233021_e_((float)timer - partialTick, 10.0F);
            this.golemLeftArm.rotateAngleX = -2.0F + 1.5F * MathHelper.func_233021_e_((float)timer - partialTick, 10.0F);
        } else {
            this.golemRightArm.rotateAngleX = (-0.2F + 1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F)) * limbSwingAmount;
            this.golemLeftArm.rotateAngleX = (-0.2F - 1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F)) * limbSwingAmount;
        }
    }
}
