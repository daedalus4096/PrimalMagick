package com.verdantartifice.primalmagic.common.tiles.rituals;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagic.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

/**
 * Definition of a ritual altar tile entity.  Provides the core functionality for the corresponding
 * block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.rituals.RitualAltarBlock}
 */
public class RitualAltarTileEntity extends TileInventoryPM implements ITickableTileEntity, IInteractWithWand {
    protected boolean active = false;
    protected int activeCount = 0;
    protected PlayerEntity activePlayerCache = null;
    
    public RitualAltarTileEntity() {
        super(TileEntityTypesPM.RITUAL_ALTAR.get(), 1);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the altar's stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(0));
    }
    
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.active = compound.getBoolean("Active");
        this.activeCount = compound.getInt("ActiveCount");
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("Active", this.active);
        compound.putInt("ActiveCount", this.activeCount);
        return super.write(compound);
    }

    @Override
    public void tick() {
        if (!this.world.isRemote && this.active) {
            this.activeCount++;
            if (this.activeCount >= 100) {
                if (this.activePlayerCache != null) {
                    this.activePlayerCache.sendStatusMessage(new StringTextComponent("Ritual complete!"), false);
                }
                this.active = false;
                this.activePlayerCache = null;
                this.activeCount = 0;
                this.markDirty();
                this.syncTile(false);
            }
        }
    }

    @Override
    public ActionResultType onWandRightClick(ItemStack wandStack, World world, PlayerEntity player, BlockPos pos, Direction direction) {
        if (!this.world.isRemote && wandStack.getItem() instanceof IWand) {
            if (this.active) {
                this.active = false;
                player.sendStatusMessage(new StringTextComponent("Ritual canceled"), false);
            } else {
                this.active = true;
                player.sendStatusMessage(new StringTextComponent("Ritual started!"), false);
            }
            this.activeCount = 0;
            this.activePlayerCache = player;    // TODO store unique id instead
            this.markDirty();
            this.syncTile(false);
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    public void onWandUseTick(ItemStack wandStack, PlayerEntity player, int count) {
        // Do nothing; ritual altars don't support wand channeling
    }
}
