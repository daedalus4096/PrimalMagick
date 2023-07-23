package com.verdantartifice.primalmagick.common.blocks.rituals;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.joml.Vector3f;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.blockstates.properties.SaltSide;
import com.verdantartifice.primalmagick.common.rituals.ISaltPowered;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        super(Block.Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.0F));
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, SaltSide.NONE).setValue(EAST, SaltSide.NONE).setValue(SOUTH, SaltSide.NONE).setValue(WEST, SaltSide.NONE).setValue(POWER, Integer.valueOf(0)));
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
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES[this.getAABBIndex(state)];
    }

    protected boolean canConnectTo(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return state.getBlock() instanceof ISaltPowered;
    }
    
    @Nonnull
    protected SaltSide getSide(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull Direction face) {
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
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            return stateIn;
        } else if (facing == Direction.UP) {
            return stateIn.setValue(WEST, this.getSide(worldIn, currentPos, Direction.WEST)).setValue(EAST, this.getSide(worldIn, currentPos, Direction.EAST)).setValue(NORTH, this.getSide(worldIn, currentPos, Direction.NORTH)).setValue(SOUTH, this.getSide(worldIn, currentPos, Direction.SOUTH));
        } else {
            return stateIn.setValue(FACING_PROPERTY_MAP.get(facing), this.getSide(worldIn, currentPos, facing));
        }
    }
    
    @Override
    public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int flags, int recursionLeft) {
        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            SaltSide saltSide = state.getValue(FACING_PROPERTY_MAP.get(dir));
            if (saltSide != SaltSide.NONE && world.getBlockState(mbp.set(pos).move(dir)).getBlock() != this) {
                mbp.move(Direction.DOWN);
                BlockState downState = world.getBlockState(mbp);
                BlockPos oppDownPos = mbp.relative(dir.getOpposite());
                BlockState newDownState = downState.updateShape(dir.getOpposite(), world.getBlockState(oppDownPos), world, mbp, oppDownPos);
                updateOrDestroy(downState, newDownState, world, mbp, flags, recursionLeft);
                
                mbp.set(pos).move(dir).move(Direction.UP);
                BlockState upState = world.getBlockState(mbp);
                BlockPos oppUpPos = mbp.relative(dir.getOpposite());
                BlockState newUpState = upState.updateShape(dir.getOpposite(), world.getBlockState(oppUpPos), world, mbp, oppUpPos);
                updateOrDestroy(upState, newUpState, world, mbp, flags, recursionLeft);
            }
        }
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
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
            state = state.setValue(POWER, Integer.valueOf(decrSignal));
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
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock() && !world.isClientSide) {
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
    
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && state.getBlock() != newState.getBlock()) {
            super.onRemove(state, world, pos, newState, isMoving);
            if (!world.isClientSide) {
                for (Direction dir : Direction.values()) {
                    world.updateNeighborsAt(pos.relative(dir), this);
                }
                
                this.updateSurroundingSaltPower(world, pos, state);
                
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
    }
    
    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!world.isClientSide) {
            if (state.canSurvive(world, pos)) {
                this.updateSurroundingSaltPower(world, pos, state);
            } else {
                dropResources(state, world, pos);
                world.removeBlock(pos, false);
            }
        }
    }

    @Override
    public int getStrongSaltPower(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return this.canProvidePower ? blockState.getValue(POWER) : 0;
    }
    
    protected boolean isPowerSourceAt(BlockGetter world, BlockPos pos, Direction side) {
        BlockPos offsetPos = pos.relative(side);
        BlockState offsetState = world.getBlockState(offsetPos);
        boolean isOffsetNormal = offsetState.isRedstoneConductor(world, offsetPos);
        BlockPos upPos = pos.above();
        boolean isUpNormal = world.getBlockState(upPos).isRedstoneConductor(world, upPos);
        
        if (!isUpNormal && isOffsetNormal && this.canConnectTo(world.getBlockState(offsetPos.above()), world, offsetPos.above(), null)) {
            return true;
        } else if (this.canConnectTo(offsetState, world, offsetPos, side)) {
            return true;
        } else {
            return !isOffsetNormal && this.canConnectTo(world.getBlockState(offsetPos.below()), world, offsetPos.below(), null);
        }
    }
    
    public static int colorMultiplier(int power) {
        float powerRatio = (float)power / 15.0F;
        float colorRatio = (power == 0) ? 0.6F : (powerRatio * 0.3F) + 0.7F;
        int color = Mth.clamp((int)(colorRatio * 255.0F), 0, 255);
        return -16777216 | color << 16 | color << 8 | color;
    }
    
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
        int power = state.getValue(POWER);
        if (power > 0) {
            double x = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double y = (double)((float)pos.getY() + 0.0625F);
            double z = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float powerRatio = (float)power / 15.0F;
            float colorRatio = (power == 0) ? 0.6F : (powerRatio * 0.3F) + 0.7F;
            world.addParticle(new DustParticleOptions(new Vector3f(colorRatio, colorRatio, colorRatio), 1.0F), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        switch(rot) {
        case CLOCKWISE_180:
            return state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
        case COUNTERCLOCKWISE_90:
            return state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
        case CLOCKWISE_90:
            return state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
        default:
            return state;
        }
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        switch(mirrorIn) {
        case LEFT_RIGHT:
            return state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
        case FRONT_BACK:
            return state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
        default:
            return state;
        }
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, POWER);
    }
}
