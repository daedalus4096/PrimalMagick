package com.verdantartifice.primalmagick.common.containers;

import com.verdantartifice.primalmagick.common.containers.slots.EssenceSlot;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EssenceCaskContainer extends AbstractContainerMenu {
    protected final Container caskInv;
    protected final ContainerData caskData;
    protected final Level level;
    protected final Slot inputSlot;
    protected final BlockPos tilePos;

    public EssenceCaskContainer(int id, Inventory playerInv, BlockPos pos) {
        this(id, playerInv, new SimpleContainer(1), new SimpleContainerData(EssenceCaskTileEntity.NUM_SLOTS), pos);
    }
    
    public EssenceCaskContainer(int id, Inventory playerInv, Container caskInv, ContainerData caskData, BlockPos pos) {
        super(ContainersPM.ESSENCE_CASK.get(), id);
        checkContainerSize(caskInv, 1);
        checkContainerDataCount(caskData, EssenceCaskTileEntity.NUM_SLOTS);
        this.caskInv = caskInv;
        this.caskData = caskData;
        this.level = playerInv.player.level;
        this.tilePos = pos;
        
        // Slot 0: Cask input
        this.inputSlot = this.addSlot(new EssenceSlot(this.caskInv, 0, 80, 108));
        
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
        
        this.addDataSlots(this.caskData);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring an item in the input slot, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 1, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inputSlot.mayPlace(slotStack)) {
                // If transferring a valid essence, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 1 && index < 28) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.moveItemStackTo(slotStack, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 37) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.moveItemStackTo(slotStack, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            
            slot.setChanged();
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, slotStack);
            this.broadcastChanges();
        }
        
        return stack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.caskInv.stillValid(player);
    }

    public int getEssenceCount(int index) {
        return this.caskData.get(index);
    }
    
    public BlockPos getTilePos() {
        return this.tilePos;
    }
    
    public int getTotalEssenceCount() {
        int retVal = 0;
        for (int index = 0; index < this.caskData.getCount(); index++) {
            retVal += this.caskData.get(index);
        }
        return retVal;
    }
    
    public int getTotalEssenceCapacity() {
        BlockEntity tile = this.level.getBlockEntity(this.tilePos);
        if (tile instanceof EssenceCaskTileEntity caskTile) {
            return caskTile.getTotalEssenceCapacity();
        } else {
            return 0;
        }
    }
    
    public boolean isEssenceTypeVisible(EssenceType type, Player player) {
        if (type == null) {
            return false;
        }
        
        // If the essence type's synthesis research has been discovered, then it's visible
        if (type.isDiscovered(player)) {
            return true;
        }
        
        // If the research has not been discovered, but there's an essence of that type in the cask, then it's visible
        int typeIndex = type.ordinal();
        int rowSize = Source.SORTED_SOURCES.size();
        for (int index = 0; index < this.caskData.getCount(); index++) {
            if (index / rowSize == typeIndex && this.caskData.get(index) > 0) {
                return true;
            }
        }
        
        // Otherwise, it's not visible
        return false;
    }
    
    public boolean isEssenceSourceVisible(Source source, Player player) {
        if (source == null) {
            return false;
        }
        
        // If the essence source has been discovered, then it's visible
        if (source.isDiscovered(player)) {
            return true;
        }
        
        // If the source has not been discovered, but there's an essence of that source in the cask, then it's visible
        int sourceIndex = Source.SORTED_SOURCES.indexOf(source);
        int rowSize = Source.SORTED_SOURCES.size();
        for (int index = 0; index < this.caskData.getCount(); index++) {
            if (index % rowSize == sourceIndex && this.caskData.get(index) > 0) {
                return true;
            }
        }
        
        // Otherwise, it's not visible
        return false;
    }
}
