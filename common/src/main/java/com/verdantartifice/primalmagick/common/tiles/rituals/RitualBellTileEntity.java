package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a ritual bell tile entity.
 * 
 * @author Daedalus4096
 */
public class RitualBellTileEntity extends AbstractRitualPropTileEntity {
    protected int ringingTicks;
    protected boolean isRinging;
    protected Direction ringDirection;

    public RitualBellTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.RITUAL_BELL.get(), pos, state);
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

    public static void tick(Level level, BlockPos pos, BlockState state, RitualBellTileEntity entity) {
        if (entity.isRinging) {
            entity.ringingTicks++;
        }
        if (entity.ringingTicks >= 50) {
            entity.isRinging = false;
            entity.ringingTicks = 0;
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
