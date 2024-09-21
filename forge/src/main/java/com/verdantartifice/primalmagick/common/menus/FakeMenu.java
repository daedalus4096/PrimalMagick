package com.verdantartifice.primalmagick.common.menus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

/**
 * Fake server data container, not tied to any specific GUI.  Used for server-side calculations that would normally be done in a GUI.
 * 
 * @author Daedalus4096
 */
public class FakeMenu extends AbstractContainerMenu {
    public FakeMenu() {
        super(MenuType.CRAFTING, 0);
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
