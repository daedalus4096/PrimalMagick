package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.misc.TreefolkEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Entity renderer for a treefolk.
 * 
 * @author Daedalus4096
 */
public class TreefolkRenderer extends HumanoidMobRenderer<TreefolkEntity, HumanoidModel<TreefolkEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/treefolk/treefolk.png");
    protected static final ResourceLocation ANGRY_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/treefolk/treefolk_angry.png");

    public TreefolkRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<TreefolkEntity>(context.bakeLayer(ModelLayersPM.TREEFOLK)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(TreefolkEntity entity) {
        return entity.isAngry() ? ANGRY_TEXTURE : TEXTURE;
    }
}
