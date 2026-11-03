package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.client.renderers.entity.model.TreefolkModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.TreefolkRenderState;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Entity renderer for a treefolk.
 * 
 * @author Daedalus4096
 */
public class TreefolkRenderer extends HumanoidMobRenderer<TreefolkEntity, TreefolkRenderState, TreefolkModel> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/treefolk/treefolk.png");
    protected static final Identifier ANGRY_TEXTURE = ResourceUtils.loc("textures/entity/treefolk/treefolk_angry.png");

    public TreefolkRenderer(EntityRendererProvider.Context context) {
        super(context, new TreefolkModel(context.bakeLayer(ModelLayersPM.TREEFOLK)), 0.5F);
    }

    @Override
    @NotNull
    public TreefolkRenderState createRenderState() {
        return new TreefolkRenderState();
    }

    @Override
    public void extractRenderState(@NotNull TreefolkEntity entity, @NotNull TreefolkRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isAngry = entity.isAngry();
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(@NotNull TreefolkRenderState state) {
        return state.isAngry ? ANGRY_TEXTURE : TEXTURE;
    }
}
