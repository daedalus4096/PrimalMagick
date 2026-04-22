package com.verdantartifice.primalmagick.common.blocks.misc;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

/**
 * Block definitions for an Enderward, which blocks incoming teleportation attempts within
 * sixteen blocks of itself.  This includes Endermen, Ender Pearls, Chorus Fruit, and the
 * Teleport spell.
 * 
 * @author Daedalus4096
 */
public class EnderwardBlock extends Block {
    public static final int EFFECT_RADIUS = 16;
    protected static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape BASE_AABB = Block.box(0, 0, 14, 16, 16, 16);
    private static final Map<Direction, VoxelShape> AABBS = new EnumMap<>(ImmutableMap.<Direction, VoxelShape>builder()
            .put(Direction.NORTH, BASE_AABB)
            .put(Direction.SOUTH, VoxelShapeUtils.rotate(BASE_AABB, Direction.Axis.Y, Rotation.CLOCKWISE_180))
            .put(Direction.WEST, VoxelShapeUtils.rotate(BASE_AABB, Direction.Axis.Y, Rotation.CLOCKWISE_90))
            .put(Direction.EAST, VoxelShapeUtils.rotate(BASE_AABB, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90))
            .build());

    public EnderwardBlock() {
        super(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instabreak().pushReaction(PushReaction.DESTROY).sound(SoundType.WOOD).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return AABBS.getOrDefault(pState.getValue(FACING), BASE_AABB);
    }

    @Override
    protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    @Override
    @NotNull
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    @NotNull
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction direction = pState.getValue(FACING);
        BlockPos blockPos = pPos.relative(direction.getOpposite());
        BlockState blockState = pLevel.getBlockState(blockPos);
        return blockState.isFaceSturdy(pLevel, blockPos, direction);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockState = this.defaultBlockState();
        LevelReader levelReader = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        Direction[] directions = pContext.getNearestLookingDirections();

        for (Direction dir : directions) {
            if (dir.getAxis().isHorizontal()) {
                Direction oppositeDir = dir.getOpposite();
                blockState = blockState.setValue(FACING, oppositeDir);
                if (blockState.canSurvive(levelReader, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    @Override
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public void animateTick(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        Direction stepDir = pState.getValue(FACING).getOpposite();
        for (int i = 0; i < 3; i++) {
            int j = pRandom.nextInt(2) * 2 - 1;
            int k = pRandom.nextInt(2) * 2 - 1;
            int l = pRandom.nextInt(2) * 2 - 1;
            double d0 = (double)pPos.getX() + 0.5D + (stepDir.getStepX() * 0.4375D) + 0.125D * (double)j;
            double d1 = (double)pPos.getY() + 0.5D + 0.125D * (double)k;
            double d2 = (double)pPos.getZ() + 0.5D + (stepDir.getStepZ() * 0.4375D) + 0.125D * (double)l;
            double d3 = (double)(pRandom.nextFloat() * (float)j);
            double d4 = (double)(pRandom.nextFloat() * (float)k);
            double d5 = (double)(pRandom.nextFloat() * (float)l);
            pLevel.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }
}
