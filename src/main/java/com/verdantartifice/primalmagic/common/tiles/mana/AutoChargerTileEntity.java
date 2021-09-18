package com.verdantartifice.primalmagic.common.tiles.mana;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an auto-charger tile entity.  Provides the siphoning functionality for the
 * corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.mana.AutoChargerBlock}
 */
public class AutoChargerTileEntity extends TileInventoryPM {
    protected int chargeTime;

    public AutoChargerTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.AUTO_CHARGER.get(), pos, state, 1);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the charger's wand stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(0));
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.chargeTime = compound.getInt("ChargeTime");
    }
    
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("ChargeTime", this.chargeTime);
        return super.save(compound);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutoChargerTileEntity entity) {
        // FIXME Remove when Forge onLoad bug is fixed
        if (entity.ticksExisted == 0) {
            entity.doInventorySync();
        }
        entity.ticksExisted++;

    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        boolean flag = !stack.isEmpty() && stack.sameItem(slotStack) && ItemStack.tagMatches(stack, slotStack);
        if (index == 0 && !flag) {
            this.chargeTime = 0;
            this.setChanged();
        }
    }
}
