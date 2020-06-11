package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.misc.DeviceTier;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;

/**
 * Server data container for this basic runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarBasicContainer extends AbstractRunescribingAltarContainer {
    public RunescribingAltarBasicContainer(int id, @Nonnull PlayerInventory playerInv) {
        this(id, playerInv, new Inventory(5));
    }
    
    public RunescribingAltarBasicContainer(int id, @Nonnull PlayerInventory playerInv, @Nonnull IInventory altarInv) {
        super(id, playerInv, altarInv, DeviceTier.BASIC);
    }
}
