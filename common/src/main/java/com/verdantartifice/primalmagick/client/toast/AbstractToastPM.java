package com.verdantartifice.primalmagick.client.toast;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class AbstractToastPM implements Toast {
    protected static final int DISPLAY_TIME = 5000;
    protected static final long TITLE_TIME = 1500L;
    protected static final float FADE_DURATION = 300F;

    private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;

    protected abstract Identifier getBackgroundSprite();
    protected abstract Component getTitleText();
    protected abstract Optional<Component> getSubtitleText();
    protected abstract Component getBodyText();
    protected abstract int getTitleColor();
    protected abstract int getSubtitleColor();
    protected abstract int getBodyColor();
    protected abstract Optional<Identifier> getIcon();

    protected int getIconWidth() {
        return 16;
    }

    protected int getIconHeight() {
        return 16;
    }

    @Override
    @NotNull
    public Toast.Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void update(@NotNull ToastManager toastManager, long pTimeSinceLastVisible) {
        this.wantedVisibility = (double)pTimeSinceLastVisible >= DISPLAY_TIME * toastManager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, @NotNull Font font, long pTimeSinceLastVisible) {
        final int x = this.getIcon().isPresent() ? 30 : 6;
        final int lineMax = this.getIcon().isPresent() ? 125 : 148;

        // Render the toast background
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getBackgroundSprite(), 0, 0, this.width(), this.height());

        // Split the body text into lines to fit the toast
        List<FormattedCharSequence> bodyLines = font.split(this.getBodyText(), lineMax);
        if (this.getSubtitleText().isEmpty() && bodyLines.size() == 1) {
            // If only one body line is needed and there's no subtitle, render the title and body together without a fade
            pGuiGraphics.text(font, this.getTitleText(), x, 7, this.getTitleColor() | 0xFF000000, false);
            pGuiGraphics.text(font, bodyLines.getFirst(), x, 18, this.getBodyColor(), false);
        } else {
            // Otherwise, render toast body with a fade
            this.extractToastBodyWithFade(pGuiGraphics, font, pTimeSinceLastVisible, x, bodyLines);
        }

        // Render the toast icon if present
        this.getIcon().ifPresent(iconLoc -> {
            pGuiGraphics.pose().pushMatrix();
            pGuiGraphics.pose().translate(8, 8);
            pGuiGraphics.pose().scale(this.getIconWidth() / 256F, this.getIconHeight() / 256F);
            pGuiGraphics.blit(RenderPipelines.GUI_TEXTURED, iconLoc, 0, 0, 0, 0, 255, 255, 256, 256);
            pGuiGraphics.pose().popMatrix();
        });
    }

    private void extractToastBodyWithFade(@NotNull GuiGraphicsExtractor pGuiGraphics, @NotNull Font font, long pTimeSinceLastVisible, int xPos, List<FormattedCharSequence> bodyLines) {
        if (pTimeSinceLastVisible < TITLE_TIME) {
            this.extractTitle(pGuiGraphics, font, pTimeSinceLastVisible, xPos);
        } else {
            int bodyFade = Mth.floor(Mth.clamp((float)(pTimeSinceLastVisible - TITLE_TIME) / FADE_DURATION, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
            int y = this.height() / 2 - bodyLines.size() * 9 / 2;
            for (FormattedCharSequence formattedcharsequence : bodyLines) {
                pGuiGraphics.text(font, formattedcharsequence, xPos, y, this.getBodyColor() | bodyFade, false);
                y += 9;
            }
        }
    }

    private void extractTitle(@NotNull GuiGraphicsExtractor pGuiGraphics, @NotNull Font font, long pTimeSinceLastVisible, int xPos) {
        int titleFade = Mth.floor(Mth.clamp((float)(TITLE_TIME - pTimeSinceLastVisible) / FADE_DURATION, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
        this.getSubtitleText().ifPresentOrElse(subtitle -> {
            pGuiGraphics.text(font, this.getTitleText(), xPos, 7, this.getTitleColor() | titleFade, false);
            pGuiGraphics.text(font, subtitle, xPos, 18, this.getSubtitleColor() | titleFade, false);
        }, () -> pGuiGraphics.text(font, this.getTitleText(), xPos, 11, this.getTitleColor() | titleFade, false));
    }
}
