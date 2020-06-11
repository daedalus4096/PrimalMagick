package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.misc.DeviceTier;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;

/**
 * Server data container for the enchanted runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarEnchantedContainer extends AbstractRunescribingAltarContainer {
    public RunescribingAltarEnchantedContainer(int id, @Nonnull PlayerInventory playerInv) {
        this(id, playerInv, new Inventory(7));
    }
    
    public RunescribingAltarEnchantedContainer(int id, @Nonnull PlayerInventory playerInv, @Nonnull IInventory altarInv) {
        super(ContainersPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), id, playerInv, altarInv, DeviceTier.ENCHANTED);
    }
}
