package com.verdantartifice.primalmagick.common.blocks.trees;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * Base definition for leaf blocks that phase in and out over time.  Not derived from LeavesBlock because
 * the RenderTypeLookup code has special hardcoded checks that force subclasses of LeavesBlock to use
 * either the solid or cutout-mipped render layers, rather than transparent.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPhasingLeavesBlock extends Block implements IPhasingBlock {
    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    protected static final Logger LOGGER = LogUtils.getLogger();

    public AbstractPhasingLeavesBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PHASE, TimePhase.FULL).setValue(DISTANCE, 7).setValue(PERSISTENT, Boolean.FALSE));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, PHASE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Set the block's phase upon placement
        TimePhase phase = this.getCurrentPhase(context.getLevel(), context.getClickedPos());
        return updateDistance(this.defaultBlockState().setValue(PHASE, phase).setValue(PERSISTENT, Boolean.TRUE), context.getLevel(), context.getClickedPos());
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(DISTANCE) == 7 && !state.getValue(PERSISTENT);
    }
    
    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource random) {
        // Remove the leaves if too far from a log
        if (!state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7) {
            dropResources(state, worldIn, pos);
            worldIn.removeBlock(pos, false);
        }

        // Periodically check to see if the block's phase needs to be updated
        TimePhase newPhase = this.getCurrentPhase(worldIn, pos);
        if (newPhase != state.getValue(PHASE)) {
            worldIn.setBlock(pos, state.setValue(PHASE, newPhase), Block.UPDATE_ALL);
        }
    }
    
    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        worldIn.setBlock(pos, updateDistance(state, worldIn, pos), Block.UPDATE_ALL);
    }
    
    @Override
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        // Determine if the blockstate needs to be updated with a new distance
        int i = getDistance(neighborState) + 1;
        if (i != 1 || state.getValue(DISTANCE) != i) {
            scheduledTickAccess.scheduleTick(pos, this, 1);
        }

        // Immediately check to see if the block's phase needs to be updated when one of its neighbors changes
        BlockState newState = super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
        newState = this.updateBlockPhase(newState, level, pos);
        return newState;
    }
    
    private static BlockState updateDistance(BlockState state, LevelAccessor world, BlockPos pos) {
        int dist = 7;
        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            mbp.set(pos).move(dir);
            dist = Math.min(dist, getDistance(world.getBlockState(mbp)) + 1);
            if (dist == 1) {
                break;
            }
        }
        return state.setValue(DISTANCE, dist);
    }
    
    private static int getDistance(BlockState neighbor) {
        if (neighbor.is(BlockTags.LOGS)) {
            return 0;
        } else if (neighbor.getBlock() instanceof AbstractPhasingLeavesBlock || neighbor.getBlock() instanceof LeavesBlock) {
            return neighbor.getValue(DISTANCE);
        } else {
            return 7;
        }
    }
    
    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (worldIn.isRainingAt(pos.above())) {
            if (rand.nextInt(15) == 1) {
                BlockPos blockpos = pos.below();
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (!blockstate.canOcclude() || !blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP)) {
                    double x = (double)pos.getX() + rand.nextDouble();
                    double y = (double)pos.getY() - 0.05D;
                    double z = (double)pos.getZ() + rand.nextDouble();
                    worldIn.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    protected static Boolean allowsSpawnOnLeaves(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
       return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }
}
