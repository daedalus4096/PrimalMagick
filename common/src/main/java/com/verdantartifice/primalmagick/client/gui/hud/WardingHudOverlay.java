package com.verdantartifice.primalmagick.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerWard;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class WardingHudOverlay {
    public static final Identifier ID = ResourceUtils.loc("warding_hud_overlay");
    protected static final Identifier GUI_ICONS_LOCATION = ResourceUtils.loc("textures/gui/hud.png");
    
    public static boolean shouldRender() {
        Minecraft mc = Minecraft.getInstance();
        return !mc.options.hideGui && mc.gameMode != null && mc.gameMode.canHurtPlayer();
    }
    
    public static void render(GuiGraphicsExtractor pGuiGraphics, DeltaTracker pDeltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (!shouldRender() || !(mc.getCameraEntity() instanceof Player player)) {
            return;
        }

        Profiler.get().push("warding");

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

        Profiler.get().pop();
    }

    private static void renderPentacles(GuiGraphicsExtractor guiGraphics, Player player, int left, int top, int rowHeight,
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

    private static void renderPentacle(GuiGraphicsExtractor guiGraphics, int xPos, int yPos, int textureX, int textureY, boolean highlight, boolean isHalf) {
        // FIXME Split texture into sprites
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI_ICONS_LOCATION, xPos, yPos, textureX + (isHalf ? 9 : 0), textureY, 9, 9, 256, 256);
    }
}
