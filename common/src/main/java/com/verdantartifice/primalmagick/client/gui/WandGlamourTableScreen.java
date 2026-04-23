package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.WandGlamourTableMenu;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI screen for the wand glamour table block.
 * 
 * @author Daedalus4096
 */
public class WandGlamourTableScreen extends AbstractContainerScreenPM<WandGlamourTableMenu> {
    private static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/wand_glamour_table.png");

    public WandGlamourTableScreen(WandGlamourTableMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphicsExtractor guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}
