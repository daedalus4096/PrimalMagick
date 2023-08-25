package com.verdantartifice.primalmagick.client.gui.hud;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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
    private static final ResourceLocation HUD_TEXTURE = PrimalMagick.resource("textures/gui/hud.png");
    
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && !mc.player.isSpectator() && Config.SHOW_WAND_HUD.get()) {
            if (mc.player.getMainHandItem().getItem() instanceof IWand wand) {
                this.renderHud(mc, guiGraphics, mc.player.getMainHandItem(), wand, partialTick);
            } else if (mc.player.getOffhandItem().getItem() instanceof IWand wand) {
                this.renderHud(mc, guiGraphics, mc.player.getOffhandItem(), wand, partialTick);
            }
        }
    }

    private void renderHud(Minecraft mc, GuiGraphics guiGraphics, ItemStack stack, IWand wand, float partialTick) {
        guiGraphics.pose().pushPose();
        
        int posY = 0;
        ResourceLocation spellIcon = wand.getActiveSpell(stack) == null ? null : wand.getActiveSpell(stack).getIcon();
        posY += this.renderSpellDisplay(guiGraphics, 0, posY, spellIcon, partialTick);
        
        int index = 0;
        int maxMana = wand.getMaxMana(stack);
        Component maxText = wand.getMaxManaText(stack);
        List<Source> discoveredSources = Source.SORTED_SOURCES.stream().filter(s -> s.isDiscovered(mc.player)).collect(Collectors.toList());
        for (Source source : discoveredSources) {
            int curMana = wand.getMana(stack, source);
            Component curText = wand.getManaText(stack, source);
            
            double ratio = (double)curMana / (double)maxMana;
            Component ratioText = Component.translatable("tooltip.primalmagick.source.mana_summary_fragment", curText, maxText);
            
            posY += this.renderManaGauge(guiGraphics, 0, posY, ratioText, ratio, source.getColor(), (++index == discoveredSources.size()), partialTick, mc.font);
        }
        
        guiGraphics.pose().popPose();
    }

    private int renderSpellDisplay(GuiGraphics guiGraphics, int x, int y, ResourceLocation spellIcon, float partialTick) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.setColor(1, 1, 1, 1);
        
        // Render the spell display background
        guiGraphics.blit(HUD_TEXTURE, x, y, 60, 0, 26, 26, 256, 256);
        
        // Render the spell icon, if present
        if (spellIcon != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5F, 0.5F, 1F);
            guiGraphics.blit(spellIcon, x + 10, y + 10, 0, 0, 32, 32, 32, 32);
            guiGraphics.pose().popPose();
        }

        return 26;
    }

    private int renderManaGauge(GuiGraphics guiGraphics, int x, int y, Component text, double ratio, int color, boolean isLast, float partialTick, Font font) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        // Render the gauge background
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.blit(HUD_TEXTURE, x, y, 0, 0, 59, 12, 256, 256);
        
        // If not the last gauge in the list, render trailing salt connector
        if (!isLast) {
            guiGraphics.blit(HUD_TEXTURE, x + 4, y + 8, 4, 12, 2, 4, 256, 256);
        }
        
        // Render the gauge mana bar
        guiGraphics.setColor(getRed(color), getGreen(color), getBlue(color), 1);
        guiGraphics.blit(HUD_TEXTURE, x + 14, y + 2, 14, 12, (int)(40 * ratio), 8, 256, 256);
        
        // Render the mana text by the gauge if holding shift
        guiGraphics.setColor(1, 1, 1, 1);
        if (Screen.hasShiftDown()) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(61, 2, 0);
            guiGraphics.drawString(font, text, x, y, Color.WHITE.getRGB());
            guiGraphics.pose().popPose();
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
