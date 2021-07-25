package com.verdantartifice.primalmagic.client.renderers.entity;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.misc.TreefolkEntity;

import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;

/**
 * Entity renderer for a treefolk.
 * 
 * @author Daedalus4096
 */
public class TreefolkRenderer extends HumanoidMobRenderer<TreefolkEntity, HumanoidModel<TreefolkEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/treefolk/treefolk.png");
    protected static final ResourceLocation ANGRY_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/treefolk/treefolk_angry.png");

    public TreefolkRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new HumanoidModel<TreefolkEntity>(0.0F), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(TreefolkEntity entity) {
        return entity.isAngry() ? ANGRY_TEXTURE : TEXTURE;
    }
}
