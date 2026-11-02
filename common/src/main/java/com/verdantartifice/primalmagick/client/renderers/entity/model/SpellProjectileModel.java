package com.verdantartifice.primalmagick.client.renderers.entity.model;

import com.verdantartifice.primalmagick.client.renderers.entity.state.SpellProjectileRenderState;
import net.minecraft.client.model.EntityModel;
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
 * @see com.verdantartifice.primalmagick.client.renderers.entity.SpellProjectileRenderer
 */
public class SpellProjectileModel extends EntityModel<SpellProjectileRenderState> {
    protected final ModelPart body;
    
    public SpellProjectileModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootPart = mesh.getRoot();
        rootPart.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }
    
    @Override
    public void setupAnim(SpellProjectileRenderState state) {
        this.root.yRot = state.yRot * ((float)Math.PI / 180F);
        this.root.xRot = state.xRot * ((float)Math.PI / 180F);
    }
}
