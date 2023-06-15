package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.ResearchTableScreen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * GUI button to select or deselect a project material in the research table screen.
 * 
 * @author Daedalus4096
 */
public class ProjectMaterialSelectionCheckbox extends Button {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/checkbox.png");
    
    protected ResearchTableScreen screen;
    protected boolean selected;
    protected int index;

    public ProjectMaterialSelectionCheckbox(int xIn, int yIn, ResearchTableScreen screen, boolean selected, int index) {
        super(xIn, yIn, 16, 16, Component.empty(), new Handler());
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
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.getX(), this.getY(), this.selected ? 16 : 0, this.isHoveredOrFocused() ? 16 : 0, this.width, this.height);
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
