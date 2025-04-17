package com.verdantartifice.primalmagick.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerWard;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class WardingHudOverlay {
    public static final ResourceLocation ID = ResourceUtils.loc("warding_hud_overlay");
    protected static final ResourceLocation GUI_ICONS_LOCATION = ResourceUtils.loc("textures/gui/hud.png");
    
    public static boolean shouldRender() {
        Minecraft mc = Minecraft.getInstance();
        return !mc.options.hideGui && mc.gameMode.canHurtPlayer() && (mc.getCameraEntity() instanceof Player);
    }
    
    public static void render(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker) {
        if (!shouldRender()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        
        mc.getProfiler().push("warding");
        RenderSystem.enableBlend();
        
        Player player = (Player)mc.getCameraEntity();
        Optional<IPlayerWard> wardCapOpt = Services.CAPABILITIES.ward(player);
        
        int ward = Mth.ceil(wardCapOpt.map(IPlayerWard::getCurrentWard).orElse(0F));
        int wardLast = ward;    // FIXME Get the last ward value
        boolean highlight = false;

        float wardMax = wardCapOpt.map(IPlayerWard::getMaxWard).orElse(0F);
        int absorb = 0; // Not relevant to wards
        int wardRows = Mth.ceil(wardMax / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (wardRows - 2), 3);
        
        if (wardMax > 0F) {
            int left = pGuiGraphics.guiWidth() / 2 - 91;
            // FIXME Find a better way to determine where the ward overlay should be rendered vertically on the screen
            int top = pGuiGraphics.guiHeight() - 28 - rowHeight;

            int regen = -1;
            if (wardCapOpt.map(IPlayerWard::isRegenerating).orElse(false)) {
                regen = mc.gui.getGuiTicks() % Mth.ceil(wardMax + 5.0F);
            }

            renderPentacles(pGuiGraphics, player, left, top, rowHeight, regen, wardMax, ward, wardLast, absorb, highlight);
        }

        RenderSystem.disableBlend();
        mc.getProfiler().pop();
    }

    private static void renderPentacles(GuiGraphics guiGraphics, Player player, int left, int top, int rowHeight,
            int regen, float wardMax, int ward, int wardLast, int absorb, boolean highlight) {
        int maxHealthHearts = Mth.ceil((double)wardMax / 2.0D);
        int maxAbsorbHearts = Mth.ceil((double)absorb / 2.0D);
        
        for (int index = maxHealthHearts + maxAbsorbHearts - 1; index >= 0; index--) {
            int row = index / 10;
            int col = index % 10;
            int xPos = left + col * 8;
            int yPos = top - row * rowHeight;
            if (index < maxHealthHearts && index == regen) {
                yPos -= 2;
            }
            
            renderPentacle(guiGraphics, xPos, yPos, 87, 0, highlight, false); // Render container
            int doubleIndex = index * 2;    // ???
            
            if (highlight && doubleIndex < wardLast) {
                renderPentacle(guiGraphics, xPos, yPos, 96, 0, true, (doubleIndex + 1 == wardLast));
            }
            if (doubleIndex < ward) {
                renderPentacle(guiGraphics, xPos, yPos, 96, 0, false, (doubleIndex + 1 == ward));
            }
        }
    }

    private static void renderPentacle(GuiGraphics guiGraphics, int xPos, int yPos, int textureX, int textureY, boolean highlight, boolean isHalf) {
        guiGraphics.blit(GUI_ICONS_LOCATION, xPos, yPos, textureX + (isHalf ? 9 : 0), textureY, 9, 9);
    }
}
