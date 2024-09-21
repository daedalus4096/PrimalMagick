package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunescribingAltarTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Custom tile entity renderer for runescribing altar blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.crafting.RunescribingAltarBlock}
 */
public class RunescribingAltarTER implements BlockEntityRenderer<RunescribingAltarTileEntity> {
    protected ItemStack runeStack = null;
    
    public RunescribingAltarTER(BlockEntityRendererProvider.Context context) {
    }
    
    protected ItemStack getRuneStack() {
        if (this.runeStack == null) {
            this.runeStack = new ItemStack(ItemsPM.RUNE_UNATTUNED.get());
        }
        return this.runeStack;
    }

    @Override
    public void render(RunescribingAltarTileEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack stack = this.getRuneStack();
        if (!stack.isEmpty()) {
            // Render the rune stack above the altar
            int rot = (int)(tileEntityIn.getLevel().getLevelData().getGameTime() % 360);
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1.1D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(rot));   // Spin the stack around its Y-axis
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GUI, combinedLight, combinedOverlay, matrixStack, buffer, tileEntityIn.getLevel(), 0);
            matrixStack.popPose();
        }
    }
}
