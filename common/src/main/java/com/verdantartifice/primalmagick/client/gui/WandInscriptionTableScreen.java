package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.WandInscriptionTableMenu;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI screen for wand inscription table block.
 * 
 * @author Daedalus4096
 */
public class WandInscriptionTableScreen extends AbstractContainerScreenPM<WandInscriptionTableMenu> {
    private static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/wand_inscription_table.png");

    public WandInscriptionTableScreen(WandInscriptionTableMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}
