package com.verdantartifice.primalmagick.common.menus.slots;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;

/**
 * Interface marking a GUI slot that has one or more backgrounds to be cycled through that should
 * be rendered under certain circumstances.
 * 
 * @author Daedalus4096
 */
public interface IHasCyclingBackgrounds {
    Pair<ResourceLocation, ResourceLocation> getNoItemIcon();
    void tickBackgrounds();
}
