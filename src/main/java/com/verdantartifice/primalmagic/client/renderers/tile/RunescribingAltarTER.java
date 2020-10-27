package com.verdantartifice.primalmagic.client.renderers.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.tiles.crafting.RunescribingAltarTileEntity;

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
 * Custom tile entity renderer for runescribing altar blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.crafting.RunescribingAltarBlock}
 */
@OnlyIn(Dist.CLIENT)
public class RunescribingAltarTER extends TileEntityRenderer<RunescribingAltarTileEntity> {
    protected ItemStack runeStack = null;
    
    public RunescribingAltarTER(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }
    
    protected ItemStack getRuneStack() {
        if (this.runeStack == null) {
            this.runeStack = new ItemStack(ItemsPM.RUNE_UNATTUNED.get());
        }
        return this.runeStack;
    }

    @Override
    public void render(RunescribingAltarTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ItemStack stack = this.getRuneStack();
        if (!stack.isEmpty()) {
            // Render the rune stack above the altar
            int rot = (int)(this.renderDispatcher.world.getWorldInfo().getGameTime() % 360);
            matrixStack.push();
            matrixStack.translate(0.5D, 1.1D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(rot));   // Spin the stack around its Y-axis
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GUI, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.pop();
        }
    }
}
