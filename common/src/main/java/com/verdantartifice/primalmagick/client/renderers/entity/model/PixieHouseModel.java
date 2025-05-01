package com.verdantartifice.primalmagick.client.renderers.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.common.entities.misc.PixieHouseEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class PixieHouseModel extends EntityModel<PixieHouseEntity> {
	// Made with Blockbench 4.12.4
	// Exported for Minecraft version 1.17 or later with Mojang mappings

	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceUtils.loc("pixie_house"), "main");

	private final ModelPart trunk;
	private final ModelPart support1;
	private final ModelPart support2;
	private final ModelPart support3;
	private final ModelPart branch1;
	private final ModelPart branch2;
	private final ModelPart house;

	public PixieHouseModel(ModelPart root) {
		this.trunk = root.getChild("trunk");
		this.support1 = root.getChild("support1");
		this.support2 = root.getChild("support2");
		this.support3 = root.getChild("support3");
		this.branch1 = root.getChild("branch1");
		this.branch2 = root.getChild("branch2");
		this.house = root.getChild("house");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition trunk = partdefinition.addOrReplaceChild("trunk", CubeListBuilder.create().texOffs(20, 26).addBox(-2.5F, 2.0F, -2.5F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(8, 36).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 15.0F, 0.5F));

		PartDefinition support1 = partdefinition.addOrReplaceChild("support1", CubeListBuilder.create(), PartPose.offset(-0.8F, 13.0F, -0.5F));

		PartDefinition leaves_r1 = support1.addOrReplaceChild("leaves_r1", CubeListBuilder.create().texOffs(8, 43).addBox(-2.0F, -6.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1F, -6.7F, -4.0F, 0.0F, -0.3491F, 0.0F));

		PartDefinition upper_r1 = support1.addOrReplaceChild("upper_r1", CubeListBuilder.create().texOffs(4, 47).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -6.0F, -3.2F, 0.5064F, 0.9341F, 0.343F));

		PartDefinition mid_r1 = support1.addOrReplaceChild("mid_r1", CubeListBuilder.create().texOffs(36, 45).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, -4.3F, -2.2F, 0.3927F, 1.0472F, 0.0F));

		PartDefinition lower_r1 = support1.addOrReplaceChild("lower_r1", CubeListBuilder.create().texOffs(36, 35).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, 1.0F, 0.5F, 0.7854F, 1.0472F, 0.0F));

		PartDefinition support2 = partdefinition.addOrReplaceChild("support2", CubeListBuilder.create(), PartPose.offset(1.1F, 14.0F, -0.7F));

		PartDefinition leaves_r2 = support2.addOrReplaceChild("leaves_r2", CubeListBuilder.create().texOffs(30, 15).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -14.0F, -2.5F, 0.0F, -1.0472F, 0.0F));

		PartDefinition upper_r2 = support2.addOrReplaceChild("upper_r2", CubeListBuilder.create().texOffs(20, 47).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.6F, -10.0F, -3.2F, 0.0F, 0.5236F, 0.0F));

		PartDefinition mid_r2 = support2.addOrReplaceChild("mid_r2", CubeListBuilder.create().texOffs(44, 35).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6F, -4.1F, -2.0F, 0.1472F, -1.0607F, -0.02F));

		PartDefinition lower_r2 = support2.addOrReplaceChild("lower_r2", CubeListBuilder.create().texOffs(20, 37).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1F, 1.0F, 0.7F, 0.7854F, -1.0472F, 0.0F));

		PartDefinition support3 = partdefinition.addOrReplaceChild("support3", CubeListBuilder.create().texOffs(0, 47).addBox(-0.5F, -18.0F, 4.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(-2.5F, -23.0F, 2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 1.3F));

		PartDefinition mid_r3 = support3.addOrReplaceChild("mid_r3", CubeListBuilder.create().texOffs(0, 36).addBox(-0.91F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, -4.7F, 2.6F, -0.2618F, 0.0F, 0.0F));

		PartDefinition lower_r3 = support3.addOrReplaceChild("lower_r3", CubeListBuilder.create().texOffs(28, 37).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -1.3F, -0.5236F, 0.0F, 0.0F));

		PartDefinition branch1 = partdefinition.addOrReplaceChild("branch1", CubeListBuilder.create().texOffs(36, 23).addBox(-1.5F, -8.0F, -4.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, -1.5F));

		PartDefinition body_r1 = branch1.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(46, 15).addBox(0.0F, -7.0F, -1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.5F, 0.5236F, 0.0F, 0.0F));

		PartDefinition branch2 = partdefinition.addOrReplaceChild("branch2", CubeListBuilder.create().texOffs(36, 29).addBox(1.5F, -10.0F, 1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.2F, 19.0F, 1.4F));

		PartDefinition body_r2 = branch2.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(44, 44).addBox(0.0F, -2.0F, 0.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8F, -6.0F, 2.5F, -0.5236F, 0.7854F, 0.0F));

		PartDefinition house = partdefinition.addOrReplaceChild("house", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 1.0F, -5.5F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(-3.0F, -1.0F, -4.5F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -1.5F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(PixieHouseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		trunk.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		support1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		support2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		support3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		branch1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		branch2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		house.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}