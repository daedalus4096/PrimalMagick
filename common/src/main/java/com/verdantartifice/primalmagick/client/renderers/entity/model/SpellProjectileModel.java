package com.verdantartifice.primalmagick.client.renderers.entity.model;

import com.verdantartifice.primalmagick.common.entities.projectiles.SpellProjectileEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Entity model for a spell projectile.  Used by the entity renderer.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.client.renderers.entity.SpellProjectileRenderer}
 */
public class SpellProjectileModel extends HierarchicalModel<SpellProjectileEntity> {
    protected final ModelPart root;
    
    public SpellProjectileModel(ModelPart modelPart) {
        this.root = modelPart;
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootPart = mesh.getRoot();
        rootPart.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }
    
    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(SpellProjectileEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.root.xRot = headPitch * ((float)Math.PI / 180F);
    }
}
