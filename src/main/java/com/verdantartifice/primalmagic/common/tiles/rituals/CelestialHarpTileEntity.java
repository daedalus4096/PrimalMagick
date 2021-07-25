package com.verdantartifice.primalmagic.common.tiles.rituals;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;

import net.minecraft.world.level.block.entity.TickableBlockEntity;

/**
 * Definition of a celestial harp tile entity.
 * 
 * @author Daedalus4096
 */
public class CelestialHarpTileEntity extends AbstractRitualPropTileEntity implements TickableBlockEntity {
    public static final int TICKS_PER_PLAY = 138;   // 6.9s, just under the length of the harp sound effect
    
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
        this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, 0);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.isPlaying = true;
            this.playTicks = 0;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }
}
