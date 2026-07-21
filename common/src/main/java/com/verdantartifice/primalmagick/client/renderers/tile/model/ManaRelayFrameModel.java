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
import net.minecraft.util.Unit;

/**
 * Definition of the 3D model of the frame of a mana relay.
 *
 * @author Daedalus4096
 */
public class ManaRelayFrameModel extends Model<Unit> {
    // Made with Blockbench 4.12.4
    // Exported for Minecraft version 1.17 or later with Mojang mappings

    public ManaRelayFrameModel(ModelPart root) {
		super(root, RenderTypes::entitySolid);
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).addBox(-6.0F, 4.0F, -6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-6.0F, -4.0F, -6.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 8).addBox(4.0F, -4.0F, -6.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, 4.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).addBox(-6.0F, -6.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(4.0F, -4.0F, 4.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 8).addBox(-6.0F, -4.0F, 4.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(4.0F, -6.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(20, 18).addBox(4.0F, 4.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-6.0F, 4.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(20, 18).addBox(-6.0F, -6.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
    }
}