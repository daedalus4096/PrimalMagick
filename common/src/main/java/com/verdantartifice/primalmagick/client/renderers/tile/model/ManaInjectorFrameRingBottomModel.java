package com.verdantartifice.primalmagick.client.renderers.tile.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class ManaInjectorFrameRingBottomModel extends AbstractManaInjectorFrameRingModel {
    // Made with Blockbench 4.12.4
    // Exported for Minecraft version 1.17 or later with Mojang mappings

    private final ModelPart bone4;

    public ManaInjectorFrameRingBottomModel(ModelPart root) {
		super(root);
		this.bone4 = root.getChild("bone4");
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone4 = partdefinition.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(@NotNull Double state) {
        super.setupAnim(state);
        this.bone4.y += 0.375F + this.getDipAmount(state, 12);
    }
}