package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.blocks.rituals.EntropySinkBlock;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an entropy sink tile entity.
 * 
 * @author Daedalus4096
 */
public class EntropySinkTileEntity extends AbstractRitualPropTileEntity {
    public static final int TICKS_PER_GLOW = 1200;  // 60s
    
    protected int glowTicks;
    protected boolean isGlowing;
    
    public EntropySinkTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.ENTROPY_SINK.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EntropySinkTileEntity entity) {
        if (entity.isGlowing) {
            entity.glowTicks++;
        }
        if (entity.glowTicks >= TICKS_PER_GLOW) {
            entity.isGlowing = false;
            entity.glowTicks = 0;
            level.setBlock(pos, state.setValue(EntropySinkBlock.LIT, Boolean.FALSE), Block.UPDATE_ALL_IMMEDIATE);
        }
    }
    
    public int getGlowTicks() {
        return this.glowTicks;
    }
    
    public boolean isGlowing() {
        return this.isGlowing;
    }
    
    public void startGlowing() {
        if (this.isGlowing) {
            this.glowTicks = 0;
        } else {
            this.isGlowing = true;
        }
        this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, 0);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.isGlowing = true;
            this.glowTicks = 0;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }
}
