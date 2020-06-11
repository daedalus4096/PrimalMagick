package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.containers.slots.RuneSlot;
import com.verdantartifice.primalmagic.common.misc.DeviceTier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Base server data container for the runescribing altar GUIs.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRunescribingAltarContainer extends Container {
    protected final IInventory altarInv;
    protected final World world;
    protected final DeviceTier tier;

    public AbstractRunescribingAltarContainer(@Nonnull ContainerType<?> type, int id, @Nonnull PlayerInventory playerInv, @Nonnull IInventory altarInv, @Nonnull DeviceTier tier) {
        super(type, id);
        this.altarInv = altarInv;
        this.world = playerInv.player.world;
        this.tier = tier;
        
        // Slot 0: runescribing input
        this.addSlot(new Slot(this.altarInv, 0, 19, 35));
        
        // TODO Slot 1: runescribing output
        
        // Slots 2-10: runes
        if (this.tier.compareTo(DeviceTier.BASIC) >= 0) {
            this.addSlot(new RuneSlot(this.altarInv, 2, 44, 35));
            this.addSlot(new RuneSlot(this.altarInv, 3, 62, 35));
            this.addSlot(new RuneSlot(this.altarInv, 4, 80, 35));
        }
        if (this.tier.compareTo(DeviceTier.ENCHANTED) >= 0) {
            this.addSlot(new RuneSlot(this.altarInv, 5, 62, 17));
            this.addSlot(new RuneSlot(this.altarInv, 6, 62, 53));
        }
        if (this.tier.compareTo(DeviceTier.FORBIDDEN) >= 0) {
            this.addSlot(new RuneSlot(this.altarInv, 7, 44, 17));
            this.addSlot(new RuneSlot(this.altarInv, 8, 80, 17));
        }
        if (this.tier == DeviceTier.HEAVENLY) {
            this.addSlot(new RuneSlot(this.altarInv, 9, 44, 53));
            this.addSlot(new RuneSlot(this.altarInv, 10, 80, 53));
        }
        
        // Slots 11-37: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots 38-46: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }
    
    public DeviceTier getDeviceTier() {
        return this.tier;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.altarInv.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        // TODO Auto-generated method stub
        return super.transferStackInSlot(playerIn, index);
    }
}
