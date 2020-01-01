package com.verdantartifice.primalmagic.client.renderers.models;

import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpellMineModel extends EntityModel<SpellMineEntity> {
    protected final RendererModel renderer;
    
    public SpellMineModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.renderer = new RendererModel(this);
        this.renderer.setTextureOffset(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
        this.renderer.setRotationPoint(0.0F, 0.0F, 0.0F);
    }
    
    @Override
    public void render(SpellMineEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.renderer.render(scale);
    }
    
    @Override
    public void setRotationAngles(SpellMineEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        this.renderer.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.renderer.rotateAngleX = headPitch * ((float)Math.PI / 180F);
    }
}
