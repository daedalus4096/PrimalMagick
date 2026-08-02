package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.common.entities.misc.FriendlyWitchEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.witch.WitchModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.WitchItemLayer;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.client.renderer.entity.state.WitchRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

/**
 * Entity renderer for a friendly witch.
 * 
 * @author Daedalus4096
 */
public class FriendlyWitchRenderer extends MobRenderer<FriendlyWitchEntity, WitchRenderState, WitchModel> {
    private static final Identifier WITCH_LOCATION = Identifier.withDefaultNamespace("textures/entity/witch.png");

    public FriendlyWitchRenderer(EntityRendererProvider.Context context) {
        super(context, new WitchModel(context.bakeLayer(ModelLayers.WITCH)), 0.5F);
        this.addLayer(new WitchItemLayer(this));
    }

    @Override
    @NotNull
    public WitchRenderState createRenderState() {
        return new WitchRenderState();
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(@NotNull WitchRenderState witchRenderState) {
        return WITCH_LOCATION;
    }

    @Override
    public void extractRenderState(@NotNull FriendlyWitchEntity entity, @NotNull WitchRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HoldingEntityRenderState.extractHoldingEntityRenderState(entity, state, this.itemModelResolver);
        state.entityId = entity.getId();
        ItemStack mainHandItem = entity.getMainHandItem();
        state.isHoldingItem = !mainHandItem.isEmpty();
        state.isHoldingPotion = mainHandItem.is(Items.POTION);
    }
}
