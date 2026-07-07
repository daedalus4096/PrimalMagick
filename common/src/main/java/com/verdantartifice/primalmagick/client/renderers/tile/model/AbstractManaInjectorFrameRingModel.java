package com.verdantartifice.primalmagick.client.renderers.tile.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;

public abstract class AbstractManaInjectorFrameRingModel extends Model<Double> {
    protected static final String TOP = "top";
    protected static final String TOP_MIDDLE = "top_middle";
    protected static final String BOTTOM_MIDDLE = "bottom_middle";
    protected static final String BOTTOM = "bottom";

    private static final int DIP_DURATION = 8;

    private final ModelPart top;
    private final ModelPart topMiddle;
    private final ModelPart bottomMiddle;
    private final ModelPart bottom;

    public AbstractManaInjectorFrameRingModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
        this.top = root.getChild(TOP);
        this.topMiddle = root.getChild(TOP_MIDDLE);
        this.bottomMiddle = root.getChild(BOTTOM_MIDDLE);
        this.bottom = root.getChild(BOTTOM);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition bone1 = partDefinition.addOrReplaceChild(TOP, CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -16.0F, -7.0F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-7.0F, -16.0F, 6.0F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 3).addBox(6.0F, -16.0F, -6.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).mirror().addBox(-7.0F, -16.0F, -6.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition bone2 = partDefinition.addOrReplaceChild(TOP_MIDDLE, CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.0F, -5.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-5.0F, -12.0F, 4.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 3).addBox(4.0F, -12.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).mirror().addBox(-5.0F, -12.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition bone3 = partDefinition.addOrReplaceChild(BOTTOM_MIDDLE, CubeListBuilder.create().texOffs(22, 0).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).mirror().addBox(-3.0F, -8.0F, 2.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 13).addBox(2.0F, -8.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 13).mirror().addBox(-3.0F, -8.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition bone4 = partDefinition.addOrReplaceChild(BOTTOM, CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void setupAnim(@NotNull Double state) {
        super.setupAnim(state);
        this.top.y += 1.875F + this.getDipAmount(state, 0);
        this.topMiddle.y += 1.375F + this.getDipAmount(state, 4);
        this.bottomMiddle.y += 0.875F + this.getDipAmount(state, 8);
        this.bottom.y += 0.375F + this.getDipAmount(state, 12);
    }

    protected float getDipAmount(double cycleTime, int dipStartTime) {
        if (cycleTime >= dipStartTime && cycleTime <= (dipStartTime + DIP_DURATION)) {
            return -0.125F * Mth.sin((cycleTime - dipStartTime) * (Math.PI / (double)DIP_DURATION));
        } else {
            return 0F;
        }
    }
}
