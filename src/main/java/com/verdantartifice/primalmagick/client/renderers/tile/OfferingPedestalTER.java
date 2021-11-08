package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

/**
 * Custom tile entity renderer for offering pedestal blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.rituals.OfferingPedestalBlock}
 */
public class OfferingPedestalTER implements BlockEntityRenderer<OfferingPedestalTileEntity> {
    public OfferingPedestalTER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(OfferingPedestalTileEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack stack = tileEntityIn.getSyncedStackInSlot(0).copy();
        if (!stack.isEmpty()) {
            // Render the held item stack above the pedestal
            int rot = (int)(tileEntityIn.getLevel().getLevelData().getGameTime() % 360);
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(rot));   // Spin the stack around its Y-axis
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GUI, combinedLight, combinedOverlay, matrixStack, buffer, 0);
            matrixStack.popPose();
        }
    }
}
