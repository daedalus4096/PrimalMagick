package com.verdantartifice.primalmagic.client.renderers.models;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;

import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity model for a spell projectile.  Used by the entity renderer.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.client.renderers.entity.SpellProjectileRenderer}
 */
@OnlyIn(Dist.CLIENT)
public class SpellProjectileModel extends ListModel<SpellProjectileEntity> {
    protected final ModelPart renderer;
    
    public SpellProjectileModel() {
        this.texWidth = 64;
        this.texHeight = 32;
        
        // Model is a simple block-sized cube centered at its relative origin
        this.renderer = new ModelPart(this);
        this.renderer.texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
        this.renderer.setPos(0.0F, 0.0F, 0.0F);
    }
    
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(this.renderer);
    }

    @Override
    public void setupAnim(SpellProjectileEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.renderer.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.renderer.xRot = headPitch * ((float)Math.PI / 180F);
    }
}
