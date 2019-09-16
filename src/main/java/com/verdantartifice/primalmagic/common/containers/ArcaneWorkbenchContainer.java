package com.verdantartifice.primalmagic.common.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public class ArcaneWorkbenchContainer extends Container {
    public ArcaneWorkbenchContainer(int windowId) {
        super(ContainersPM.ARCANE_WORKBENCH, windowId);
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        // TODO method stub
        return true;
    }
}
