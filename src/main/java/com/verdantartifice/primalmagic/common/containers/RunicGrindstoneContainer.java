package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.runes.RuneManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.GrindstoneContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;

/**
 * Server data container for the runic grindstone GUI.
 * 
 * @author Daedalus4096
 */
public class RunicGrindstoneContainer extends GrindstoneContainer {
    protected IWorldPosCallable worldPosCallable;
    protected PlayerEntity player;

    public RunicGrindstoneContainer(int windowId, PlayerInventory playerInv) {
        this(windowId, playerInv, IWorldPosCallable.DUMMY);
    }
    
    public RunicGrindstoneContainer(int windowId, PlayerInventory playerInv, IWorldPosCallable worldPosCallable) {
        super(windowId, playerInv, worldPosCallable);
        this.worldPosCallable = worldPosCallable;
        this.player = playerInv.player;
    }
    
    @Override
    public ItemStack removeEnchantments(ItemStack stack, int damage, int count) {
        ItemStack retVal = super.removeEnchantments(stack, damage, count);
        RuneManager.clearRunes(retVal);
        return retVal;
    }
    
    @Override
    public void updateRecipeOutput() {
        super.updateRecipeOutput();
        this.worldPosCallable.consume((world, pos) -> {
            if (!world.isRemote && this.player instanceof ServerPlayerEntity) {
                ServerPlayerEntity spe = (ServerPlayerEntity)this.player;
                ItemStack stack = this.outputInventory.getStackInSlot(0);
                spe.connection.sendPacket(new SSetSlotPacket(this.windowId, 2, stack));
            }
        });
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.RUNIC_GRINDSTONE.get());
    }
}
