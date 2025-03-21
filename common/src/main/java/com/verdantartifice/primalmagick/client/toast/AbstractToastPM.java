package com.verdantartifice.primalmagick.client.toast;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class AbstractToastPM implements Toast {
    protected static final ResourceLocation BACKGROUND_SPRITE = ResourceUtils.loc("toast/background");
    protected static final int DISPLAY_TIME = 5000;
    protected static final long TITLE_TIME = 1500L;
    protected static final float FADE_DURATION = 300F;

    protected abstract Component getTitleText();
    protected abstract Component getBodyText();
    protected abstract int getTitleColor();
    protected abstract Optional<ResourceLocation> getIcon();

    @Override
    public @NotNull Visibility render(GuiGraphics pGuiGraphics, ToastComponent pToastComponent, long pTimeSinceLastVisible) {
        int x = this.getIcon().isPresent() ? 30 : 6;
        int lineMax = this.getIcon().isPresent() ? 125 : 148;
        int titleColor = this.getTitleColor();

        // Render the toast background
        pGuiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());

        // Split the body text into lines to fit the toast
        List<FormattedCharSequence> bodyLines = pToastComponent.getMinecraft().font.split(this.getBodyText(), lineMax);
        if (bodyLines.size() == 1) {
            // If only one body line is needed, render the title and body together
            pGuiGraphics.drawString(pToastComponent.getMinecraft().font, this.getTitleText(), x, 7, titleColor | 0xFF000000, false);
            pGuiGraphics.drawString(pToastComponent.getMinecraft().font, bodyLines.getFirst(), x, 18, -1, false);
        } else {
            // If more than one body line is needed, render the title first, then the body, fading between the two
            if (pTimeSinceLastVisible < TITLE_TIME) {
                int titleFade = Mth.floor(Mth.clamp((float)(TITLE_TIME - pTimeSinceLastVisible) / FADE_DURATION, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                pGuiGraphics.drawString(pToastComponent.getMinecraft().font, this.getTitleText(), x, 11, titleColor | titleFade, false);
            } else {
                int bodyFade = Mth.floor(Mth.clamp((float)(pTimeSinceLastVisible - TITLE_TIME) / FADE_DURATION, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int y = this.height() / 2 - bodyLines.size() * 9 / 2;
                for (FormattedCharSequence formattedcharsequence : bodyLines) {
                    pGuiGraphics.drawString(pToastComponent.getMinecraft().font, formattedcharsequence, x, y, 16777215 | bodyFade, false);
                    y += 9;
                }
            }
        }

        // Render the toast icon if present
        this.getIcon().ifPresent(iconLoc -> {
            pGuiGraphics.blit(iconLoc, 8, 8, 0, 0, 255, 255);
        });

        return (double)pTimeSinceLastVisible >= DISPLAY_TIME * pToastComponent.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
