package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.ResearchTableScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI button to select or deselect a project material in the research table screen.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ProjectMaterialSelectionCheckbox extends Button {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/checkbox.png");
    
    protected ResearchTableScreen screen;
    protected boolean selected;
    protected int index;

    public ProjectMaterialSelectionCheckbox(int xIn, int yIn, ResearchTableScreen screen, boolean selected, int index) {
        super(xIn, yIn, 16, 16, "", new Handler());
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
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(this.x, this.y, this.selected ? 16 : 0, this.isHovered() ? 16 : 0, this.width, this.height);
    }
    
    protected static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof ProjectMaterialSelectionCheckbox) {
                ProjectMaterialSelectionCheckbox pmsc = (ProjectMaterialSelectionCheckbox)button;
                pmsc.setSelected(!pmsc.isSelected());
            }
        }
    }
}
