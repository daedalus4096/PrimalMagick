package com.verdantartifice.primalmagic.client.gui.grimoire;

import java.awt.Color;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing the attunements gained from a research stage.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class AttunementGainPage extends AbstractPage {
    protected SourceList attunements;
    
    public AttunementGainPage(@Nonnull SourceList attunements) {
        this.attunements = attunements;
    }
    
    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }

    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        // Render page title
        this.renderTitle(side, x, y, mouseX, mouseY, null);
        y += 53;

        // Render attunement gain list
        Minecraft mc = Minecraft.getInstance();
        for (Source source : this.attunements.getSourcesSorted()) {
            int amount = MathHelper.clamp(this.attunements.getAmount(source), 0, 5);
            ITextComponent labelText = source.isDiscovered(mc.player) ?
                    new TranslationTextComponent(source.getNameTranslationKey()) :
                    new TranslationTextComponent(Source.getUnknownTranslationKey());
            ITextComponent amountText = new TranslationTextComponent("primalmagic.attunement_gain." + Integer.toString(amount));
            ITextComponent fullText = new TranslationTextComponent("primalmagic.attunement_gain.text", labelText, amountText);
            mc.fontRenderer.drawString(fullText.getFormattedText(), x - 3 + (side * 140), y - 6, Color.BLACK.getRGB());
            y += mc.fontRenderer.FONT_HEIGHT;
        }
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.attunement_gain_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {}
}
