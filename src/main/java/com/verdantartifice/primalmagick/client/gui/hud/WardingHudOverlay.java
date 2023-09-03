package com.verdantartifice.primalmagick.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerWard;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.util.LazyOptional;

public class WardingHudOverlay implements IGuiOverlay {
    protected static final ResourceLocation GUI_ICONS_LOCATION = PrimalMagick.resource("textures/gui/hud.png");

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = gui.getMinecraft();
        if (!mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            
            mc.getProfiler().push("warding");
            RenderSystem.enableBlend();
            
            Player player = (Player)mc.getCameraEntity();
            LazyOptional<IPlayerWard> wardCapOpt = PrimalMagickCapabilities.getWard(player);
            
            int ward = Mth.ceil(wardCapOpt.<Float>lazyMap(wardCap -> wardCap.getCurrentWard()).orElse(0F));
            int wardLast = ward;    // FIXME Get the last ward value
            boolean highlight = false;

            float wardMax = wardCapOpt.<Float>lazyMap(wardCap -> wardCap.getMaxWard()).orElse(0F);
            int absorb = 0; // Not relevant to wards
            int wardRows = Mth.ceil(wardMax / 2.0F / 10.0F);
            int rowHeight = Math.max(10 - (wardRows - 2), 3);
            
            if (wardMax > 0F) {
                int left = screenWidth / 2 - 91;
                int top = screenHeight - gui.leftHeight;
                gui.leftHeight += (wardRows * rowHeight);
                if (rowHeight != 10) gui.leftHeight += 10 - rowHeight;

                int regen = -1;
                if (wardCapOpt.<Boolean>lazyMap(wardCap -> wardCap.isRegenerating()).orElse(false)) {
                    regen = gui.getGuiTicks() % Mth.ceil(wardMax + 5.0F);
                }

                this.renderPentacles(gui, guiGraphics, player, left, top, rowHeight, regen, wardMax, ward, wardLast, absorb, highlight);
            }

            RenderSystem.disableBlend();
            mc.getProfiler().pop();
        }
    }

    private void renderPentacles(ForgeGui gui, GuiGraphics guiGraphics, Player player, int left, int top, int rowHeight,
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
            
            this.renderPentacle(guiGraphics, xPos, yPos, 87, 0, highlight, false); // Render container
            int doubleIndex = index * 2;    // ???
            
            if (highlight && doubleIndex < wardLast) {
                this.renderPentacle(guiGraphics, xPos, yPos, 96, 0, true, (doubleIndex + 1 == wardLast));
            }
            if (doubleIndex < ward) {
                this.renderPentacle(guiGraphics, xPos, yPos, 96, 0, false, (doubleIndex + 1 == ward));
            }
        }
    }

    private void renderPentacle(GuiGraphics guiGraphics, int xPos, int yPos, int textureX, int textureY, boolean highlight, boolean isHalf) {
        guiGraphics.blit(GUI_ICONS_LOCATION, xPos, yPos, textureX + (isHalf ? 9 : 0), textureY, 9, 9);
    }
}
