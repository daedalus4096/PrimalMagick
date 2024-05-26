package com.verdantartifice.primalmagick.common.research.requirements;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;

public interface IVanillaStatRequirement {
    Stat<?> getStat();
    ResourceLocation getStatTypeLoc();
    ResourceLocation getStatValueLoc();
    int getThreshold();
    Component getStatDescription();
    ResourceLocation getIconLocation();
}
