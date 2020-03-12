package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing that a project was unlocked by a research aid in the research table GUI.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class AidUnlockWidget extends Widget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/research_table_overlay.png");
    
    protected Block aidBlock;

    public AidUnlockWidget(int x, int y, @Nonnull Block aidBlock) {
        super(x, y, 8, 8, "");
        this.aidBlock = aidBlock;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Draw padlock icon
        RenderSystem.pushMatrix();
        mc.getTextureManager().bindTexture(TEXTURE);
        RenderSystem.translatef(this.x, this.y, 0.0F);
        this.blit(0, 0, 198, 0, 8, 8);
        RenderSystem.popMatrix();

        if (this.isHovered() && this.aidBlock != null) {
            // Render tooltip
            ITextComponent unlockText = new TranslationTextComponent("primalmagic.research_table.unlock", this.aidBlock.getNameTextComponent());
            GuiUtils.renderCustomTooltip(Collections.singletonList(unlockText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
