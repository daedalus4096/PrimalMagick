package com.verdantartifice.primalmagic.client.renderers.models;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity model for a spell projectile.  Used by the entity renderer.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.client.renderers.entity.SpellProjectileRenderer}
 */
@OnlyIn(Dist.CLIENT)
public class SpellProjectileModel extends SegmentedModel<SpellProjectileEntity> {
    protected final ModelRenderer renderer;
    
    public SpellProjectileModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        
        // Model is a simple block-sized cube centered at its relative origin
        this.renderer = new ModelRenderer(this);
        this.renderer.setTextureOffset(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
        this.renderer.setRotationPoint(0.0F, 0.0F, 0.0F);
    }
    
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.renderer);
    }

    @Override
    public void setRotationAngles(SpellProjectileEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.renderer.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.renderer.rotateAngleX = headPitch * ((float)Math.PI / 180F);
    }
}
