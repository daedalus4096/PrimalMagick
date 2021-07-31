package com.verdantartifice.primalmagic.common.containers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/**
 * Fake server data container, not tied to any specific GUI.  Used for server-side calculations that would normally be done in a GUI.
 * 
 * @author Daedalus4096
 */
public class FakeContainer extends AbstractContainerMenu {
    public FakeContainer() {
        super(MenuType.CRAFTING, 0);
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return false;
    }
}
