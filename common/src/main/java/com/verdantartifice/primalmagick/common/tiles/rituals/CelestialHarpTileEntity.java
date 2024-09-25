package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a celestial harp tile entity.
 * 
 * @author Daedalus4096
 */
public class CelestialHarpTileEntity extends AbstractRitualPropTileEntity {
    public static final int TICKS_PER_PLAY = 138;   // 6.9s, just under the length of the harp sound effect
    
    protected int playTicks;
    protected boolean isPlaying;
    
    public CelestialHarpTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.CELESTIAL_HARP.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CelestialHarpTileEntity entity) {
        if (entity.isPlaying) {
            entity.playTicks++;
        }
        if (entity.playTicks >= TICKS_PER_PLAY) {
            entity.isPlaying = false;
            entity.playTicks = 0;
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
