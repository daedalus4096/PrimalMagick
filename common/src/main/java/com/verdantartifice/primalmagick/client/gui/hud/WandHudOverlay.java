package com.verdantartifice.primalmagick.client.gui.hud;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.common.wands.ManaManager;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * HUD overlay to show wand mana levels.
 * 
 * @author Daedalus4096
 */
public class WandHudOverlay {
    public static final Identifier ID = ResourceUtils.loc("wand_hud_overlay");
    private static final Identifier HUD_TEXTURE = ResourceUtils.loc("textures/gui/hud.png");
    
    public static boolean shouldRender() {
        Minecraft mc = Minecraft.getInstance();
        return !mc.options.hideGui && mc.player != null && !mc.player.isSpectator() && Services.CONFIG.showWandHud() &&
                (mc.player.getMainHandItem().getItem() instanceof IWand || mc.player.getOffhandItem().getItem() instanceof IWand);
    }
    
    public static void render(GuiGraphicsExtractor pGuiGraphics, DeltaTracker pDeltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (shouldRender() && mc.player != null) {
            renderHud(mc, pGuiGraphics, mc.player.getMainHandItem(), mc.player.getOffhandItem(), pDeltaTracker.getGameTimeDeltaPartialTick(true));
        }
    }
    
    private static void renderHud(Minecraft mc, GuiGraphicsExtractor guiGraphics, ItemStack mainHandStack, ItemStack offHandStack, float partialTick) {
        guiGraphics.pose().pushMatrix();
        
        int posY = 0;
        SpellPackage activeSpell = SpellManager.getActiveSpell(mainHandStack, offHandStack);
        Identifier spellIcon = activeSpell == null ? null : activeSpell.getIcon();
        posY += renderSpellDisplay(guiGraphics, 0, posY, spellIcon, partialTick);

        int index = 0;
        List<Source> discoveredSources = Sources.getAllSorted().stream().filter(s -> s.isDiscovered(mc.player)).toList();
        for (Source source : discoveredSources) {
            int maxMana = ManaManager.getMaxMana(mc.player, source);
            Component maxText = ManaManager.getMaxManaText(mc.player, source);

            int curMana = ManaManager.getMana(mc.player, source);
            Component curText = ManaManager.getManaText(mc.player, source);

            double ratio = (double)curMana / (double)maxMana;
            Component ratioText = Component.translatable("tooltip.primalmagick.source.mana_summary_fragment", curText, maxText);

            posY += renderManaGauge(guiGraphics, 0, posY, ratioText, ratio, source.getColor(), (++index == discoveredSources.size()), partialTick, mc.font);
        }

        guiGraphics.pose().popMatrix();
    }

    private static int renderSpellDisplay(GuiGraphicsExtractor guiGraphics, int x, int y, Identifier spellIcon, float partialTick) {
        // Render the spell display background
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HUD_TEXTURE, x, y, 60, 0, 26, 26, 256, 256);
        
        // Render the spell icon, if present
        if (spellIcon != null) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().scale(0.5F, 0.5F);
            guiGraphics.blit(spellIcon, x + 10, y + 10, 0, 0, 32, 32, 32, 32);
            guiGraphics.pose().popMatrix();
        }

        return 26;
    }

    private static int renderManaGauge(GuiGraphicsExtractor guiGraphics, int x, int y, Component text, double ratio, int color, boolean isLast, float partialTick, Font font) {
        // Render the gauge background
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HUD_TEXTURE, x, y, 0, 0, 59, 12, 256, 256);
        
        // If not the last gauge in the list, render trailing salt connector
        if (!isLast) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HUD_TEXTURE, x + 4, y + 8, 4, 12, 2, 4, 256, 256);
        }
        
        // Render the gauge mana bar
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HUD_TEXTURE, x + 14, y + 2, 14, 12, (int)(40 * ratio), 8, 256, 256, color);
        
        // Render the mana text by the gauge if holding shift
        if (Minecraft.getInstance().hasShiftDown()) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(61, 2);
            guiGraphics.drawString(font, text, x, y, -1);
            guiGraphics.pose().popMatrix();
        }
        
        return 12;
    }
}
