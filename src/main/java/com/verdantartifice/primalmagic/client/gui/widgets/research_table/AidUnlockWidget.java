package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing that a project was unlocked by a research aid in the research table GUI.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class AidUnlockWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/research_table_overlay.png");
    
    protected Block aidBlock;

    public AidUnlockWidget(int x, int y, @Nonnull Block aidBlock) {
        super(x, y, 8, 8, TextComponent.EMPTY);
        this.aidBlock = aidBlock;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        // Draw padlock icon
        matrixStack.pushPose();
        mc.getTextureManager().bind(TEXTURE);
        matrixStack.translate(this.x, this.y, 0.0F);
        this.blit(matrixStack, 0, 0, 198, 0, 8, 8);
        matrixStack.popPose();

        if (this.isHovered() && this.aidBlock != null) {
            // Render tooltip
            Component unlockText = new TranslatableComponent("primalmagic.research_table.unlock", this.aidBlock.getName());
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(unlockText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
