package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

/**
 * GUI button to navigate to the next or previous page of the current grimoire entry.
 * 
 * @author Daedalus4096
 */
public class PageButton extends Button {
    private static final Identifier NEXT_PAGE = ResourceUtils.loc("grimoire/next_page");
    private static final Identifier PREV_PAGE = ResourceUtils.loc("grimoire/previous_page");

    protected GrimoireScreen screen;
    protected boolean isNext;

    public PageButton(int x, int y, GrimoireScreen screen, boolean isNext) {
        super(x, y, 12, 5, Component.empty(), new Handler(), Button.DEFAULT_NARRATION);
        this.screen = screen;
        this.isNext = isNext;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    public boolean isNext() {
        return this.isNext;
    }
    
    @Override
    public void renderContents(@NotNull GuiGraphicsExtractor guiGraphics, int pRenderButton1, int pRenderButton2, float pRenderButton3) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            guiGraphics.pose().pushMatrix();

            // When hovered, scale the button up and down to create a pulsing effect
            float scaleMod = this.isHoveredOrFocused() ? Mth.sin(mc.player.tickCount / 3.0F) * 0.2F + 0.1F : 0.0F;
            int dx = this.width / 2;
            int dy = this.height / 2;
            guiGraphics.pose().translate(this.getX() + dx, this.getY() + dy);
            guiGraphics.pose().scale(1.5F + scaleMod, 1.5F + scaleMod);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.isNext ? NEXT_PAGE : PREV_PAGE, -dx, -dy, this.width, this.height);

            guiGraphics.pose().popMatrix();
        }
}
    
    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(@NotNull Button button) {
            if (button instanceof PageButton gpb) {
                if (gpb.isNext()) {
                    gpb.getScreen().nextPage();
                } else {
                    gpb.getScreen().prevPage();
                }
            }
        }
    }
}
