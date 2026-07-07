package com.verdantartifice.primalmagick.client.renderers.tile.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class ManaInjectorFrameRingBottomMiddleModel extends AbstractManaInjectorFrameRingModel {
    // Made with Blockbench 4.12.4
    // Exported for Minecraft version 1.17 or later with Mojang mappings

    private final ModelPart bone3;

    public ManaInjectorFrameRingBottomMiddleModel(ModelPart root) {
		super(root);
		this.bone3 = root.getChild("bone3");
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone3 = partdefinition.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(22, 0).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 0).mirror().addBox(-3.0F, -8.0F, 2.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 13).addBox(2.0F, -8.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).mirror().addBox(-3.0F, -8.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
    }

	@Override
	public void setupAnim(@NotNull Double state) {
		super.setupAnim(state);
		this.bone3.y += 0.875F + this.getDipAmount(state, 8);
	}
}
