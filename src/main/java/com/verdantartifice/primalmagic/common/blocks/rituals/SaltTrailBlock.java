package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blockstates.properties.SaltSide;
import com.verdantartifice.primalmagic.common.rituals.ISaltPowered;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

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

    protected static final VoxelShape[] SHAPES = new VoxelShape[] { Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D) };

    protected boolean canProvidePower = true;
    
    protected final Set<BlockPos> blocksNeedingUpdate = new HashSet<>();

    public SaltTrailBlock() {
        super(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0F));
        this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, SaltSide.NONE).with(EAST, SaltSide.NONE).with(SOUTH, SaltSide.NONE).with(WEST, SaltSide.NONE).with(POWER, Integer.valueOf(0)));
    }
    
    protected int getAABBIndex(BlockState state) {
        int index = 0;
        boolean nConnected = state.get(NORTH) != SaltSide.NONE;
        boolean eConnected = state.get(EAST) != SaltSide.NONE;
        boolean sConnected = state.get(SOUTH) != SaltSide.NONE;
        boolean wConnected = state.get(WEST) != SaltSide.NONE;
        
        if (nConnected || sConnected && !nConnected && !eConnected && !wConnected) {
           index |= 1 << Direction.NORTH.getHorizontalIndex();
        }
        if (eConnected || wConnected && !nConnected && !eConnected && !sConnected) {
           index |= 1 << Direction.EAST.getHorizontalIndex();
        }
        if (sConnected || nConnected && !eConnected && !sConnected && !wConnected) {
           index |= 1 << Direction.SOUTH.getHorizontalIndex();
        }
        if (wConnected || eConnected && !nConnected && !sConnected && !wConnected) {
           index |= 1 << Direction.WEST.getHorizontalIndex();
        }

        return index;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[this.getAABBIndex(state)];
    }

    protected boolean canConnectTo(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        Block block = state.getBlock();
        if (block == BlocksPM.SALT_TRAIL.get()) {
            return true;
        } else if (block instanceof ISaltPowered) {
            return ((ISaltPowered)block).canConnectSalt(state, world, pos, side) && side != null;
        } else {
            return false;
        }
    }
    
    @Nonnull
    protected SaltSide getSide(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull Direction face) {
        BlockPos facePos = pos.offset(face);
        BlockState faceState = world.getBlockState(facePos);
        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);
        if (!upState.isNormalCube(world, upPos)) {
            boolean isSolid = faceState.isSolidSide(world, facePos, Direction.UP);
            boolean canConnectFaceUp = this.canConnectTo(world.getBlockState(facePos.up()), world, facePos.up(), null);
            if (isSolid && canConnectFaceUp) {
                if (faceState.isCollisionShapeOpaque(world, facePos)) {
                    return SaltSide.UP;
                } else {
                    return SaltSide.SIDE;
                }
            }
        }
        
        boolean canConnectToFace = this.canConnectTo(faceState, world, facePos, face);
        boolean isFaceNormal = faceState.isNormalCube(world, facePos);
        boolean canConnectFaceDown = this.canConnectTo(world.getBlockState(facePos.down()), world, facePos.down(), null);
        if (!canConnectToFace && (isFaceNormal || !canConnectFaceDown)) {
            return SaltSide.NONE;
        } else {
            return SaltSide.SIDE;
        }
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        return this.getDefaultState().with(WEST, this.getSide(world, pos, Direction.WEST)).with(EAST, this.getSide(world, pos, Direction.EAST)).with(NORTH, this.getSide(world, pos, Direction.NORTH)).with(SOUTH, this.getSide(world, pos, Direction.SOUTH));
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            return stateIn;
        } else if (facing == Direction.UP) {
            return stateIn.with(WEST, this.getSide(worldIn, currentPos, Direction.WEST)).with(EAST, this.getSide(worldIn, currentPos, Direction.EAST)).with(NORTH, this.getSide(worldIn, currentPos, Direction.NORTH)).with(SOUTH, this.getSide(worldIn, currentPos, Direction.SOUTH));
        } else {
            return stateIn.with(FACING_PROPERTY_MAP.get(facing), this.getSide(worldIn, currentPos, facing));
        }
    }
    
    @Override
    public void updateDiagonalNeighbors(BlockState state, IWorld world, BlockPos pos, int flags) {
        try (BlockPos.PooledMutable pmbp = BlockPos.PooledMutable.retain()) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                SaltSide saltSide = state.get(FACING_PROPERTY_MAP.get(dir));
                if (saltSide != SaltSide.NONE && world.getBlockState(pmbp.setPos(pos).move(dir)).getBlock() != this) {
                    pmbp.move(Direction.DOWN);
                    BlockState downState = world.getBlockState(pmbp);
                    BlockPos oppDownPos = pmbp.offset(dir.getOpposite());
                    BlockState newDownState = downState.updatePostPlacement(dir.getOpposite(), world.getBlockState(oppDownPos), world, pmbp, oppDownPos);
                    replaceBlock(downState, newDownState, world, pmbp, flags);
                    
                    pmbp.setPos(pos).move(dir).move(Direction.UP);
                    BlockState upState = world.getBlockState(pmbp);
                    BlockPos oppUpPos = pmbp.offset(dir.getOpposite());
                    BlockState newUpState = upState.updatePostPlacement(dir.getOpposite(), world.getBlockState(oppUpPos), world, pmbp, oppUpPos);
                    replaceBlock(upState, newUpState, world, pmbp, flags);
                }
            }
        }
    }
    
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos downPos = pos.down();
        BlockState downState = world.getBlockState(downPos);
        return downState.isSolidSide(world, downPos, Direction.UP);
    }
    
    protected int getSaltPowerFromNeighbors(World world, BlockPos pos) {
        int maxPower = 0;
        for (Direction dir : Direction.values()) {
            int power = this.getSaltPower(world, pos.offset(dir), dir);
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
            int power = neighbor.get(POWER);
            return Math.max(power, existingSignal);
        } else {
            return existingSignal;
        }
    }
    
    protected BlockState updateOwnSaltPower(World world, BlockPos pos, BlockState state) {
        BlockState stateCopy = state;
        
        int curPower = state.get(POWER);
        this.canProvidePower = false;
        int neighborPower = this.getSaltPowerFromNeighbors(world, pos);
        this.canProvidePower = true;
        
        int signal = 0;
        if (neighborPower < 15) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos offsetPos = pos.offset(dir);
                BlockState offsetState = world.getBlockState(offsetPos);
                signal = this.maxSignal(signal, offsetState);
                
                BlockPos upPos = pos.up();
                if (offsetState.isNormalCube(world, offsetPos) && !world.getBlockState(upPos).isNormalCube(world, upPos)) {
                    signal = this.maxSignal(signal, world.getBlockState(offsetPos.up()));
                } else if (!offsetState.isNormalCube(world, offsetPos)) {
                    signal = this.maxSignal(signal, world.getBlockState(offsetPos.down()));
                }
            }
        }
        
        int decrSignal = signal - 1;
        if (neighborPower > decrSignal) {
            decrSignal = neighborPower;
        }
        
        if (curPower != decrSignal) {
            state = state.with(POWER, Integer.valueOf(decrSignal));
            if (world.getBlockState(pos) == stateCopy) {
                world.setBlockState(pos, state, Constants.BlockFlags.BLOCK_UPDATE);
            }
            this.blocksNeedingUpdate.add(pos);
            for (Direction dir : Direction.values()) {
                this.blocksNeedingUpdate.add(pos.offset(dir));
            }
        }
        
        return state;
    }
    
    protected BlockState updateSurroundingSaltPower(World world, BlockPos pos, BlockState state) {
        state = this.updateOwnSaltPower(world, pos, state);
        List<BlockPos> updateList = new ArrayList<>(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        for (BlockPos updatePos : updateList) {
            world.notifyNeighborsOfStateChange(updatePos, this);
        }
        return state;
    }
    
    protected void notifyTrailNeighborsOfStateChange(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == this) {
            world.notifyNeighborsOfStateChange(pos, this);
            for (Direction dir : Direction.values()) {
                world.notifyNeighborsOfStateChange(pos.offset(dir), this);
            }
        }
    }
    
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock() && !world.isRemote) {
            this.updateSurroundingSaltPower(world, pos, state);
            
            for (Direction dir : Direction.Plane.VERTICAL) {
                world.notifyNeighborsOfStateChange(pos.offset(dir), this);
            }
            
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                this.notifyTrailNeighborsOfStateChange(world, pos.offset(dir));
            }
            
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos offsetPos = pos.offset(dir);
                if (world.getBlockState(offsetPos).isNormalCube(world, offsetPos)) {
                    this.notifyTrailNeighborsOfStateChange(world, offsetPos.up());
                } else {
                    this.notifyTrailNeighborsOfStateChange(world, offsetPos.down());
                }
            }
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && state.getBlock() != newState.getBlock()) {
            super.onReplaced(state, world, pos, newState, isMoving);
            if (!world.isRemote) {
                for (Direction dir : Direction.values()) {
                    world.notifyNeighborsOfStateChange(pos.offset(dir), this);
                }
                
                this.updateSurroundingSaltPower(world, pos, state);
                
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    this.notifyTrailNeighborsOfStateChange(world, pos.offset(dir));
                }
                
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos offsetPos = pos.offset(dir);
                    if (world.getBlockState(offsetPos).isNormalCube(world, offsetPos)) {
                        this.notifyTrailNeighborsOfStateChange(world, offsetPos.up());
                    } else {
                        this.notifyTrailNeighborsOfStateChange(world, offsetPos.down());
                    }
                }
            }
        }
    }
    
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!world.isRemote) {
            if (state.isValidPosition(world, pos)) {
                this.updateSurroundingSaltPower(world, pos, state);
            } else {
                spawnDrops(state, world, pos);
                world.removeBlock(pos, false);
            }
        }
    }

    @Override
    public int getStrongSaltPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return !this.canProvidePower ? 0 : this.getWeakSaltPower(blockState, blockAccess, pos, side);
    }
    
    protected boolean isPowerSourceAt(IBlockReader world, BlockPos pos, Direction side) {
        BlockPos offsetPos = pos.offset(side);
        BlockState offsetState = world.getBlockState(offsetPos);
        boolean isOffsetNormal = offsetState.isNormalCube(world, offsetPos);
        BlockPos upPos = pos.up();
        boolean isUpNormal = world.getBlockState(upPos).isNormalCube(world, upPos);
        
        if (!isUpNormal && isOffsetNormal && this.canConnectTo(world.getBlockState(offsetPos.up()), world, offsetPos.up(), null)) {
            return true;
        } else if (this.canConnectTo(offsetState, world, offsetPos, side)) {
            return true;
        } else {
            return !isOffsetNormal && this.canConnectTo(world.getBlockState(offsetPos.down()), world, offsetPos.down(), null);
        }
    }
    
    @Override
    public int getWeakSaltPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        if (!this.canProvidePower) {
            return 0;
        } else {
            int power = blockState.get(POWER);
            if (power == 0) {
                return 0;
            } else if (side == Direction.UP) {
                return power;
            } else {
                EnumSet<Direction> dirSet = EnumSet.noneOf(Direction.class);
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    if (this.isPowerSourceAt(blockAccess, pos, dir)) {
                        dirSet.add(dir);
                    }
                }
                if (side.getAxis().isHorizontal() && dirSet.isEmpty()) {
                    return power;
                } else if (dirSet.contains(side) && !dirSet.contains(side.rotateYCCW()) && !dirSet.contains(side.rotateY())) {
                    return power;
                } else {
                    return 0;
                }
            }
        }
    }
    
    @Override
    public boolean canProvideSaltPower(BlockState state) {
        return this.canProvidePower;
    }
    
    public static int colorMultiplier(int power) {
        float powerRatio = (float)power / 15.0F;
        float colorRatio = (power == 0) ? 0.6F : (powerRatio * 0.3F) + 0.7F;
        int color = MathHelper.clamp((int)(colorRatio * 255.0F), 0, 255);
        return -16777216 | color << 16 | color << 8 | color;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        int power = state.get(POWER);
        if (power > 0) {
            double x = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double y = (double)((float)pos.getY() + 0.0625F);
            double z = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float powerRatio = (float)power / 15.0F;
            float colorRatio = (power == 0) ? 0.6F : (powerRatio * 0.3F) + 0.7F;
            world.addParticle(new RedstoneParticleData(colorRatio, colorRatio, colorRatio, 1.0F), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        switch(rot) {
        case CLOCKWISE_180:
            return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
        case COUNTERCLOCKWISE_90:
            return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
        case CLOCKWISE_90:
            return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
        default:
            return state;
        }
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        switch(mirrorIn) {
        case LEFT_RIGHT:
            return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
        case FRONT_BACK:
            return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
        default:
            return state;
        }
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, POWER);
    }
}
