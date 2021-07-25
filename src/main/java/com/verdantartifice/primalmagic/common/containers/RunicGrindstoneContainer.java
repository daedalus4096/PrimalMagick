package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.runes.RuneManager;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.inventory.ContainerLevelAccess;

/**
 * Server data container for the runic grindstone GUI.
 * 
 * @author Daedalus4096
 */
public class RunicGrindstoneContainer extends GrindstoneMenu {
    protected ContainerLevelAccess worldPosCallable;
    protected Player player;

    public RunicGrindstoneContainer(int windowId, Inventory playerInv) {
        this(windowId, playerInv, ContainerLevelAccess.NULL);
    }
    
    public RunicGrindstoneContainer(int windowId, Inventory playerInv, ContainerLevelAccess worldPosCallable) {
        super(windowId, playerInv, worldPosCallable);
        this.worldPosCallable = worldPosCallable;
        this.player = playerInv.player;
    }
    
    @Override
    public ItemStack removeNonCurses(ItemStack stack, int damage, int count) {
        ItemStack retVal = super.removeNonCurses(stack, damage, count);
        RuneManager.clearRunes(retVal);
        return retVal;
    }
    
    @Override
    public void createResult() {
        super.createResult();
        this.worldPosCallable.execute((world, pos) -> {
            if (!world.isClientSide && this.player instanceof ServerPlayer) {
                ServerPlayer spe = (ServerPlayer)this.player;
                ItemStack stack = this.resultSlots.getItem(0);
                spe.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, 2, stack));
            }
        });
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, BlocksPM.RUNIC_GRINDSTONE.get());
    }
}
