package com.verdantartifice.primalmagick.client.renderers.entity.model;

import com.verdantartifice.primalmagick.client.renderers.entity.state.TreefolkRenderState;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkArmPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

/**
 * Definition of a 3D model for a treefolk entity.
 * 
 * @author Daedalus4096
 */
public class TreefolkModel extends HumanoidModel<TreefolkRenderState> {
    private final PartPose bodyDefault = this.body.storePose();
    private final PartPose headDefault = this.head.storePose();
    private final PartPose leftArmDefault = this.leftArm.storePose();
    private final PartPose rightArmDefault = this.rightArm.storePose();

    public TreefolkModel(ModelPart pRoot) {
        super(pRoot);
    }

    @Override
    public void setupAnim(@NotNull TreefolkRenderState state) {
        this.body.loadPose(this.bodyDefault);
        this.head.loadPose(this.headDefault);
        this.leftArm.loadPose(this.leftArmDefault);
        this.rightArm.loadPose(this.rightArmDefault);
        super.setupAnim(state);

        TreefolkArmPose armPose = state.armPose;
        if (armPose == TreefolkArmPose.ADMIRING_ITEM) {
            this.head.xRot = 0.5F;
            this.head.yRot = 0.0F;
            if (state.mainArm == HumanoidArm.LEFT) {
                this.rightArm.yRot = -0.5F;
                this.rightArm.xRot = -0.9F;
            } else {
                this.leftArm.yRot = 0.5F;
                this.leftArm.xRot = -0.9F;
            }
        } else if (armPose == TreefolkArmPose.DANCING) {
            float ageModVal = state.ageInTicks / 60.0F;
            this.head.x = Mth.sin(ageModVal * 10.0F);
            this.head.y = Mth.sin(ageModVal * 40.0F) + 0.4F;
            this.rightArm.xRot = ((float)Math.PI / 180F) * (Mth.sin(ageModVal * 40.0F) * 30.0F);
            this.rightArm.zRot = ((float)Math.PI / 180F) * (40.0F + Mth.cos(ageModVal * 40.0F) * 10.0F);
            this.leftArm.xRot = this.rightArm.xRot * -1.0F;
            this.leftArm.zRot = this.rightArm.zRot * -1.0F;
            this.rightArm.y = Mth.sin(ageModVal * 40.0F) * 0.5F + 1.5F;
            this.leftArm.y = Mth.sin(ageModVal * 40.0F) * 0.5F + 1.5F;
            this.body.y = Mth.sin(ageModVal * 40.0F) * 0.35F;
        }
    }
}
