package com.verdantartifice.primalmagick.client.renderers.tile.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import org.joml.Math;

public abstract class AbstractManaInjectorFrameRingModel extends Model<Double> {
    private static final int DIP_DURATION = 8;

    public AbstractManaInjectorFrameRingModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
    }

    protected float getDipAmount(double cycleTime, int dipStartTime) {
        if (cycleTime >= dipStartTime && cycleTime <= (dipStartTime + DIP_DURATION)) {
            return -0.125F * Mth.sin((cycleTime - dipStartTime) * (Math.PI / (double)DIP_DURATION));
        } else {
            return 0F;
        }
    }
}
