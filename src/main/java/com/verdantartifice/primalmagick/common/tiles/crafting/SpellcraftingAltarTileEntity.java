package com.verdantartifice.primalmagick.common.tiles.crafting;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.verdantartifice.primalmagick.common.blocks.crafting.SpellcraftingAltarBlock;
import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellcraftingRunePacket;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a spellcrafting altar tile entity.  Holds the tile inventory and controls its
 * custom renderer.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarTileEntity extends AbstractTilePM implements MenuProvider {
    public static final int BOB_CYCLE_TIME_TICKS = 200;
    protected static final int TICKS_PER_SEGMENT_ROTATION = 10;
    protected static final int TICKS_PER_PAUSE = 20;
    protected static final List<Source> ALLOWED_SOURCES = Arrays.asList(Source.EARTH, Source.SEA, Source.SKY, Source.SUN, Source.MOON);
    
    protected int phaseTicks = 0;
    protected int nextUpdate = 0;
    protected Segment lastSegment = Segment.U1;
    protected Segment nextSegment = Segment.U1;
    protected RotationPhase currentRotation = RotationPhase.COUNTER_CLOCKWISE_PAUSE;
    protected Source lastSource = Source.EARTH;
    protected Source nextSource = Source.EARTH;
    
    public SpellcraftingAltarTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.SPELLCRAFTING_ALTAR.get(), pos, state);
    }
    
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new SpellcraftingAltarMenu(windowId, playerInv, this.getBlockPos(), this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        
        this.phaseTicks = compound.getInt("PhaseTicks");
        this.nextUpdate = compound.getInt("NextUpdate");
        this.lastSegment = Segment.values()[compound.getInt("LastSegmentIndex")];
        this.nextSegment = Segment.values()[compound.getInt("NextSegmentIndex")];
        this.currentRotation = RotationPhase.values()[compound.getInt("CurrentRotationIndex")];
        
        Source last = Source.getSource(compound.getString("LastSource"));
        this.lastSource = last == null ? Source.EARTH : last;
        Source next = Source.getSource(compound.getString("NextSource"));
        this.nextSource = next == null ? Source.EARTH : next;
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("PhaseTicks", this.phaseTicks);
        compound.putInt("NextUpdate", this.nextUpdate);
        compound.putInt("LastSegmentIndex", this.lastSegment.ordinal());
        compound.putInt("NextSegmentIndex", this.nextSegment.ordinal());
        compound.putInt("CurrentRotationIndex", this.currentRotation.ordinal());
        compound.putString("LastSource", this.lastSource.getTag());
        compound.putString("NextSource", this.nextSource.getTag());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpellcraftingAltarTileEntity entity) {
        if (entity.phaseTicks++ >= entity.nextUpdate && !level.isClientSide) {
            entity.nextRotationPhase();
        }
    }
    
    protected void nextRotationPhase() {
        this.currentRotation = this.currentRotation.getNext();
        if (!this.currentRotation.isPause()) {
            this.lastSegment = this.nextSegment;
            Segment[] nextPossibleSegments = Arrays.stream(Segment.values()).filter(s -> !s.equals(this.lastSegment)).toArray(Segment[]::new);
            this.nextSegment = nextPossibleSegments[this.level.random.nextInt(nextPossibleSegments.length)];
            
            this.lastSource = this.nextSource;
            Source[] nextPossibleSources = ALLOWED_SOURCES.stream().filter(s -> !s.equals(this.lastSource)).toArray(Source[]::new);
            this.nextSource = nextPossibleSources[this.level.random.nextInt(nextPossibleSources.length)];
        }
        this.nextUpdate = this.currentRotation.getDuration(this.lastSegment, this.nextSegment);
        this.phaseTicks = 0;
        
        this.setChanged();
        this.syncTile(true);
        
        if (!this.level.isClientSide && this.currentRotation.isPause()) {
            this.emitRuneParticle();
        }
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
    
    public Color getCurrentColor(float partialTicks) {
        if (this.nextUpdate == 0) {
            return new Color(Source.EARTH.getColor());
        } else if (this.currentRotation.isPause()) {
            return new Color(this.nextSource.getColor());
        } else {
            Color last = new Color(this.lastSource.getColor());
            Color next = new Color(this.nextSource.getColor());
            float blend = (this.phaseTicks + partialTicks) / (float)this.nextUpdate;
            float inverse = 1F - blend;
            float r = (next.getRed() * blend) + (last.getRed() * inverse);
            float g = (next.getGreen() * blend) + (last.getGreen() * inverse);
            float b = (next.getBlue() * blend) + (last.getBlue() * inverse);
            return new Color(Mth.clamp(r / 255F, 0, 1F), Mth.clamp(g / 255F, 0, 1F), Mth.clamp(b / 255F, 0, 1F));
        }
    }
    
    protected void emitRuneParticle() {
        Vec3 center = Vec3.upFromBottomCenterOf(this.worldPosition, 1.1875D);
        Vec3 facingNormal = Vec3.atLowerCornerOf(this.getBlockState().getValue(SpellcraftingAltarBlock.FACING).getNormal());
        Vec3 centerOffset = facingNormal.scale(0.5D);
        Vec3 movement = facingNormal.scale(0.05D);
        long time = this.getLevel().getLevelData().getGameTime();
        double bobDelta = 0.125D * Math.sin(time * (2D * Math.PI / (double)BOB_CYCLE_TIME_TICKS));
        
        PacketHandler.sendToAllAround(
                new SpellcraftingRunePacket(this.nextSegment, center.x + centerOffset.x, center.y + bobDelta, center.z + centerOffset.z, movement.x, 0D, movement.z, this.nextSource.getColor()), 
                this.level.dimension(), 
                this.worldPosition, 
                64.0D);
    }
    
    public static enum Segment {
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
