package com.verdantartifice.primalmagick.common.menus;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.tiles.crafting.RunescribingAltarTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * Server data container for the enchanted runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarEnchantedMenu extends AbstractRunescribingAltarMenu {
    protected static final int RUNE_CAPACITY = 5;
    
    public RunescribingAltarEnchantedMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, null);
    }

    public RunescribingAltarEnchantedMenu(int id, @Nonnull Inventory playerInv, BlockPos tilePos, RunescribingAltarTileEntity altar) {
        super(MenuTypesPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), id, playerInv, tilePos, altar);
    }
    
    @Override
    protected int getRuneCapacity() {
        return RUNE_CAPACITY;
    }
    
    @Override
    protected Slot addRuneSlots() {
        this.addSlot(makeRuneSlot(this.altarInv, 1, 62, 17));
        this.addSlot(makeRuneSlot(this.altarInv, 2, 44, 35));
        this.addSlot(makeRuneSlot(this.altarInv, 3, 62, 35));
        this.addSlot(makeRuneSlot(this.altarInv, 4, 80, 35));
        return this.addSlot(makeRuneSlot(this.altarInv, 5, 62, 53));
    }
}
