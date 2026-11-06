package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.util.EntityUtils;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3fc;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Custom item stack renderer for the arcanometer. The base model (chosen by scan state) is selected by the
 * item definition JSON; this renderer only draws the preview of whatever the player is looking at onto the
 * arcanometer's screen.
 *
 * @see com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem
 */
public class ArcanometerSpecialRenderer implements NoDataSpecialModelRenderer {
    private static final AtomicBoolean isRenderingScreen = new AtomicBoolean(false);

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }

        // We might be asked to show another arcanometer on screen; don't recurse in that case
        if (!isRenderingScreen.getAndSet(true)) {
            try {
                // Determine what to show on the screen
                HitResult result = RayTraceUtils.getMouseOver(mc.level, mc.player);
                if (result != null) {
                    if (result.getType() == HitResult.Type.ENTITY) {
                        Entity entity = ((EntityHitResult)result).getEntity();
                        if (entity != null) {
                            ItemStack screenStack = EntityUtils.getEntityItemStack(entity);
                            if (!screenStack.isEmpty()) {
                                this.renderScreenItem(mc, screenStack, poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
                            } else if (Services.PART_ENTITIES.isPartEntity(entity)) {
                                this.renderScreenEntity(mc, Services.PART_ENTITIES.getParent(entity), poseStack, submitNodeCollector);
                            } else {
                                this.renderScreenEntity(mc, entity, poseStack, submitNodeCollector);
                            }
                        }
                    } else if (result.getType() == HitResult.Type.BLOCK) {
                        ItemStack screenStack = new ItemStack(mc.level.getBlockState(((BlockHitResult)result).getBlockPos()).getBlock());
                        this.renderScreenItem(mc, screenStack, poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
                    }
                }
            } finally {
                isRenderingScreen.set(false);
            }
        }
    }

    private void renderScreenItem(Minecraft mc, ItemStack screenStack, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, int outlineColor) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.4375D, 0.405D);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.scale(0.2F, 0.2F, 0.0001F);
        ItemStackRenderState state = new ItemStackRenderState();
        mc.getItemModelResolver().updateForTopItem(state, screenStack, ItemDisplayContext.GUI, mc.level, null, 0);
        state.submit(poseStack, collector, lightCoords, overlayCoords, outlineColor);
        poseStack.popPose();
    }

    private void renderScreenEntity(Minecraft mc, Entity entity, PoseStack poseStack, SubmitNodeCollector collector) {
        float scale = 0.175F;
        float size = Math.max(entity.getBbWidth(), entity.getBbHeight());
        if ((double)size > 1.0D) {
            scale /= size;
        }
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.35D, 0.405D);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.scale(scale, scale, 0.0001F);
        EntityRenderDispatcher erd = mc.getEntityRenderDispatcher();
        EntityRenderState renderState = erd.extractEntity(entity, mc.getDeltaTracker().getGameTimeDeltaPartialTick(true));
        // A default (zeroed) camera state is sufficient for this tiny in-item preview
        erd.submit(renderState, new CameraRenderState(), 0.0D, 0.0D, 0.0D, poseStack, collector);
        poseStack.popPose();
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> consumer) {
        SubModelRenderHelper.unitCubeExtents(consumer);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final ArcanometerSpecialRenderer.Unbaked INSTANCE = new ArcanometerSpecialRenderer.Unbaked();
        public static final MapCodec<ArcanometerSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public ArcanometerSpecialRenderer bake(@NotNull BakingContext bakingContext) {
            return new ArcanometerSpecialRenderer();
        }

        @Override
        @NotNull
        public MapCodec<ArcanometerSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
