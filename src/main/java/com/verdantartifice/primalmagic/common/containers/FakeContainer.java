package com.verdantartifice.primalmagic.common.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

/**
 * Fake server data container, not tied to any specific GUI.  Used for server-side calculations that would normally be done in a GUI.
 * 
 * @author Daedalus4096
 */
public class FakeContainer extends Container {
    public FakeContainer() {
        super(ContainerType.CRAFTING, 0);
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
