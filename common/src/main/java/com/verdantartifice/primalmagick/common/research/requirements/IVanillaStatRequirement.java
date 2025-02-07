package com.verdantartifice.primalmagick.common.research.requirements;

import com.verdantartifice.primalmagick.common.research.IconDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;

public interface IVanillaStatRequirement {
    Stat<?> getStat();
    ResourceLocation getStatTypeLoc();
    ResourceLocation getStatValueLoc();
    int getCurrentValue(Player player);
    int getThreshold();
    Component getStatDescription();
    IconDefinition getIconDefinition();
}
