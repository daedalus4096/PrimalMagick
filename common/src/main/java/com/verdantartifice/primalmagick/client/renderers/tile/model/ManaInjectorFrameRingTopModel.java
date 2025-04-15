package com.verdantartifice.primalmagick.client.renderers.tile.model;

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

public class ManaInjectorFrameRingTopModel extends Model {
  // Made with Blockbench 4.12.4
  // Exported for Minecraft version 1.17 or later with Mojang mappings

  private final ModelPart bone1;

  public ManaInjectorFrameRingTopModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.bone1 = root.getChild("bone1");
  }

  public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone1 = partdefinition.addOrReplaceChild("bone1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -16.0F, -7.0F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-7.0F, -16.0F, 6.0F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 3).addBox(6.0F, -16.0F, -6.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).mirror().addBox(-7.0F, -16.0F, -6.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
  }

  @Override
  public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		bone1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
  }
}
