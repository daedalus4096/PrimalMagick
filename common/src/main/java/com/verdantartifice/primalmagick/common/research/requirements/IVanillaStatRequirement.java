package com.verdantartifice.primalmagick.common.research.requirements;

import com.verdantartifice.primalmagick.common.misc.IconDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;

public interface IVanillaStatRequirement {
    Stat<?> getStat();
    Identifier getStatTypeLoc();
    Identifier getStatValueLoc();
    int getCurrentValue(Player player);
    int getThreshold();
    Component getStatDescription();
    IconDefinition getIconDefinition();
}
