package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

/**
 * Custom tile entity renderer for auto-charger blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.mana.AutoChargerBlock}
 */
public class AutoChargerTER implements BlockEntityRenderer<AutoChargerTileEntity> {
    public AutoChargerTER(BlockEntityRendererProvider.Context context) {
    }
    
    @Override
    public void render(AutoChargerTileEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack wandStack = tileEntityIn.getSyncedStackInSlot(0).copy();
        if (!wandStack.isEmpty()) {
            // Render the wand in the center of the charger
            wandStack.setCount(1);
            int rot = (int)(tileEntityIn.getLevel().getLevelData().getGameTime() % 360);
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(rot));   // Spin the wand around its Y-axis
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance().getItemRenderer().renderStatic(wandStack, ItemTransforms.TransformType.GUI, combinedLight, combinedOverlay, matrixStack, buffer, 0);
            matrixStack.popPose();
        }
    }
}
