package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.renderers.entity.layers.InnerDemonArmorLayer;
import com.verdantartifice.primalmagick.common.entities.misc.InnerDemonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import org.jetbrains.annotations.NotNull;

/**
 * Entity renderer for an inner demon.
 * 
 * @author Daedalus4096
 */
public class InnerDemonRenderer extends HumanoidMobRenderer<InnerDemonEntity, AvatarRenderState, PlayerModel> {
    protected static final float SCALE = 2.0F;
    
    protected final EntityRendererProvider.Context context;
    protected InnerDemonArmorLayer armorLayer;
    protected boolean modelFinalized = false;
    
    public InnerDemonRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F * SCALE);
        this.context = context;
        this.armorLayer = new InnerDemonArmorLayer(this, context.getModelSet(), false);
        this.addLayer(this.armorLayer);
    }

    @Override
    @NotNull
    public AvatarRenderState createRenderState() {
        return new AvatarRenderState();
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(@NotNull AvatarRenderState state) {
        // Use the viewing player's skin texture
        Minecraft mc = Minecraft.getInstance();
        return mc.player.getSkin().body().texturePath();
    }

    @Override
    protected void scale(@NotNull AvatarRenderState state, @NotNull PoseStack poseStack) {
        if (!this.modelFinalized) {
            // Can't get the player's skin type at renderer registration time, so monkey-patch it after we're already going
            Minecraft mc = Minecraft.getInstance();
            boolean slimModel = mc.player.getSkin().model().equals(PlayerModelType.SLIM);
            
            this.model = new PlayerModel(this.context.bakeLayer(slimModel ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slimModel);
            
            this.layers.remove(this.armorLayer);
            this.armorLayer = new InnerDemonArmorLayer(this, this.context.getModelSet(), slimModel);
            this.addLayer(this.armorLayer);
            
            this.modelFinalized = true;
        }
        poseStack.scale(SCALE, SCALE, SCALE);
    }

    @Override
    public void submit(@NotNull AvatarRenderState state, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        // FIXME Render beams for each in-range sin crystal? Or is that already being handled by SinCrystalRenderer?
    }
}
