package com.verdantartifice.primalmagick.client.gui.hud;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
    private static final ResourceLocation HUD_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/hud.png");
    
    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && !mc.player.isSpectator()) {
            ItemStack stack = mc.player.getItemBySlot(EquipmentSlot.MAINHAND);
            if (stack.getItem() instanceof IWand wand) {
                this.renderHud(mc, poseStack, stack, wand, partialTick);
            }
        }
    }

    private void renderHud(Minecraft mc, PoseStack poseStack, ItemStack stack, IWand wand, float partialTick) {
        poseStack.pushPose();
        
        int posY = 0;
        int maxMana = wand.getMaxMana(stack);
        Component maxText = wand.getMaxManaText(stack);
        for (Source source : Source.SORTED_SOURCES) {
            int curMana = wand.getMana(stack, source);
            Component curText = wand.getManaText(stack, source);
            
            double ratio = (double)curMana / (double)maxMana;
            Component ratioText = Component.translatable("primalmagick.source.mana_summary_fragment", curText, maxText);
            
            posY += this.renderManaGauge(poseStack, 0, posY, ratioText, ratio, source.getColor(), partialTick, mc.font);
        }
        
        poseStack.popPose();
    }

    private int renderManaGauge(PoseStack poseStack, int x, int y, Component text, double ratio, int color, float partialTick, Font font) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, HUD_TEXTURE);
        
        // Render the gauge background
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GuiComponent.blit(poseStack, x, y, 0, 0, 50, 12, 256, 256);
        
        // Render the gauge mana bar
        RenderSystem.setShaderColor(getRed(color), getGreen(color), getBlue(color), 1);
        GuiComponent.blit(poseStack, x + 5, y + 2, 5, 12, 40, 8, 256, 256);
        
        // Render the mana text by the gauge if holding shift
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (Screen.hasShiftDown()) {
            poseStack.pushPose();
            poseStack.translate(52, 2, 0);
            font.drawShadow(poseStack, text, x, y, Color.WHITE.getRGB());
            poseStack.popPose();
        }
        
        return 12;
    }
    
    private static float getRed(int color) {
        return (color >> 16 & 0xFF) / 255.0F;
    }

    private static float getGreen(int color) {
        return (color >> 8 & 0xFF) / 255.0F;
    }

    private static float getBlue(int color) {
        return (color & 0xFF) / 255.0F;
    }
}
