package com.verdantartifice.primalmagic.client.renderers.itemstack;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.verdantartifice.primalmagic.common.items.tools.TieredShieldItem;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom item stack renderer for magical metal shields.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.items.tools.TieredShieldItem}
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractTieredShieldISTER extends BlockEntityWithoutLevelRenderer {
    protected final ShieldModel model = new ShieldModel();
    
    public AbstractTieredShieldISTER() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }
    
    @Override
    public void renderByItem(ItemStack stack, TransformType p_239207_2_, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof TieredShieldItem) {
            boolean hasPattern = stack.getTagElement("BlockEntityTag") != null;
            matrixStack.pushPose();
            matrixStack.scale(1.0F, -1.0F, -1.0F);
            Material renderMaterial = this.getRenderMaterial(hasPattern);
            VertexConsumer vertexBuilder = renderMaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(renderMaterial.atlasLocation()), true, stack.hasFoil()));
            this.model.handle().render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            if (hasPattern) {
                List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack));
                BannerRenderer.renderPatterns(matrixStack, buffer, combinedLight, combinedOverlay, this.model.plate(), renderMaterial, false, list, stack.hasFoil());
            } else {
                this.model.plate().render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            matrixStack.popPose();
        }
    }
    
    protected abstract Material getRenderMaterial(boolean hasPattern);
}
