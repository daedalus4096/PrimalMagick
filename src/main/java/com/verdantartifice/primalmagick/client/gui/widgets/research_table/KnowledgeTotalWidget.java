package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Display widget for showing accumulated knowledge (e.g. observations) in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class KnowledgeTotalWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/research_table_overlay.png");
    protected static final DecimalFormat FORMATTER = new DecimalFormat("###.##");

    protected final KnowledgeType type;
    protected final LazyOptional<IPlayerKnowledge> knowledgeOpt;
    protected final OptionalInt successDeltaOpt;
    
    public KnowledgeTotalWidget(int x, int y, KnowledgeType type) {
        this(x, y, type, OptionalInt.empty());
    }
    
    public KnowledgeTotalWidget(int x, int y, KnowledgeType type, OptionalInt successDeltaOpt) {
        super(x, y, 16, 19, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.type = type;
        this.knowledgeOpt = PrimalMagickCapabilities.getKnowledge(mc.player);
        this.successDeltaOpt = successDeltaOpt;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        
        // Prepare tooltip
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable(this.type.getNameTranslationKey()));
        
        // Draw knowledge type icon
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        guiGraphics.pose().scale(0.0625F, 0.0625F, 0.0625F);
        guiGraphics.blit(this.type.getIconLocation(), 0, 0, 0, 0, 255, 255);        
        guiGraphics.pose().popPose();
        
        // Draw progress bar background
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY() + 17, 0.0F);
        guiGraphics.blit(TEXTURE, 0, 0, 182, 2, 16, 2);
        guiGraphics.pose().popPose();
        
        this.knowledgeOpt.ifPresent(knowledge -> {
            // Draw amount str
            int levels = knowledge.getKnowledge(this.type);
            Component amountText = Component.literal(Integer.toString(levels));
            int width = mc.font.width(amountText);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12, 5.0F);
            guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
            guiGraphics.drawString(mc.font, amountText, 0, 0, Color.WHITE.getRGB());
            guiGraphics.pose().popPose();
            
            // Draw progress bar foreground
            int rawPoints = knowledge.getKnowledgeRaw(this.type);
            int levelPoints = rawPoints % this.type.getProgression();
            int px = (int)(16.0D * ((double)levelPoints / (double)this.type.getProgression()));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX(), this.getY() + 17, 1.0F);
            guiGraphics.blit(TEXTURE, 0, 0, 182, 0, px, 2);
            guiGraphics.pose().popPose();
        });
        
        this.successDeltaOpt.ifPresent(points -> {
            double levels = (double)points / (double)this.type.getProgression();
            String levelStr = FORMATTER.format(Math.abs(levels));
            if (points > 0) {
                // Draw theory gain preview str
                Component previewText = Component.translatable("label.primalmagick.research_table.theory_gain_preview.positive", levelStr).withStyle(ChatFormatting.GREEN);
                int width = mc.font.width(previewText);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY(), 5.0F);
                guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
                guiGraphics.drawString(mc.font, previewText, 0, 0, Color.WHITE.getRGB());
                guiGraphics.pose().popPose();
                
                // Prepare tooltip addition
                if (levels == 1D) {
                    lines.add(Component.translatable("tooltip.primalmagick.research_table.theory_gain_preview.positive.single", levelStr).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                } else {
                    lines.add(Component.translatable("tooltip.primalmagick.research_table.theory_gain_preview.positive.multiple", levelStr).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                }
            }
        });
        
        // Assemble tooltip
        this.setTooltip(Tooltip.create(CommonComponents.joinLines(lines)));
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
