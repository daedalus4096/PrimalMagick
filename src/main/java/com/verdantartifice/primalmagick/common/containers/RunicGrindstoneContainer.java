package com.verdantartifice.primalmagick.common.containers;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.runes.RuneManager;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
        ItemStack itemstack = this.repairSlots.getItem(0);
        ItemStack itemstack1 = this.repairSlots.getItem(1);
        boolean flag = !itemstack.isEmpty() || !itemstack1.isEmpty();
        boolean flag1 = !itemstack.isEmpty() && !itemstack1.isEmpty();
        
        if (!flag) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            // This flag is the whole reason we're re-implementing the entire method, instead of delegating to super, so that we can remove runes from unenchanted items; ugh
            boolean flag2 = !itemstack.isEmpty() && !itemstack.is(Items.ENCHANTED_BOOK) && !itemstack.isEnchanted() && !RuneManager.hasRunes(itemstack) || 
                            !itemstack1.isEmpty() && !itemstack1.is(Items.ENCHANTED_BOOK) && !itemstack1.isEnchanted() && !RuneManager.hasRunes(itemstack1);
            if (itemstack.getCount() > 1 || itemstack1.getCount() > 1 || !flag1 && flag2) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.broadcastChanges();
                return;
            }

            int j = 1;
            int i;
            ItemStack itemstack2;
            if (flag1) {
                if (!itemstack.is(itemstack1.getItem())) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.broadcastChanges();
                    return;
                }

                int k = itemstack.getMaxDamage() - itemstack.getDamageValue();
                int l = itemstack.getMaxDamage() - itemstack1.getDamageValue();
                int i1 = k + l + itemstack.getMaxDamage() * 5 / 100;
                i = Math.max(itemstack.getMaxDamage() - i1, 0);
                itemstack2 = this.mergeEnchants(itemstack, itemstack1);
                if (!itemstack2.isRepairable()) i = itemstack.getDamageValue();
                if (!itemstack2.isDamageableItem() || !itemstack2.isRepairable()) {
                    if (!ItemStack.matches(itemstack, itemstack1)) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.broadcastChanges();
                        return;
                    }
                    j = 2;
                }
            } else {
                boolean flag3 = !itemstack.isEmpty();
                i = flag3 ? itemstack.getDamageValue() : itemstack1.getDamageValue();
                itemstack2 = flag3 ? itemstack : itemstack1;
            }

            this.resultSlots.setItem(0, this.removeNonCurses(itemstack2, i, j));
        }

        this.broadcastChanges();
        
        this.worldPosCallable.execute((world, pos) -> {
            if (!world.isClientSide && this.player instanceof ServerPlayer spe) {
                ItemStack stack = this.resultSlots.getItem(0);
                spe.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 2, stack));
            }
        });
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, BlocksPM.RUNIC_GRINDSTONE.get());
    }
}
