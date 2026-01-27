package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AttunementMeterWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AttunementThresholdWidget;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Grimoire page showing the details of a discovered attunement.
 * 
 * @author Daedalus4096
 */
public class AttunementPage extends AbstractPage {
    private static final Identifier BACKGROUND = ResourceUtils.loc("grimoire/attunement_meter_background");

    protected Source source;
    protected List<IPageElement> contents = new ArrayList<>();
    protected boolean firstPage;

    public AttunementPage(@Nonnull Source source) {
        this(source, false);
    }
    
    public AttunementPage(@Nonnull Source source, boolean first) {
        this.source = source;
        this.firstPage = first;
    }
    
    @Nonnull
    public List<IPageElement> getElements() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addElement(IPageElement element) {
        return this.contents.add(element);
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page if applicable
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
            y += 53;
        } else {
            y += 25;
        }
        
        if (this.isFirstPage()) {
            // Render meter background
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, x + 51 + (side * 140), y, 16, 120);
        }

        // Render page contents
        for (IPageElement content : this.contents) {
            content.render(guiGraphics, side, x, y);
            y = content.getNextY(y);
        }
    }

    @Override
    protected Component getTitleText() {
        return Component.translatable(this.source.getNameTranslationKey());
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        if (this.isFirstPage()) {
            screen.addWidgetToScreen(new AttunementMeterWidget(this.source, x + 68 + (side * 140), y + 17));
            screen.addWidgetToScreen(new AttunementThresholdWidget(this.source, AttunementThreshold.MINOR, x + 83 + (side * 140), y + 79));
            screen.addWidgetToScreen(new AttunementThresholdWidget(this.source, AttunementThreshold.LESSER, x + 83 + (side * 140), y + 49));
            screen.addWidgetToScreen(new AttunementThresholdWidget(this.source, AttunementThreshold.GREATER, x + 83 + (side * 140), y + 19));
        }
    }
}
