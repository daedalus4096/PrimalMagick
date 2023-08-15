package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.model.TreefolkModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Entity renderer for a treefolk.
 * 
 * @author Daedalus4096
 */
public class TreefolkRenderer extends HumanoidMobRenderer<TreefolkEntity, TreefolkModel<TreefolkEntity>> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/entity/treefolk/treefolk.png");
    protected static final ResourceLocation ANGRY_TEXTURE = PrimalMagick.resource("textures/entity/treefolk/treefolk_angry.png");

    public TreefolkRenderer(EntityRendererProvider.Context context) {
        super(context, new TreefolkModel<TreefolkEntity>(context.bakeLayer(ModelLayersPM.TREEFOLK)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(TreefolkEntity entity) {
        return entity.isAngry() ? ANGRY_TEXTURE : TEXTURE;
    }
}
