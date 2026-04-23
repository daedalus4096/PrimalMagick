package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.WandAssemblyTableMenu;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for wand assembly table block.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyTableScreen extends AbstractContainerScreenPM<WandAssemblyTableMenu> {
    private static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/wand_assembly_table.png");

    public WandAssemblyTableScreen(WandAssemblyTableMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphicsExtractor guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}
