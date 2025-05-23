package com.verdantartifice.primalmagick.client.renderers.itemstack.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

/**
 * Definition of a 3D model of the core of an archmage-grade mana orb.
 *
 * @author Daedalus4096
 */
public class ManaOrbArchmageModel extends Model {
	// Made with Blockbench 4.12.4
	// Exported for Minecraft version 1.17 or later with Mojang mappings

	private final ModelPart bb_main;

	public ManaOrbArchmageModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-1.5F, -5.0F, -0.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 10).addBox(-0.5F, -7.0F, -0.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 10).addBox(-1.5F, -4.0F, -1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 6).addBox(-0.5F, -5.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 8).addBox(-1.5F, -6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 0).addBox(0.5F, -6.0F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 2).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}