package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Random;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.Constants;

/**
 * Base definition for leaf blocks that phase in and out over time.  Not derived from LeavesBlock because
 * the RenderTypeLookup code has special hardcoded checks that force subclasses of LeavesBlock to use
 * either the solid or cutout-mipped render layers, rather than transparent.
 * 
 * @author Daedalus4096
 */
@SuppressWarnings("deprecation")
public abstract class AbstractPhasingLeavesBlock extends Block implements IShearable {
    public static final EnumProperty<TimePhase> PHASE = EnumProperty.create("phase", TimePhase.class);
    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE_1_7;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public AbstractPhasingLeavesBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(PHASE, TimePhase.FULL).with(DISTANCE, Integer.valueOf(7)).with(PERSISTENT, Boolean.valueOf(false)));
    }
    
    /**
     * Get the current phase of the block based on the current game time.
     * 
     * @param world the game world
     * @return the block's current phase
     */
    public abstract TimePhase getCurrentPhase(IWorld world);
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, PHASE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Set the block's phase upon placement
        TimePhase phase = this.getCurrentPhase(context.getWorld());
        return updateDistance(this.getDefaultState().with(PHASE, phase).with(PERSISTENT, Boolean.valueOf(true)), context.getWorld(), context.getPos());
    }
    
    @Override
    public boolean ticksRandomly(BlockState state) {
        return state.get(DISTANCE) == 7 && !state.get(PERSISTENT);
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        // Remove the leaves if too far from a log
        if (!state.get(PERSISTENT) && state.get(DISTANCE) == 7) {
            spawnDrops(state, worldIn, pos);
            worldIn.removeBlock(pos, false);
        }

        // Periodically check to see if the block's phase needs to be updated
        TimePhase newPhase = this.getCurrentPhase(worldIn);
        if (newPhase != state.get(PHASE)) {
            worldIn.setBlockState(pos, state.with(PHASE, newPhase), Constants.BlockFlags.DEFAULT);
        }
    }
    
    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        worldIn.setBlockState(pos, updateDistance(state, worldIn, pos), Constants.BlockFlags.DEFAULT);
    }
    
    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        // Determine if the blockstate needs to be updated with a new distance
        int i = getDistance(facingState) + 1;
        if (i != 1 || stateIn.get(DISTANCE) != i) {
            worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
        }

        // Immediately check to see if the block's phase needs to be updated when one of its neighbors changes
        TimePhase newPhase = this.getCurrentPhase(worldIn);
        if (newPhase != stateIn.get(PHASE)) {
            stateIn = stateIn.with(PHASE, newPhase);
        }
        return stateIn;
    }
    
    private static BlockState updateDistance(BlockState state, IWorld world, BlockPos pos) {
        int dist = 7;
        try (BlockPos.PooledMutable pmbp = BlockPos.PooledMutable.retain()) {
            for (Direction dir : Direction.values()) {
                pmbp.setPos(pos).move(dir);
                dist = Math.min(dist, getDistance(world.getBlockState(pmbp)) + 1);
                if (dist == 1) {
                    break;
                }
            }
        }
        return state.with(DISTANCE, Integer.valueOf(dist));
    }
    
    private static int getDistance(BlockState neighbor) {
        if (BlockTags.LOGS.contains(neighbor.getBlock())) {
            return 0;
        } else if (neighbor.getBlock() instanceof AbstractPhasingLeavesBlock || neighbor.getBlock() instanceof LeavesBlock) {
            return neighbor.get(DISTANCE);
        } else {
            return 7;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.isRainingAt(pos.up())) {
            if (rand.nextInt(15) == 1) {
                BlockPos blockpos = pos.down();
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (!blockstate.isSolid() || !blockstate.isSolidSide(worldIn, blockpos, Direction.UP)) {
                    double x = (double)pos.getX() + rand.nextDouble();
                    double y = (double)pos.getY() - 0.05D;
                    double z = (double)pos.getZ() + rand.nextDouble();
                    worldIn.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
    
    @Override
    public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }
    
    @Override
    public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
        if (blockState.get(PHASE) == TimePhase.FULL) {
            // If the block is fully phased in, use its default hardness as those aren't all the same
            return this.blockHardness;
        } else {
            return blockState.get(PHASE).getHardness();
        }
    }
    
    @Override
    public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, Entity exploder, Explosion explosion) {
        if (state.get(PHASE) == TimePhase.FULL) {
            // If the block is fully phased in, use its default resistance as those aren't all the same
            return this.blockResistance;
        } else {
            return state.get(PHASE).getResistance();
        }
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(PHASE).getLightLevel();
    }
}
