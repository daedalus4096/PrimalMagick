package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.WandChargerMenu;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI screen for wand charger block.
 * 
 * @author Daedalus4096
 */
public class WandChargerScreen extends AbstractContainerScreenPM<WandChargerMenu> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/wand_charger.png");
    protected static final Identifier PROGRESS_SPRITE = ResourceUtils.loc("progress_arrow");

    public WandChargerScreen(WandChargerMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        
        // Animate charge progress indicator
        int charge = this.menu.getChargeProgressionScaled();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_SPRITE, 24, 16, 0, 0, this.leftPos + 75, this.topPos + 34, charge, 16);
    }

}
