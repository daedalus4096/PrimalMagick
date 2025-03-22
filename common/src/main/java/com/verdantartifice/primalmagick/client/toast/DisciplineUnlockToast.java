package com.verdantartifice.primalmagick.client.toast;

import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class DisciplineUnlockToast extends AbstractToastPM {
    protected static final ResourceLocation BACKGROUND_SPRITE = ResourceUtils.loc("toast/discipline");

    protected final ResearchDiscipline discipline;

    public DisciplineUnlockToast(ResearchDiscipline discipline) {
        this.discipline = discipline;
    }

    @Override
    protected ResourceLocation getBackgroundSprite() {
        return BACKGROUND_SPRITE;
    }

    @Override
    protected Component getTitleText() {
        return Component.translatable("label.primalmagick.toast.discipline.title");
    }

    @Override
    protected Optional<Component> getSubtitleText() {
        return Optional.of(Component.translatable(this.discipline.getNameTranslationKey()));
    }

    @Override
    protected Component getBodyText() {
        return Component.translatable("label.primalmagick.toast.discipline.body");
    }

    @Override
    protected int getTitleColor() {
        return Sources.SUN.getColor();
    }

    @Override
    protected int getSubtitleColor() {
        return -1;
    }

    @Override
    protected int getBodyColor() {
        return -1;
    }

    @Override
    protected Optional<ResourceLocation> getIcon() {
        return Optional.of(this.discipline.iconLocation());
    }
}
