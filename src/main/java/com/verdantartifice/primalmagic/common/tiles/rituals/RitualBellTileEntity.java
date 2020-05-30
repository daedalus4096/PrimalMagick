package com.verdantartifice.primalmagic.common.tiles.rituals;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

/**
 * Definition of a ritual bell tile entity.
 * 
 * @author Daedalus4096
 */
public class RitualBellTileEntity extends AbstractRitualPropTileEntity implements ITickableTileEntity {
    protected int ringingTicks;
    protected boolean isRinging;
    protected Direction ringDirection;

    public RitualBellTileEntity() {
        super(TileEntityTypesPM.RITUAL_BELL.get());
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
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.ringDirection = Direction.byIndex(type);
            this.ringingTicks = 0;
            this.isRinging = true;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }
    
    public void ring(Direction dir) {
        this.ringDirection = dir;
        if (this.isRinging) {
            this.ringingTicks = 0;
        } else {
            this.isRinging = true;
        }
        this.world.addBlockEvent(this.getPos(), this.getBlockState().getBlock(), 1, this.ringDirection.getIndex());
    }
}
