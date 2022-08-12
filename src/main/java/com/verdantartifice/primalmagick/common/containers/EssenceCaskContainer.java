package com.verdantartifice.primalmagick.common.containers;

import com.verdantartifice.primalmagick.common.containers.slots.EssenceSlot;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EssenceCaskContainer extends AbstractContainerMenu {
    protected final Container caskInv;
    protected final Level level;
    
    public EssenceCaskContainer(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(1));
    }
    
    public EssenceCaskContainer(int id, Inventory playerInv, Container caskInv) {
        super(ContainersPM.ESSENCE_CASK.get(), id);
        checkContainerSize(caskInv, 1);
        this.caskInv = caskInv;
        this.level = playerInv.player.level;
        
        // Slots 0: Cask input
        this.addSlot(new EssenceSlot(this.caskInv, 0, 80, 108));
        
        // Slots 1-27: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }

        // Slots 28-36: Player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 198));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.caskInv.stillValid(player);
    }

}
