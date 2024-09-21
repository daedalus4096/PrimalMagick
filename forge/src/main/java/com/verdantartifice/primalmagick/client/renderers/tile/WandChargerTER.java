package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.tiles.mana.WandChargerTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Custom tile entity renderer for wand charger blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.mana.WandChargerBlock}
 */
public class WandChargerTER implements BlockEntityRenderer<WandChargerTileEntity> {
    public WandChargerTER(BlockEntityRendererProvider.Context context) {
    }
    
    @Override
    public void render(WandChargerTileEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack wandStack = tileEntityIn.getSyncedItem(WandChargerTileEntity.CHARGE_INV_INDEX, 0).copy();
        if (!wandStack.isEmpty()) {
            // Render the wand in the center of the charger
            wandStack.setCount(1);
            int rot = (int)(tileEntityIn.getLevel().getLevelData().getGameTime() % 360);
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(rot));   // Spin the wand around its Y-axis
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance().getItemRenderer().renderStatic(wandStack, ItemDisplayContext.GUI, combinedLight, combinedOverlay, matrixStack, buffer, tileEntityIn.getLevel(), 0);
            matrixStack.popPose();
        }
    }
}
