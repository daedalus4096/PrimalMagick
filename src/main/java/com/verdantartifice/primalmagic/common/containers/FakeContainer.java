package com.verdantartifice.primalmagic.common.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class FakeContainer extends Container {
    public FakeContainer() {
        super(ContainerType.CRAFTING, 0);
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
