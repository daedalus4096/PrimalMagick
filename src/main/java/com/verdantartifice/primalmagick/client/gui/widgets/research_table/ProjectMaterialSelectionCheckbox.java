package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.ResearchTableScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * GUI button to select or deselect a project material in the research table screen.
 * 
 * @author Daedalus4096
 */
public class ProjectMaterialSelectionCheckbox extends Button {
    private static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/checkbox.png");
    
    protected ResearchTableScreen screen;
    protected boolean selected;
    protected int index;

    public ProjectMaterialSelectionCheckbox(int x, int y, ResearchTableScreen screen, boolean selected, int index) {
        super(Button.builder(Component.empty(), new Handler()).bounds(x, y, 16, 16));
        this.screen = screen;
        this.selected = selected;
        this.index = index;
    }
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        this.screen.setMaterialSelection(this.index, this.selected);
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        guiGraphics.blit(TEXTURE, this.getX(), this.getY(), this.selected ? 16 : 0, this.isHoveredOrFocused() ? 16 : 0, this.width, this.height);
    }
    
    protected static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof ProjectMaterialSelectionCheckbox) {
                ProjectMaterialSelectionCheckbox pmsc = (ProjectMaterialSelectionCheckbox)button;
                pmsc.setSelected(!pmsc.isSelected());
            }
        }
    }
}
