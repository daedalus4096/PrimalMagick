package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.blockstates.properties.SaltSide;
import com.verdantartifice.primalmagick.common.rituals.ISaltPowered;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Definition of a salt trail block.  Intended to work similarly to redstone wire, but transmitting
 * a different kind of power for use in rituals.
 * 
 * @author Daedalus4096
 */
public class SaltTrailBlock extends Block implements ISaltPowered {
    public static final EnumProperty<SaltSide> NORTH = EnumProperty.create("north", SaltSide.class);
    public static final EnumProperty<SaltSide> EAST = EnumProperty.create("east", SaltSide.class);
    public static final EnumProperty<SaltSide> SOUTH = EnumProperty.create("south", SaltSide.class);
    public static final EnumProperty<SaltSide> WEST = EnumProperty.create("west", SaltSide.class);
    public static final IntegerProperty POWER = IntegerProperty.create("salt_power", 0, 15);
    public static final Map<Direction, EnumProperty<SaltSide>> FACING_PROPERTY_MAP = new EnumMap<>(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));

    protected static final VoxelShape[] SHAPES = new VoxelShape[] { Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.box(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D) };

    protected boolean canProvidePower = true;
    
    protected final Set<BlockPos> blocksNeedingUpdate = new HashSet<>();

    public SaltTrailBlock() {
        super(Block.Properties.of().pushReaction(PushReaction.DESTROY).noCollision().strength(0.0F));
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, SaltSide.NONE).setValue(EAST, SaltSide.NONE).setValue(SOUTH, SaltSide.NONE).setValue(WEST, SaltSide.NONE).setValue(POWER, 0));
    }
    
    protected int getAABBIndex(BlockState state) {
        int index = 0;
        boolean nConnected = state.getValue(NORTH) != SaltSide.NONE;
        boolean eConnected = state.getValue(EAST) != SaltSide.NONE;
        boolean sConnected = state.getValue(SOUTH) != SaltSide.NONE;
        boolean wConnected = state.getValue(WEST) != SaltSide.NONE;
        
        if (nConnected || sConnected && !nConnected && !eConnected && !wConnected) {
           index |= 1 << Direction.NORTH.get2DDataValue();
        }
        if (eConnected || wConnected && !nConnected && !eConnected && !sConnected) {
           index |= 1 << Direction.EAST.get2DDataValue();
        }
        if (sConnected || nConnected && !eConnected && !sConnected && !wConnected) {
           index |= 1 << Direction.SOUTH.get2DDataValue();
        }
        if (wConnected || eConnected && !nConnected && !sConnected && !wConnected) {
           index |= 1 << Direction.WEST.get2DDataValue();
        }

        return index;
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPES[this.getAABBIndex(state)];
    }

    protected boolean canConnectTo(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return state.getBlock() instanceof ISaltPowered;
    }
    
    @NotNull
    protected SaltSide getSide(@NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull Direction face) {
        BlockPos facePos = pos.relative(face);
        BlockState faceState = world.getBlockState(facePos);
        BlockPos upPos = pos.above();
        BlockState upState = world.getBlockState(upPos);
        if (!upState.isRedstoneConductor(world, upPos)) {
            boolean isSolid = faceState.isFaceSturdy(world, facePos, Direction.UP);
            boolean canConnectFaceUp = this.canConnectTo(world.getBlockState(facePos.above()), world, facePos.above(), null);
            if (isSolid && canConnectFaceUp) {
                if (faceState.isFaceSturdy(world, facePos, face.getOpposite())) {
                    return SaltSide.UP;
                } else {
                    return SaltSide.SIDE;
                }
            }
        }
        
        boolean canConnectToFace = this.canConnectTo(faceState, world, facePos, face);
        boolean isFaceNormal = faceState.isRedstoneConductor(world, facePos);
        boolean canConnectFaceDown = this.canConnectTo(world.getBlockState(facePos.below()), world, facePos.below(), null);
        if (!canConnectToFace && (isFaceNormal || !canConnectFaceDown)) {
            return SaltSide.NONE;
        } else {
            return SaltSide.SIDE;
        }
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return this.defaultBlockState().setValue(WEST, this.getSide(world, pos, Direction.WEST)).setValue(EAST, this.getSide(world, pos, Direction.EAST)).setValue(NORTH, this.getSide(world, pos, Direction.NORTH)).setValue(SOUTH, this.getSide(world, pos, Direction.SOUTH));
    }

    @Override
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        if (direction == Direction.DOWN) {
            return state;
        } else if (direction == Direction.UP) {
            return state.setValue(WEST, this.getSide(level, pos, Direction.WEST)).setValue(EAST, this.getSide(level, pos, Direction.EAST)).setValue(NORTH, this.getSide(level, pos, Direction.NORTH)).setValue(SOUTH, this.getSide(level, pos, Direction.SOUTH));
        } else {
            return state.setValue(FACING_PROPERTY_MAP.get(direction), this.getSide(level, pos, direction));
        }
    }
    
    @Override
    public void updateIndirectNeighbourShapes(@NotNull BlockState state, @NotNull LevelAccessor world, @NotNull BlockPos pos, int flags, int recursionLeft) {
        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            SaltSide saltSide = state.getValue(FACING_PROPERTY_MAP.get(dir));
            if (saltSide != SaltSide.NONE && !world.getBlockState(mbp.set(pos).move(dir)).is(this)) {
                mbp.move(Direction.DOWN);
                BlockState downState = world.getBlockState(mbp);
                if (downState.is(this)) {
                    BlockPos downPos = mbp.relative(dir.getOpposite());
                    world.neighborShapeChanged(dir.getOpposite(), mbp, downPos, world.getBlockState(downPos), flags, recursionLeft);
                }

                mbp.set(pos).move(dir).move(Direction.UP);
                BlockState upState = world.getBlockState(mbp);
                if (upState.is(this)) {
                    BlockPos upPos = mbp.relative(dir.getOpposite());
                    world.neighborShapeChanged(dir.getOpposite(), mbp, upPos, world.getBlockState(upPos), flags, recursionLeft);
                }
            }
        }
    }
    
    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader world, @NotNull BlockPos pos) {
        BlockPos downPos = pos.below();
        BlockState downState = world.getBlockState(downPos);
        return downState.isFaceSturdy(world, downPos, Direction.UP);
    }
    
    protected int getSaltPowerFromNeighbors(Level world, BlockPos pos) {
        int maxPower = 0;
        for (Direction dir : Direction.values()) {
            int power = this.getSaltPower(world, pos.relative(dir), dir);
            if (power >= 15) {
                return 15;
            } else if (power > maxPower) {
                maxPower = power;
            }
        }
        return maxPower;
    }
    
    protected int maxSignal(int existingSignal, BlockState neighbor) {
        if (neighbor.getBlock() == this) {
            int power = neighbor.getValue(POWER);
            return Math.max(power, existingSignal);
        } else {
            return existingSignal;
        }
    }
    
    protected BlockState updateOwnSaltPower(Level world, BlockPos pos, BlockState state) {
        BlockState stateCopy = state;
        
        int curPower = state.getValue(POWER);
        this.canProvidePower = false;
        int neighborPower = this.getSaltPowerFromNeighbors(world, pos);
        this.canProvidePower = true;
        
        int signal = 0;
        if (neighborPower < 15) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos offsetPos = pos.relative(dir);
                BlockState offsetState = world.getBlockState(offsetPos);
                signal = this.maxSignal(signal, offsetState);
                
                BlockPos upPos = pos.above();
                if (offsetState.isRedstoneConductor(world, offsetPos) && !world.getBlockState(upPos).isRedstoneConductor(world, upPos)) {
                    signal = this.maxSignal(signal, world.getBlockState(offsetPos.above()));
                } else if (!offsetState.isRedstoneConductor(world, offsetPos)) {
                    signal = this.maxSignal(signal, world.getBlockState(offsetPos.below()));
                }
            }
        }
        
        int decrSignal = signal - 1;
        if (neighborPower > decrSignal) {
            decrSignal = neighborPower;
        }
        
        if (curPower != decrSignal) {
            state = state.setValue(POWER, decrSignal);
            if (world.getBlockState(pos) == stateCopy) {
                world.setBlock(pos, state, Block.UPDATE_CLIENTS);
            }
            this.blocksNeedingUpdate.add(pos);
            for (Direction dir : Direction.values()) {
                this.blocksNeedingUpdate.add(pos.relative(dir));
            }
        }
        
        return state;
    }
    
    protected BlockState updateSurroundingSaltPower(Level world, BlockPos pos, BlockState state) {
        state = this.updateOwnSaltPower(world, pos, state);
        List<BlockPos> updateList = new ArrayList<>(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        for (BlockPos updatePos : updateList) {
            world.updateNeighborsAt(updatePos, this);
        }
        return state;
    }
    
    protected void notifyTrailNeighborsOfStateChange(Level world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == this) {
            world.updateNeighborsAt(pos, this);
            for (Direction dir : Direction.values()) {
                world.updateNeighborsAt(pos.relative(dir), this);
            }
        }
    }
    
    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock() && !world.isClientSide()) {
            this.updateSurroundingSaltPower(world, pos, state);
            
            for (Direction dir : Direction.Plane.VERTICAL) {
                world.updateNeighborsAt(pos.relative(dir), this);
            }
            
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                this.notifyTrailNeighborsOfStateChange(world, pos.relative(dir));
            }
            
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos offsetPos = pos.relative(dir);
                if (world.getBlockState(offsetPos).isRedstoneConductor(world, offsetPos)) {
                    this.notifyTrailNeighborsOfStateChange(world, offsetPos.above());
                } else {
                    this.notifyTrailNeighborsOfStateChange(world, offsetPos.below());
                }
            }
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
        for (Direction dir : Direction.values()) {
            level.updateNeighborsAt(pos.relative(dir), this);
        }

        this.updateSurroundingSaltPower(level, pos, state);

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            this.notifyTrailNeighborsOfStateChange(level, pos.relative(dir));
        }

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos offsetPos = pos.relative(dir);
            if (level.getBlockState(offsetPos).isRedstoneConductor(level, offsetPos)) {
                this.notifyTrailNeighborsOfStateChange(level, offsetPos.above());
            } else {
                this.notifyTrailNeighborsOfStateChange(level, offsetPos.below());
            }
        }
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Block blockIn, @Nullable Orientation orientation, boolean isMoving) {
        if (!world.isClientSide()) {
            if (state.canSurvive(world, pos)) {
                this.updateSurroundingSaltPower(world, pos, state);
            } else {
                dropResources(state, world, pos);
                world.removeBlock(pos, false);
            }
        }
    }

    @Override
    public int getStrongSaltPower(@NotNull BlockState blockState, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return this.canProvidePower ? blockState.getValue(POWER) : 0;
    }
    
    public static int colorMultiplier(int power) {
        float powerRatio = (float)power / 15.0F;
        float colorRatio = (power == 0) ? 0.6F : (powerRatio * 0.3F) + 0.7F;
        int color = Mth.clamp((int)(colorRatio * 255.0F), 0, 255);
        return -16777216 | color << 16 | color << 8 | color;
    }
    
    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        int power = state.getValue(POWER);
        if (power > 0) {
            double x = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double y = (float)pos.getY() + 0.0625F;
            double z = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float powerRatio = (float)power / 15.0F;
            float colorRatio = powerRatio * 0.3F + 0.7F;
            world.addParticle(new DustParticleOptions(ARGB.color(new Vec3(colorRatio, colorRatio, colorRatio)), 1.0F), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    @NotNull
    public BlockState rotate(@NotNull BlockState state, @NotNull Rotation rot) {
        return switch (rot) {
            case CLOCKWISE_180 ->
                    state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default -> state;
        };
    }
    
    @Override
    @NotNull
    public BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirrorIn) {
        return switch (mirrorIn) {
            case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> state;
        };
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, POWER);
    }
}
