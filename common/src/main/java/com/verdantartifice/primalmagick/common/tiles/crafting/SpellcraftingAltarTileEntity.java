package com.verdantartifice.primalmagick.common.tiles.crafting;

import com.verdantartifice.primalmagick.common.blocks.crafting.SpellcraftingAltarBlock;
import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellcraftingRunePacket;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

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
    protected static final List<Source> ALLOWED_SOURCES = Arrays.asList(Sources.EARTH, Sources.SEA, Sources.SKY, Sources.SUN, Sources.MOON);
    
    protected int phaseTicks = 0;
    protected int nextUpdate = 0;
    protected Segment lastSegment = Segment.U1;
    protected Segment nextSegment = Segment.U1;
    protected RotationPhase currentRotation = RotationPhase.COUNTER_CLOCKWISE_PAUSE;
    protected Source lastSource = Sources.EARTH;
    protected Source nextSource = Sources.EARTH;
    
    public SpellcraftingAltarTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.SPELLCRAFTING_ALTAR.get(), pos, state);
    }
    
    @Override
    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory playerInv, @NotNull Player player) {
        return new SpellcraftingAltarMenu(windowId, playerInv, this.getBlockPos(), this);
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
    
    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        Segment[] segments = Segment.values();
        RotationPhase[] phases = RotationPhase.values();

        this.phaseTicks = input.getIntOr("PhaseTicks", 0);
        this.nextUpdate = input.getIntOr("NextUpdate", 0);
        this.lastSegment = segments[Mth.clamp(input.getIntOr("LastSegmentIndex", 0), 0, segments.length)];
        this.nextSegment = segments[Mth.clamp(input.getIntOr("NextSegmentIndex", 0), 0, segments.length)];
        this.currentRotation = phases[Mth.clamp(input.getIntOr("CurrentRotationIndex", 0), 0, phases.length)];
        this.lastSource = input.read("LastSource", Source.CODEC).orElse(Sources.EARTH);
        this.nextSource = input.read("NextSource", Source.CODEC).orElse(Sources.EARTH);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("PhaseTicks", this.phaseTicks);
        output.putInt("NextUpdate", this.nextUpdate);
        output.putInt("LastSegmentIndex", this.lastSegment.ordinal());
        output.putInt("NextSegmentIndex", this.nextSegment.ordinal());
        output.putInt("CurrentRotationIndex", this.currentRotation.ordinal());
        output.store("LastSource", Source.CODEC, this.lastSource);
        output.store("NextSource", Source.CODEC, this.nextSource);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpellcraftingAltarTileEntity entity) {
        if (entity.phaseTicks++ >= entity.nextUpdate && !level.isClientSide()) {
            entity.nextRotationPhase();
        }
    }
    
    protected void nextRotationPhase() {
        if (this.level != null) {
            this.currentRotation = this.currentRotation.getNext();
            if (!this.currentRotation.isPause()) {
                this.lastSegment = this.nextSegment;
                Segment[] nextPossibleSegments = Arrays.stream(Segment.values()).filter(s -> !s.equals(this.lastSegment)).toArray(Segment[]::new);
                this.nextSegment = nextPossibleSegments[this.level.getRandom().nextInt(nextPossibleSegments.length)];

                this.lastSource = this.nextSource;
                Source[] nextPossibleSources = ALLOWED_SOURCES.stream().filter(s -> !s.equals(this.lastSource)).toArray(Source[]::new);
                this.nextSource = nextPossibleSources[this.level.getRandom().nextInt(nextPossibleSources.length)];
            }
            this.nextUpdate = this.currentRotation.getDuration(this.lastSegment, this.nextSegment);
            this.phaseTicks = 0;

            this.setChanged();
            this.syncTile(true);

            if (this.level instanceof ServerLevel serverLevel && this.currentRotation.isPause()) {
                this.emitRuneParticle(serverLevel);
            }
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
            return new Color(Sources.EARTH.getColor());
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
    
    protected void emitRuneParticle(@NotNull ServerLevel serverLevel) {
        Vec3 center = Vec3.upFromBottomCenterOf(this.worldPosition, 1.1875D);
        Vec3 facingNormal = Vec3.atLowerCornerOf(this.getBlockState().getValue(SpellcraftingAltarBlock.FACING).getUnitVec3i());
        Vec3 centerOffset = facingNormal.scale(0.5D);
        Vec3 movement = facingNormal.scale(0.05D);
        long time = serverLevel.getLevelData().getGameTime();
        double bobDelta = 0.125D * Math.sin(time * (2D * Math.PI / (double)BOB_CYCLE_TIME_TICKS));

        PacketHandler.sendToAllAround(
                new SpellcraftingRunePacket(this.nextSegment, center.x + centerOffset.x, center.y + bobDelta, center.z + centerOffset.z, movement.x, 0D, movement.z, this.nextSource.getColor()),
                serverLevel,
                this.worldPosition,
                64.0D);
    }
    
    public enum Segment {
        U1(0),
        V1(45),
        T1(90),
        D1(135),
        U2(180),
        V2(225),
        T2(270),
        D2(315);
        
        private final int degreeOffset;
        
        Segment(int degrees) {
            this.degreeOffset = degrees;
        }
        
        public int getDegreeOffset() {
            return this.degreeOffset;
        }
        
        private int getDegreeTarget(Segment last, RotationPhase rotation) {
            if (rotation.isReverse() && this.degreeOffset >= last.degreeOffset) {
                return this.degreeOffset - 360;
            } else if (!rotation.isReverse() && this.degreeOffset <= last.degreeOffset) {
                return this.degreeOffset + 360;
            } else {
                return this.degreeOffset;
            }
        }
    }
    
    protected enum RotationPhase {
        CLOCKWISE(false, false),
        CLOCKWISE_PAUSE(false, true),
        COUNTER_CLOCKWISE(true, false),
        COUNTER_CLOCKWISE_PAUSE(true, true);
        
        private final boolean reverse;
        private final boolean pause;
        
        RotationPhase(boolean reverse, boolean pause) {
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
            return switch (this) {
                case CLOCKWISE, COUNTER_CLOCKWISE -> {
                    int segmentDelta = Math.abs(next.getDegreeTarget(last, this) - last.getDegreeOffset()) / 45;
                    yield TICKS_PER_SEGMENT_ROTATION * segmentDelta;
                }
                case CLOCKWISE_PAUSE, COUNTER_CLOCKWISE_PAUSE -> TICKS_PER_PAUSE;
            };
        }
        
        public RotationPhase getNext() {
            return switch (this) {
                case CLOCKWISE -> CLOCKWISE_PAUSE;
                case CLOCKWISE_PAUSE -> COUNTER_CLOCKWISE;
                case COUNTER_CLOCKWISE -> COUNTER_CLOCKWISE_PAUSE;
                case COUNTER_CLOCKWISE_PAUSE -> CLOCKWISE;
            };
        }
    }
}
