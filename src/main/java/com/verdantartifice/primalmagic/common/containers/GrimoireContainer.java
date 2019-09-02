package com.verdantartifice.primalmagic.common.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public class GrimoireContainer extends Container {
    public GrimoireContainer(int windowId) {
        super(ContainersPM.GRIMOIRE, windowId);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
