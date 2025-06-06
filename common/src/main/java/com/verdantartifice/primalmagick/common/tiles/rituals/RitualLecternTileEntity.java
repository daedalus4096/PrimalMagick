package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Definition of a ritual lectern tile entity.  Holds the lectern's inventory.
 * 
 * @author Daedalus4096
 */
public abstract class RitualLecternTileEntity extends AbstractTileSidedInventoryPM implements IRitualPropTileEntity {
    protected static final int INPUT_INV_INDEX = 0;
    
    protected BlockPos altarPos = null;
    protected boolean isOpen = false;
    
    public RitualLecternTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.RITUAL_LECTERN.get(), pos, state);
    }
    
    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.altarPos = compound.contains("AltarPos", Tag.TAG_LONG) ? BlockPos.of(compound.getLong("AltarPos")) : null;
        this.isOpen = compound.contains("PropOpen", Tag.TAG_BYTE) ? compound.getBoolean("PropOpen") : false;
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
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
    public Optional<Integer> getInventoryIndexForFace(@NotNull Direction face) {
        // Don't connect pipes on any side of the lectern
        return Optional.empty();
    }

    @Override
    protected NonNullList<IItemHandlerPM> createItemHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create output handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .slotLimitFunction(slot -> 1)
                .itemValidFunction((slot, stack) -> false)
                .build());

        return retVal;
    }
}
