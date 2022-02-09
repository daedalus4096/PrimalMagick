package com.verdantartifice.primalmagick.common.tiles.crafting;

import java.util.Arrays;

import com.verdantartifice.primalmagick.common.containers.SpellcraftingAltarContainer;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a spellcrafting altar tile entity.  Holds the tile inventory and controls its
 * custom renderer.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarTileEntity extends TilePM implements MenuProvider {
    protected static final int TICKS_PER_SEGMENT_ROTATION = 10;
    protected static final int TICKS_PER_PAUSE = 20;
    
    protected int phaseTicks = 0;
    protected int nextUpdate = 0;
    protected Segment lastSegment = Segment.U1;
    protected Segment nextSegment = Segment.U1;
    protected RotationPhase currentRotation = RotationPhase.COUNTER_CLOCKWISE_PAUSE;
    
    public SpellcraftingAltarTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.SPELLCRAFTING_ALTAR.get(), pos, state);
    }
    
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new SpellcraftingAltarContainer(windowId, playerInv, ContainerLevelAccess.create(this.level, this.getBlockPos()));
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.phaseTicks = compound.getInt("PhaseTicks");
        this.nextUpdate = compound.getInt("NextUpdate");
        this.lastSegment = Segment.values()[compound.getInt("LastSegmentIndex")];
        this.nextSegment = Segment.values()[compound.getInt("NextSegmentIndex")];
        this.currentRotation = RotationPhase.values()[compound.getInt("CurrentRotationIndex")];
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("PhaseTicks", this.phaseTicks);
        compound.putInt("NextUpdate", this.nextUpdate);
        compound.putInt("LastSegmentIndex", this.lastSegment.ordinal());
        compound.putInt("NextSegmentIndex", this.nextSegment.ordinal());
        compound.putInt("CurrentRotationIndex", this.currentRotation.ordinal());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpellcraftingAltarTileEntity entity) {
        if (level.isClientSide && entity.phaseTicks++ >= entity.nextUpdate) {
            entity.nextRotationPhase();
        }
    }
    
    protected void nextRotationPhase() {
        this.currentRotation = this.currentRotation.getNext();
        if (!this.currentRotation.isPause()) {
            this.lastSegment = this.nextSegment;
            Segment[] nextPossibleSegments = Arrays.stream(Segment.values()).filter(s -> !s.equals(this.lastSegment)).toArray(Segment[]::new);
            this.nextSegment = nextPossibleSegments[this.level.random.nextInt(nextPossibleSegments.length)];
        }
        this.nextUpdate = this.currentRotation.getDuration(this.lastSegment, this.nextSegment);
        this.phaseTicks = 0;
    }
    
    public float getCurrentRotation(float partialTicks) {
        if (this.nextUpdate == 0) {
            return 0F;
        } else if (this.currentRotation.isPause()) {
            return (float)this.nextSegment.getDegreeOffset();
        } else {
            return Mth.lerp((this.phaseTicks + partialTicks) / (float)this.nextUpdate, this.lastSegment.getDegreeOffset(), this.nextSegment.getDegreeTarget(this.lastSegment, this.currentRotation));
        }
    }

    protected static enum Segment {
        U1(0),
        V1(45),
        T1(90),
        D1(135),
        U2(180),
        V2(225),
        T2(270),
        D2(315);
        
        private final int degreeOffset;
        
        private Segment(int degrees) {
            this.degreeOffset = degrees;
        }
        
        public int getDegreeOffset() {
            return this.degreeOffset;
        }
        
        public int getDegreeTarget(Segment last, RotationPhase rotation) {
            if (rotation.isReverse() && this.degreeOffset >= last.degreeOffset) {
                return this.degreeOffset - 360;
            } else if (!rotation.isReverse() && this.degreeOffset <= last.degreeOffset) {
                return this.degreeOffset + 360;
            } else {
                return this.degreeOffset;
            }
        }
    }
    
    protected static enum RotationPhase {
        CLOCKWISE(false, false),
        CLOCKWISE_PAUSE(false, true),
        COUNTER_CLOCKWISE(true, false),
        COUNTER_CLOCKWISE_PAUSE(true, true);
        
        private final boolean reverse;
        private final boolean pause;
        
        private RotationPhase(boolean reverse, boolean pause) {
            this.reverse = reverse;
            this.pause = pause;
        }
        
        public boolean isReverse() {
            return this.reverse;
        }
        
        public boolean isPause() {
            return this.pause;
        }
        
        public int getDuration(Segment last, Segment next) {
            switch (this) {
            case CLOCKWISE:
            case COUNTER_CLOCKWISE:
                int segmentDelta = Math.abs(next.getDegreeTarget(last, this) - last.getDegreeOffset()) / 45;
                return TICKS_PER_SEGMENT_ROTATION * segmentDelta;
            case CLOCKWISE_PAUSE:
            case COUNTER_CLOCKWISE_PAUSE:
                return TICKS_PER_PAUSE;
            default:
                throw new IndexOutOfBoundsException("No such rotation phase!");
            }
        }
        
        public RotationPhase getNext() {
            switch (this) {
            case CLOCKWISE:
                return CLOCKWISE_PAUSE;
            case CLOCKWISE_PAUSE:
                return COUNTER_CLOCKWISE;
            case COUNTER_CLOCKWISE:
                return COUNTER_CLOCKWISE_PAUSE;
            case COUNTER_CLOCKWISE_PAUSE:
                return CLOCKWISE;
            default:
                throw new IndexOutOfBoundsException("No such rotation phase!");
            }
        }
    }
}
