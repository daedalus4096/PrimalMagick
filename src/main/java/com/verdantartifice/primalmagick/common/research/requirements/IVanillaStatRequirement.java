package com.verdantartifice.primalmagick.common.research.requirements;

import com.verdantartifice.primalmagick.common.research.IconDefinition;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;

public interface IVanillaStatRequirement {
    Stat<?> getStat();
    ResourceLocation getStatTypeLoc();
    ResourceLocation getStatValueLoc();
    int getThreshold();
    Component getStatDescription();
    IconDefinition getIconDefinition();
}
