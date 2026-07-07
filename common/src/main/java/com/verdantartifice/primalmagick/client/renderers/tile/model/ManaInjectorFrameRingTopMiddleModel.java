package com.verdantartifice.primalmagick.client.renderers.tile.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class ManaInjectorFrameRingTopMiddleModel extends AbstractManaInjectorFrameRingModel {
    // Made with Blockbench 4.12.4
    // Exported for Minecraft version 1.17 or later with Mojang mappings

    private final ModelPart bone2;

    public ManaInjectorFrameRingTopMiddleModel(ModelPart root) {
		super(root);
		this.bone2 = root.getChild("bone2");
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.0F, -5.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-5.0F, -12.0F, 4.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 3).addBox(4.0F, -12.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).mirror().addBox(-5.0F, -12.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
    }

	@Override
	public void setupAnim(@NotNull Double state) {
		super.setupAnim(state);
		this.bone2.y += 1.375F + this.getDipAmount(state, 4);
	}
}
