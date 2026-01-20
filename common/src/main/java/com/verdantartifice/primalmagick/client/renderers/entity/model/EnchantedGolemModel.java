package com.verdantartifice.primalmagick.client.renderers.entity.model;

import com.verdantartifice.primalmagick.client.renderers.entity.state.EnchantedGolemRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Definition of a 3D model for a primalite golem.
 * 
 * @author Daedalus4096
 */
public class EnchantedGolemModel extends EntityModel<EnchantedGolemRenderState> {
    protected final ModelPart golemHead;
    protected final ModelPart golemRightArm;
    protected final ModelPart golemLeftArm;
    protected final ModelPart golemLeftLeg;
    protected final ModelPart golemRightLeg;
    
    public EnchantedGolemModel(ModelPart modelPart) {
        super(modelPart);
        this.golemHead = modelPart.getChild("head");
        this.golemRightArm = modelPart.getChild("right_arm");
        this.golemLeftArm = modelPart.getChild("left_arm");
        this.golemRightLeg = modelPart.getChild("right_leg");
        this.golemLeftLeg = modelPart.getChild("left_leg");
    }

    @Override
    public void setupAnim(EnchantedGolemRenderState state) {
        float attackTimer = state.attackTicksRemaining;
        float limbSwingSpeed = state.walkAnimationSpeed;
        float limbSwing = state.walkAnimationPos;

        if (attackTimer > 0F) {
            this.golemRightArm.xRot = -2.0F + 1.5F * Mth.triangleWave(attackTimer, 10.0F);
            this.golemLeftArm.xRot = -2.0F + 1.5F * Mth.triangleWave(attackTimer, 10.0F);
        } else {
            this.golemRightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * limbSwingSpeed;
            this.golemLeftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * limbSwingSpeed;
        }

        this.golemHead.yRot = state.yRot * ((float)Math.PI / 180F);
        this.golemHead.xRot = state.xRot * ((float)Math.PI / 180F);
        this.golemLeftLeg.xRot = -1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingSpeed;
        this.golemRightLeg.xRot = 1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingSpeed;
        this.golemLeftLeg.yRot = 0.0F;
        this.golemRightLeg.yRot = 0.0F;
    }
}
