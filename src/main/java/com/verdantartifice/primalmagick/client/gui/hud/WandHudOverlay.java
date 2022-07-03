package com.verdantartifice.primalmagick.client.gui.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

/**
 * HUD overlay to show wand mana levels.
 * 
 * @author Daedalus4096
 */
public class WandHudOverlay implements IIngameOverlay {
    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && !mc.player.isSpectator()) {
            ItemStack stack = mc.player.getItemBySlot(EquipmentSlot.MAINHAND);
            if (stack.getItem() instanceof IWand wand) {
                this.renderHud(poseStack, stack, wand, partialTick);
            }
        }
    }

    private void renderHud(PoseStack poseStack, ItemStack stack, IWand wand, float partialTick) {
        // TODO Auto-generated method stub
        
    }
}
