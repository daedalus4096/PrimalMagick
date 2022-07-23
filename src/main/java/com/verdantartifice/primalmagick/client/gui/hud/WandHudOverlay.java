package com.verdantartifice.primalmagick.client.gui.hud;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/**
 * HUD overlay to show wand mana levels.
 * 
 * @author Daedalus4096
 */
public class WandHudOverlay implements IGuiOverlay {
    private static final ResourceLocation HUD_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/hud.png");
    
    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && !mc.player.isSpectator() && Config.SHOW_WAND_HUD.get()) {
            if (mc.player.getMainHandItem().getItem() instanceof IWand wand) {
                this.renderHud(mc, poseStack, mc.player.getMainHandItem(), wand, partialTick);
            } else if (mc.player.getOffhandItem().getItem() instanceof IWand wand) {
                this.renderHud(mc, poseStack, mc.player.getOffhandItem(), wand, partialTick);
            }
        }
    }

    private void renderHud(Minecraft mc, PoseStack poseStack, ItemStack stack, IWand wand, float partialTick) {
        poseStack.pushPose();
        
        int posY = 0;
        ResourceLocation spellIcon = wand.getActiveSpell(stack) == null ? null : wand.getActiveSpell(stack).getIcon();
        posY += this.renderSpellDisplay(poseStack, 0, posY, spellIcon, partialTick);
        
        int index = 0;
        int maxMana = wand.getMaxMana(stack);
        Component maxText = wand.getMaxManaText(stack);
        List<Source> discoveredSources = Source.SORTED_SOURCES.stream().filter(s -> s.isDiscovered(mc.player)).collect(Collectors.toList());
        for (Source source : discoveredSources) {
            int curMana = wand.getMana(stack, source);
            Component curText = wand.getManaText(stack, source);
            
            double ratio = (double)curMana / (double)maxMana;
            Component ratioText = Component.translatable("primalmagick.source.mana_summary_fragment", curText, maxText);
            
            posY += this.renderManaGauge(poseStack, 0, posY, ratioText, ratio, source.getColor(), (++index == discoveredSources.size()), partialTick, mc.font);
        }
        
        poseStack.popPose();
    }

    private int renderSpellDisplay(PoseStack poseStack, int x, int y, ResourceLocation spellIcon, float partialTick) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        
        // Render the spell display background
        RenderSystem.setShaderTexture(0, HUD_TEXTURE);
        GuiComponent.blit(poseStack, x, y, 60, 0, 26, 26, 256, 256);
        
        // Render the spell icon, if present
        if (spellIcon != null) {
            RenderSystem.setShaderTexture(0, spellIcon);
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 1F);
            GuiComponent.blit(poseStack, x + 10, y + 10, 0, 0, 32, 32, 32, 32);
            poseStack.popPose();
        }

        return 26;
    }

    private int renderManaGauge(PoseStack poseStack, int x, int y, Component text, double ratio, int color, boolean isLast, float partialTick, Font font) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, HUD_TEXTURE);
        
        // Render the gauge background
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GuiComponent.blit(poseStack, x, y, 0, 0, 59, 12, 256, 256);
        
        // If not the last gauge in the list, render trailing salt connector
        if (!isLast) {
            GuiComponent.blit(poseStack, x + 4, y + 8, 4, 12, 2, 4, 256, 256);
        }
        
        // Render the gauge mana bar
        RenderSystem.setShaderColor(getRed(color), getGreen(color), getBlue(color), 1);
        GuiComponent.blit(poseStack, x + 14, y + 2, 14, 12, (int)(40 * ratio), 8, 256, 256);
        
        // Render the mana text by the gauge if holding shift
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (Screen.hasShiftDown()) {
            poseStack.pushPose();
            poseStack.translate(61, 2, 0);
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
