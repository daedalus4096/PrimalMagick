package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/**
 * Grimoire page showing the player's relevant linguistics scores (i.e. comprehension and vocabulary)
 * for a given language.
 * 
 * @author Daedalus4096
 */
public class LinguisticsScorePage extends AbstractPage {
    protected final Holder<BookLanguage> language;
    
    public LinguisticsScorePage(Holder<BookLanguage> language) {
        this.language = language;
    }

    @Override
    protected Component getTitleText() {
        return this.language.get().getName();
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Nothing to do
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Render page title
        this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
        y += 53;
        
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);  // Bump up slightly in the Z-order to prevent the underline from being swallowed
        Minecraft mc = Minecraft.getInstance();
        
        // Render comprehension score and rating
        Component compHeader = Component.translatable("grimoire.primalmagick.linguistics_data.comprehension_score_header").withStyle(ChatFormatting.UNDERLINE);
        guiGraphics.drawString(mc.font, compHeader, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
        y += mc.font.lineHeight;
        Component scoreText;
        if (this.language.get().complexity() > 0 && !this.language.get().autoTranslate()) {
            int currentComp = LinguisticsManager.getComprehension(mc.player, this.language);
            int compRating = Mth.clamp((int)(((double)currentComp / (double)this.language.get().complexity()) * 4D), 0, 4);
            Component ratingText = Component.translatable("grimoire.primalmagick.linguistics_data.comprehension_rating." + compRating);
            scoreText = Component.translatable("grimoire.primalmagick.linguistics_data.comprehension_score", currentComp, this.language.get().complexity(), ratingText);
        } else {
            scoreText = Component.translatable("grimoire.primalmagick.linguistics_data.comprehension_score.unreadable");
        }
        guiGraphics.drawString(mc.font, scoreText, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
        y += mc.font.lineHeight;

        // Leave a blank line between comprehension and vocabulary
        y += mc.font.lineHeight;

        // Render vocabulary rating
        Component vocabHeader = Component.translatable("grimoire.primalmagick.linguistics_data.vocabulary_score_header").withStyle(ChatFormatting.UNDERLINE);
        guiGraphics.drawString(mc.font, vocabHeader, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
        y += mc.font.lineHeight;
        Component vocabText = (this.language.get().complexity() > 0 && !this.language.get().autoTranslate()) ?
                Component.literal(Integer.toString(LinguisticsManager.getVocabulary(mc.player, this.language))) :
                Component.translatable("grimoire.primalmagick.linguistics_data.comprehension_score.unreadable");
        guiGraphics.drawString(mc.font, vocabText, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
        y += mc.font.lineHeight;

        guiGraphics.pose().popPose();
    }
}
