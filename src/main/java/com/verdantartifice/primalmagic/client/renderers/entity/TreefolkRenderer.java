package com.verdantartifice.primalmagic.client.renderers.entity;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.misc.TreefolkEntity;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;

/**
 * Entity renderer for a treefolk.
 * 
 * @author Daedalus4096
 */
public class TreefolkRenderer extends BipedRenderer<TreefolkEntity, BipedModel<TreefolkEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/treefolk/treefolk.png");
    protected static final ResourceLocation ANGRY_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/treefolk/treefolk_angry.png");

    public TreefolkRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BipedModel<TreefolkEntity>(0.0F), 0.5F);
    }

    @Override
    public ResourceLocation getEntityTexture(TreefolkEntity entity) {
        return entity.isAngry() ? ANGRY_TEXTURE : TEXTURE;
    }
}
