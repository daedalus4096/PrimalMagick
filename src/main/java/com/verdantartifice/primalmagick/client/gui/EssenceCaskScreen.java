package com.verdantartifice.primalmagick.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.EssenceCaskWidget;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.menus.EssenceCaskMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.WithdrawCaskEssencePacket;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the essence cask block.
 * 
 * @author Daedalus4096
 */
public class EssenceCaskScreen extends AbstractContainerScreen<EssenceCaskMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/essence_cask.png");
    
    protected final List<EssenceCaskWidget> caskWidgets = new ArrayList<>();
    protected long lastCheck = 0L;
    protected int lastTotalEssence = 0;

    public EssenceCaskScreen(EssenceCaskMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }
    
    @Override
    protected void init() {
        super.init();
        this.lastTotalEssence = this.menu.getTotalEssenceCount();
        this.initWidgets();
    }

    protected void initWidgets() {
        Minecraft mc = Minecraft.getInstance();
        this.clearWidgets();
        this.caskWidgets.clear();
        
        int visibleRows = Arrays.stream(EssenceType.values()).mapToInt(t -> this.menu.isEssenceTypeVisible(t, mc.player) ? 1 : 0).sum();
        int visibleCols = Source.SORTED_SOURCES.stream().mapToInt(s -> this.menu.isEssenceSourceVisible(s, mc.player) ? 1 : 0).sum();
        int startX = this.leftPos + 8 + (((Source.SORTED_SOURCES.size() - visibleCols) * 18) / 2);
        int startY = this.topPos + 18 + (((EssenceType.values().length - visibleRows) * 18) / 2);
        
        int index = 0;
        int xPos = startX;
        int yPos = startY;
        for (int row = 0; row < EssenceType.values().length; row++) {
            boolean rowPopulated = false;
            for (int col = 0; col < Source.SORTED_SOURCES.size(); col++) {
                EssenceType cellType = EssenceType.values()[row];
                Source cellSource = Source.SORTED_SOURCES.get(col);
                if (this.menu.isEssenceTypeVisible(cellType, mc.player) && this.menu.isEssenceSourceVisible(cellSource, mc.player)) {
                    int count = this.menu.getEssenceCount(index);
                    this.caskWidgets.add(this.addRenderableWidget(new EssenceCaskWidget(index, cellType, cellSource, count, xPos, yPos, this::onWidgetClicked)));
                    xPos += 18;
                    rowPopulated = true;
                }
                index++;
            }
            xPos = startX;
            if (rowPopulated) {
                yPos += 18;
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Determine if we need to update the GUI based on how long it's been since the last refresh or the total essence in the cask
        long millis = System.currentTimeMillis();
        if (millis > this.lastCheck || this.lastTotalEssence != this.menu.getTotalEssenceCount()) {
            this.lastCheck = millis + 2000L;
            this.lastTotalEssence = this.menu.getTotalEssenceCount();
            this.initWidgets();
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        
        for (EssenceCaskWidget widget : this.caskWidgets) {
            if (widget.isHovered()) {
                renderSlotHighlight(guiGraphics, widget.getX(), widget.getY(), 0, this.slotColor);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        Component contentsLabel = Component.translatable("label.primalmagick.essence_cask.contents", this.menu.getTotalEssenceCount(), this.menu.getTotalEssenceCapacity());
        guiGraphics.drawString(this.font, contentsLabel, 8, 92, 4210752, false);
    }

    protected void onWidgetClicked(EssenceCaskWidget widget, int clickButton) {
        int toRemove = clickButton == 1 ? 1 : 64;
        PacketHandler.sendToServer(new WithdrawCaskEssencePacket(widget.getEssenceType(), widget.getSource(), toRemove, this.menu.getTilePos()));
    }
}
