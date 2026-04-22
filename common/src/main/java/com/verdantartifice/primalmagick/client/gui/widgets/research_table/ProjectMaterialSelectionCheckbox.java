package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import com.verdantartifice.primalmagick.client.gui.ResearchTableScreen;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * GUI button to select or deselect a project material in the research table screen.
 * 
 * @author Daedalus4096
 */
public class ProjectMaterialSelectionCheckbox extends Button {
    private static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/checkbox.png");
    
    protected ResearchTableScreen screen;
    protected boolean selected;
    protected int index;

    public ProjectMaterialSelectionCheckbox(int x, int y, ResearchTableScreen screen, boolean selected, int index) {
        super(x, y, 16, 16, Component.empty(), new Handler(), Button.DEFAULT_NARRATION);
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
    public void renderContents(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX(), this.getY(), this.selected ? 16 : 0, this.isHoveredOrFocused() ? 16 : 0, this.width, this.height, 256, 256);
    }
    
    protected static class Handler implements OnPress {
        @Override
        public void onPress(@NotNull Button button) {
            if (button instanceof ProjectMaterialSelectionCheckbox checkbox) {
                checkbox.setSelected(!checkbox.isSelected());
            }
        }
    }
}
