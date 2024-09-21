package com.verdantartifice.primalmagick.client.renderers.entity.model;

import com.verdantartifice.primalmagick.common.entities.companions.golems.AbstractEnchantedGolemEntity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Definition of a 3D model for a primalite golem.
 * 
 * @author Daedalus4096
 */
public class EnchantedGolemModel<T extends AbstractEnchantedGolemEntity> extends HierarchicalModel<T> {
    protected final ModelPart root;
    protected final ModelPart golemHead;
    protected final ModelPart golemRightArm;
    protected final ModelPart golemLeftArm;
    protected final ModelPart golemLeftLeg;
    protected final ModelPart golemRightLeg;
    
    public EnchantedGolemModel(ModelPart modelPart) {
        this.root = modelPart;
        this.golemHead = modelPart.getChild("head");
        this.golemRightArm = modelPart.getChild("right_arm");
        this.golemLeftArm = modelPart.getChild("left_arm");
        this.golemRightLeg = modelPart.getChild("right_leg");
        this.golemLeftLeg = modelPart.getChild("left_leg");
    }

    @Override
    public ModelPart root() {
        return this.root;
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
