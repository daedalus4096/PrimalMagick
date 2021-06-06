package com.verdantartifice.primalmagic.common.tiles.rituals;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;

import net.minecraft.tileentity.ITickableTileEntity;

/**
 * Definition of a celestial harp tile entity.
 * 
 * @author Daedalus4096
 */
public class CelestialHarpTileEntity extends AbstractRitualPropTileEntity implements ITickableTileEntity {
    protected static final int TICKS_PER_PLAY = 138;    // 6.9s
    
    protected int playTicks;
    protected boolean isPlaying;
    
    public CelestialHarpTileEntity() {
        super(TileEntityTypesPM.CELESTIAL_HARP.get());
    }

    @Override
    public void tick() {
        if (this.isPlaying) {
            this.playTicks++;
        }
        if (this.playTicks >= TICKS_PER_PLAY) {
            this.isPlaying = false;
            this.playTicks = 0;
        }
    }

    public int getPlayTicks() {
        return this.playTicks;
    }
    
    public boolean isPlaying() {
        return this.isPlaying;
    }
    
    public void startPlaying() {
        if (this.isPlaying) {
            this.playTicks = 0;
        } else {
            this.isPlaying = true;
        }
        this.world.addBlockEvent(this.getPos(), this.getBlockState().getBlock(), 1, 0);
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.isPlaying = true;
            this.playTicks = 0;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }
}
