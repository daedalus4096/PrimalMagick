package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.WandAssemblyTableMenu;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for wand assembly table block.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyTableScreen extends AbstractContainerScreenPM<WandAssemblyTableMenu> {
    private static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/gui/wand_assembly_table.png");

    public WandAssemblyTableScreen(WandAssemblyTableMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
