package com.verdantartifice.primalmagick.common.tiles.rituals;

import java.util.OptionalInt;

import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Definition of a ritual lectern tile entity.  Holds the lectern's inventory.
 * 
 * @author Daedalus4096
 */
public class RitualLecternTileEntity extends AbstractTileSidedInventoryPM implements IRitualPropTileEntity {
    protected static final int INPUT_INV_INDEX = 0;
    
    protected BlockPos altarPos = null;
    protected boolean isOpen = false;
    
    public RitualLecternTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.RITUAL_LECTERN.get(), pos, state);
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.altarPos = compound.contains("AltarPos", Tag.TAG_LONG) ? BlockPos.of(compound.getLong("AltarPos")) : null;
        this.isOpen = compound.contains("PropOpen", Tag.TAG_BYTE) ? compound.getBoolean("PropOpen") : false;
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.altarPos != null) {
            compound.putLong("AltarPos", this.altarPos.asLong());
        }
        compound.putBoolean("PropOpen", this.isOpen);
    }
    
    @Override
    public boolean isPropOpen() {
        return this.isOpen;
    }

    @Override
    public void setPropOpen(boolean open) {
        this.isOpen = open;
    }

    @Override
    public BlockPos getAltarPos() {
        return this.altarPos;
    }

    @Override
    public void setAltarPos(BlockPos pos) {
        this.altarPos = pos;
        this.setChanged();
    }

    @Override
    public void notifyAltarOfPropActivation(float stabilityBonus) {
        if (this.altarPos != null) {
            BlockEntity tile = this.level.getBlockEntity(this.altarPos);
            if (tile instanceof RitualAltarTileEntity) {
                ((RitualAltarTileEntity)tile).onPropActivation(this.worldPosition, stabilityBonus);
            }
        }
    }

    public ItemStack getItem() {
        return this.getItem(INPUT_INV_INDEX, 0);
    }
    
    public void setItem(ItemStack stack) {
        this.setItem(INPUT_INV_INDEX, 0, stack);
    }
    
    @Override
    protected int getInventoryCount() {
        return 1;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return inventoryIndex == INPUT_INV_INDEX ? 1 : 0;
    }

    @Override
    protected OptionalInt getInventoryIndexForFace(Direction face) {
        // Don't connect pipes on any side of the lectern
        return OptionalInt.empty();
    }

    @Override
    protected NonNullList<ItemStackHandler> createHandlers() {
        NonNullList<ItemStackHandler> retVal = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandlerPM(this));
        
        // Create output handler
        retVal.set(INPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(INPUT_INV_INDEX), this) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return false;
            }
        });

        return retVal;
    }

    @Override
    protected void loadLegacyItems(NonNullList<ItemStack> legacyItems) {
        // Slot 0 was the input item stack
        this.setItem(INPUT_INV_INDEX, 0, legacyItems.get(0));
    }
}
