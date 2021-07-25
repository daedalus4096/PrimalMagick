package com.verdantartifice.primalmagic.common.tiles.rituals;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;

import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/**
 * Definition of a ritual bell tile entity.
 * 
 * @author Daedalus4096
 */
public class RitualBellTileEntity extends AbstractRitualPropTileEntity implements TickableBlockEntity {
    protected int ringingTicks;
    protected boolean isRinging;
    protected Direction ringDirection;

    public RitualBellTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.RITUAL_BELL.get(), pos, state);
    }
    
    public int getRingingTicks() {
        return this.ringingTicks;
    }
    
    public boolean isRinging() {
        return this.isRinging;
    }
    
    public Direction getRingDirection() {
        return this.ringDirection;
    }

    @Override
    public void tick() {
        if (this.isRinging) {
            this.ringingTicks++;
        }
        if (this.ringingTicks >= 50) {
            this.isRinging = false;
            this.ringingTicks = 0;
        }
    }
    
    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.ringDirection = Direction.from3DDataValue(type);
            this.ringingTicks = 0;
            this.isRinging = true;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }
    
    public void ring(Direction dir) {
        this.ringDirection = dir;
        if (this.isRinging) {
            this.ringingTicks = 0;
        } else {
            this.isRinging = true;
        }
        this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, this.ringDirection.get3DDataValue());
    }
}
