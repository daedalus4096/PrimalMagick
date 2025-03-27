package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.Optional;

public class MarkReadButton extends Button {
    protected static final ResourceLocation SPRITE = ResourceUtils.loc("grimoire/mark_all_as_read");
    protected static final int BUTTON_WIDTH = 10;
    protected static final int BUTTON_HEIGHT = 10;
    protected static final int SPRITE_WIDTH = 32;
    protected static final int SPRITE_HEIGHT = 32;
    protected static final float BASE_SCALE = (float)BUTTON_WIDTH / (float)SPRITE_WIDTH;

    protected final Optional<ResearchDiscipline> disciplineOpt;

    public MarkReadButton(int pX, int pY) {
        this(pX, pY, null);
    }

    public MarkReadButton(int pX, int pY, @Nullable ResearchDiscipline discipline) {
        super(pX, pY, BUTTON_WIDTH, BUTTON_HEIGHT, Component.empty(), new Handler(), Button.DEFAULT_NARRATION);
        this.disciplineOpt = Optional.ofNullable(discipline);
        this.setTooltip(Tooltip.create(
                this.disciplineOpt.map(d -> Component.translatable("tooltip.primalmagick.mark_read.discipline", Component.translatable(d.getNameTranslationKey())))
                        .orElse(Component.translatable("tooltip.primalmagick.mark_read.all"))));
    }

    public Optional<ResearchDiscipline> getDiscipline() {
        return this.disciplineOpt;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();

        // When hovered, scale the button up and down to create a pulsing effect
        float scaleMod = this.isHoveredOrFocused() ? Mth.sin(mc.player.tickCount / 3.0F) * 0.2F + 1.1F : 1.0F;
        int dx = this.width / 2;
        int dy = this.height / 2;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(this.getX() + dx, this.getY() + dy, 10.0F);
        pGuiGraphics.pose().scale(BASE_SCALE, BASE_SCALE, 1F);
        pGuiGraphics.pose().scale(scaleMod, scaleMod, 1F);
        pGuiGraphics.blitSprite(SPRITE, (int)(-dx / BASE_SCALE), (int)(-dy / BASE_SCALE), SPRITE_WIDTH, SPRITE_HEIGHT);
        pGuiGraphics.pose().popPose();
    }

    @Override
    public void playDownSound(SoundManager pHandler) {
        pHandler.play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof MarkReadButton mrb) {
                LogUtils.getLogger().warn("Marking all as read for {}", mrb.getDiscipline().map(d -> d.key().getRootKey().location().getPath()).orElse("all disciplines"));
            }
        }
    }
}
