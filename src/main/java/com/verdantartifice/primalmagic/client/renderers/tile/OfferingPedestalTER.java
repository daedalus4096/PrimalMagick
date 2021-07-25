package com.verdantartifice.primalmagic.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.common.tiles.rituals.OfferingPedestalTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom tile entity renderer for offering pedestal blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.rituals.OfferingPedestalBlock}
 */
@OnlyIn(Dist.CLIENT)
public class OfferingPedestalTER extends BlockEntityRenderer<OfferingPedestalTileEntity> {
    public OfferingPedestalTER(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(OfferingPedestalTileEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack stack = tileEntityIn.getSyncedStackInSlot(0).copy();
        if (!stack.isEmpty()) {
            // Render the held item stack above the pedestal
            int rot = (int)(this.renderer.level.getLevelData().getGameTime() % 360);
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(rot));   // Spin the stack around its Y-axis
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GUI, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.popPose();
        }
    }
}
