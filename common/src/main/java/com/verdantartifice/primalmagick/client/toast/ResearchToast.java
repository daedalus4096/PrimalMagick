package com.verdantartifice.primalmagick.client.toast;

import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * GUI element for the toast that shows when you complete a research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchToast extends AbstractToastPM {
    protected final ResearchEntry entry;
    protected final boolean isComplete;
    
    public ResearchToast(ResearchEntry entry, boolean isComplete) {
        this.entry = entry;
        this.isComplete = isComplete;
    }

    @Override
    protected Component getTitleText() {
        return this.isComplete ? Component.translatable("label.primalmagick.toast.completed") : Component.translatable("label.primalmagick.toast.revealed");
    }

    @Override
    protected Component getBodyText() {
        return Component.translatable(this.entry.getNameTranslationKey());
    }

    @Override
    protected int getTitleColor() {
        return this.isComplete ? Sources.VOID.getColor() : Sources.INFERNAL.getColor();
    }

    @Override
    protected Optional<ResourceLocation> getIcon() {
        return Optional.empty();
    }
}
