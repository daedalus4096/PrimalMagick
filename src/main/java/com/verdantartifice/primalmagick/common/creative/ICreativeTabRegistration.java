package com.verdantartifice.primalmagick.common.creative;

import net.minecraft.world.item.CreativeModeTab;

public interface ICreativeTabRegistration {
    void register(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output);
}
