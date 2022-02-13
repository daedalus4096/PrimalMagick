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

/**
 * Definition of a 3D model for the ring hovering over a spellcrafting altar.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarRingModel extends Model {
    private final ModelPart ring;
    
    public SpellcraftingAltarRingModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.ring = root.getChild("ring");
    }
    
    public static LayerDefinition createBodyLayer() {
        // Made with Blockbench 4.1.4
        // Exported for Minecraft version 1.17 with Mojang mappings
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition ring = partdefinition.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -7.2426F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
        .texOffs(0, 17).addBox(4.2426F, -4.0F, -3.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
        .texOffs(18, 10).addBox(-3.0F, -4.0F, 4.2427F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
        .texOffs(36, 0).addBox(-7.2426F, -4.0F, -3.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        ring.addOrReplaceChild("segment8_r1", CubeListBuilder.create().texOffs(36, 10).addBox(-3.0F, -4.01F, -7.2426F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
        .texOffs(0, 7).addBox(4.2426F, -4.01F, -3.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ring.addOrReplaceChild("segment6_r1", CubeListBuilder.create().texOffs(18, 17).addBox(-3.0F, -4.01F, 4.2427F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
        .texOffs(18, 0).addBox(4.2426F, -4.01F, -3.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }
    
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Made with Blockbench 4.1.4
        // Exported for Minecraft version 1.17 with Mojang mappings
        this.ring.render(poseStack, buffer, packedLight, packedOverlay);
    }

}
