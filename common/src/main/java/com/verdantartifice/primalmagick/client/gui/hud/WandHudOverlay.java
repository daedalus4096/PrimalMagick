package com.verdantartifice.primalmagick.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HUD overlay to show wand mana levels.
 * 
 * @author Daedalus4096
 */
public class WandHudOverlay {
    private static final ResourceLocation HUD_TEXTURE = ResourceUtils.loc("textures/gui/hud.png");
    
    public static boolean shouldRender() {
        Minecraft mc = Minecraft.getInstance();
        return !mc.options.hideGui && !mc.player.isSpectator() && Services.CONFIG.showWandHud();
    }
    
    public static void render(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getMainHandItem().getItem() instanceof IWand wand) {
            renderHud(mc, pGuiGraphics, mc.player.getMainHandItem(), wand, pDeltaTracker.getGameTimeDeltaPartialTick(true));
        } else if (mc.player.getOffhandItem().getItem() instanceof IWand wand) {
            renderHud(mc, pGuiGraphics, mc.player.getOffhandItem(), wand, pDeltaTracker.getGameTimeDeltaPartialTick(true));
        }
    }
    
    private static void renderHud(Minecraft mc, GuiGraphics guiGraphics, ItemStack stack, IWand wand, float partialTick) {
        guiGraphics.pose().pushPose();
        
        int posY = 0;
        ResourceLocation spellIcon = wand.getActiveSpell(stack) == null ? null : wand.getActiveSpell(stack).getIcon();
        posY += renderSpellDisplay(guiGraphics, 0, posY, spellIcon, partialTick);
        
        int index = 0;
        int maxMana = wand.getMaxMana(stack);
        Component maxText = wand.getMaxManaText(stack);
        List<Source> discoveredSources = Sources.getAllSorted().stream().filter(s -> s.isDiscovered(mc.player)).collect(Collectors.toList());
        for (Source source : discoveredSources) {
            int curMana = wand.getMana(stack, source);
            Component curText = wand.getManaText(stack, source);
            
            double ratio = (double)curMana / (double)maxMana;
            Component ratioText = Component.translatable("tooltip.primalmagick.source.mana_summary_fragment", curText, maxText);
            
            posY += renderManaGauge(guiGraphics, 0, posY, ratioText, ratio, source.getColor(), (++index == discoveredSources.size()), partialTick, mc.font);
        }
        
        guiGraphics.pose().popPose();
    }

    private static int renderSpellDisplay(GuiGraphics guiGraphics, int x, int y, ResourceLocation spellIcon, float partialTick) {
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

    private static int renderManaGauge(GuiGraphics guiGraphics, int x, int y, Component text, double ratio, int color, boolean isLast, float partialTick, Font font) {
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
        return FastColor.ARGB32.red(color) / 255.0F;
    }

    private static float getGreen(int color) {
        return FastColor.ARGB32.green(color) / 255.0F;
    }

    private static float getBlue(int color) {
        return FastColor.ARGB32.blue(color) / 255.0F;
    }
}
