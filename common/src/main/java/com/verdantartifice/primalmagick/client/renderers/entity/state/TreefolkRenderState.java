package com.verdantartifice.primalmagick.client.renderers.entity.state;

import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkArmPose;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class TreefolkRenderState extends HumanoidRenderState {
    public TreefolkArmPose armPose = TreefolkArmPose.DEFAULT;
    public boolean isAngry;
}
