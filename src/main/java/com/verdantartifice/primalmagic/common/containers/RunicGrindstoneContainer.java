package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.runes.RuneManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.GrindstoneContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;

/**
 * Server data container for the runic grindstone GUI.
 * 
 * @author Daedalus4096
 */
public class RunicGrindstoneContainer extends GrindstoneContainer {
    protected IWorldPosCallable worldPosCallable;

    public RunicGrindstoneContainer(int windowId, PlayerInventory playerInv) {
        this(windowId, playerInv, IWorldPosCallable.DUMMY);
    }
    
    public RunicGrindstoneContainer(int windowId, PlayerInventory playerInv, IWorldPosCallable worldPosCallable) {
        super(windowId, playerInv, worldPosCallable);
        this.worldPosCallable = worldPosCallable;
    }
    
    @Override
    public ItemStack removeEnchantments(ItemStack stack, int damage, int count) {
        ItemStack retVal = super.removeEnchantments(stack, damage, count);
        RuneManager.clearRunes(retVal);
        return retVal;
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.RUNIC_GRINDSTONE.get());
    }
}
