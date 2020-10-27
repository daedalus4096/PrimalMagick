package com.verdantartifice.primalmagic.client.renderers.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.common.tiles.mana.WandChargerTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom tile entity renderer for wand charger blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.mana.WandChargerBlock}
 */
@OnlyIn(Dist.CLIENT)
public class WandChargerTER extends TileEntityRenderer<WandChargerTileEntity> {
    public WandChargerTER(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }
    
    @Override
    public void render(WandChargerTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ItemStack wandStack = tileEntityIn.getSyncedStackInSlot(1).copy();
        if (!wandStack.isEmpty()) {
            // Render the wand in the center of the charger
            wandStack.setCount(1);
            int rot = (int)(this.renderDispatcher.world.getWorldInfo().getGameTime() % 360);
            matrixStack.push();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(rot));   // Spin the wand around its Y-axis
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance().getItemRenderer().renderItem(wandStack, ItemCameraTransforms.TransformType.GUI, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.pop();
        }
    }
}
