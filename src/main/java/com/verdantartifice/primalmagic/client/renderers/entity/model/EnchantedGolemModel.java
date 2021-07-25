package com.verdantartifice.primalmagic.client.renderers.entity.model;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.entities.companions.golems.AbstractEnchantedGolemEntity;

import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Definition of a 3D model for a primalite golem.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class EnchantedGolemModel<T extends AbstractEnchantedGolemEntity> extends ListModel<T> {
    protected final ModelPart golemHead;
    protected final ModelPart golemBody;
    protected final ModelPart golemRightArm;
    protected final ModelPart golemLeftArm;
    protected final ModelPart golemLeftLeg;
    protected final ModelPart golemRightLeg;
    
    public EnchantedGolemModel() {
        this.golemHead = (new ModelPart(this)).setTexSize(128, 128);
        this.golemHead.setPos(0.0F, -7.0F, -2.0F);
        this.golemHead.texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F);
        this.golemHead.texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, 0.0F);
        this.golemBody = (new ModelPart(this)).setTexSize(128, 128);
        this.golemBody.setPos(0.0F, -7.0F, 0.0F);
        this.golemBody.texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F, 0.0F);
        this.golemBody.texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, 0.5F);
        this.golemRightArm = (new ModelPart(this)).setTexSize(128, 128);
        this.golemRightArm.setPos(0.0F, -7.0F, 0.0F);
        this.golemRightArm.texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F);
        this.golemLeftArm = (new ModelPart(this)).setTexSize(128, 128);
        this.golemLeftArm.setPos(0.0F, -7.0F, 0.0F);
        this.golemLeftArm.texOffs(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F);
        this.golemLeftLeg = (new ModelPart(this, 0, 22)).setTexSize(128, 128);
        this.golemLeftLeg.setPos(-4.0F, 11.0F, 0.0F);
        this.golemLeftLeg.texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F);
        this.golemRightLeg = (new ModelPart(this, 0, 22)).setTexSize(128, 128);
        this.golemRightLeg.mirror = true;
        this.golemRightLeg.texOffs(60, 0).setPos(5.0F, 11.0F, 0.0F);
        this.golemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(this.golemHead, this.golemBody, this.golemLeftLeg, this.golemRightLeg, this.golemRightArm, this.golemLeftArm);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.golemHead.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.golemHead.xRot = headPitch * ((float)Math.PI / 180F);
        this.golemLeftLeg.xRot = -1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.golemRightLeg.xRot = 1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.golemLeftLeg.yRot = 0.0F;
        this.golemRightLeg.yRot = 0.0F;
    }

    @Override
    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        int timer = entityIn.getAttackTimer();
        if (timer > 0) {
            this.golemRightArm.xRot = -2.0F + 1.5F * Mth.triangleWave((float)timer - partialTick, 10.0F);
            this.golemLeftArm.xRot = -2.0F + 1.5F * Mth.triangleWave((float)timer - partialTick, 10.0F);
        } else {
            this.golemRightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
            this.golemLeftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
        }
    }
}
