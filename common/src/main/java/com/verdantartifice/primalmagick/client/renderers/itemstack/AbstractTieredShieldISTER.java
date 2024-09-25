package com.verdantartifice.primalmagick.client.renderers.itemstack;

import java.util.Objects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.common.items.tools.AbstractTieredShieldItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

/**
 * Custom item stack renderer for magickal metal shields.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.items.tools.AbstractTieredShieldItem}
 */
public abstract class AbstractTieredShieldISTER extends BlockEntityWithoutLevelRenderer {
    protected ShieldModel model;
    
    public AbstractTieredShieldISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }
    
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.model = new ShieldModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.SHIELD));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof AbstractTieredShieldItem) {
            BannerPatternLayers layers = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
            DyeColor dyeColor = stack.get(DataComponents.BASE_COLOR);
            boolean hasPattern = !layers.layers().isEmpty() || dyeColor != null;
            poseStack.pushPose();
            poseStack.scale(1.0F, -1.0F, -1.0F);
            Material renderMaterial = this.getRenderMaterial(hasPattern);
            VertexConsumer vertexBuilder = renderMaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(renderMaterial.atlasLocation()), true, stack.hasFoil()));
            this.model.handle().render(poseStack, vertexBuilder, combinedLight, combinedOverlay, -1);
            if (hasPattern) {
                BannerRenderer.renderPatterns(poseStack, buffer, combinedLight, combinedOverlay, this.model.plate(), renderMaterial, false, Objects.requireNonNullElse(dyeColor, DyeColor.WHITE), layers, stack.hasFoil());
            } else {
                this.model.plate().render(poseStack, vertexBuilder, combinedLight, combinedOverlay, -1);
            }
            poseStack.popPose();
        }
    }
    
    protected abstract Material getRenderMaterial(boolean hasPattern);
}
