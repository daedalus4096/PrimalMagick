package com.verdantartifice.primalmagick.common.blocks.minerals;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Definition of a gem cluster grown from a {@link BuddingGemSourceBlock}.
 * 
 * @author Daedalus4096
 */
public class BuddingGemClusterBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    protected final GemBudType gemType;
    protected final Optional<Supplier<BuddingGemClusterBlock>> nextGemSupplierOpt;
    protected final ImmutableMap<Direction, VoxelShape> shapes;

    public BuddingGemClusterBlock(int pSize, int pOffset, GemBudType gemType, Optional<Supplier<BuddingGemClusterBlock>> nextGemSupplierOpt, Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.FALSE).setValue(FACING, Direction.UP));
        this.gemType = gemType;
        this.nextGemSupplierOpt = nextGemSupplierOpt;
        this.shapes = ImmutableMap.<Direction, VoxelShape>builder()
                .put(Direction.UP, Block.box(pOffset, 0.0D, pOffset, (16 - pOffset), pSize, (16 - pOffset)))
                .put(Direction.DOWN, Block.box(pOffset, (16 - pSize), pOffset, (16 - pOffset), 16.0D, (16 - pOffset)))
                .put(Direction.NORTH, Block.box(pOffset, pOffset, (16 - pSize), (16 - pOffset), (16 - pOffset), 16.0D))
                .put(Direction.SOUTH, Block.box(pOffset, pOffset, 0.0D, (16 - pOffset), (16 - pOffset), pSize))
                .put(Direction.EAST, Block.box(0.0D, pOffset, pOffset, pSize, (16 - pOffset), (16 - pOffset)))
                .put(Direction.WEST, Block.box((16 - pSize), pOffset, pOffset, 16.0D, (16 - pOffset), (16 - pOffset)))
                .build();
    }
    
    public GemBudType getGemBudType() {
        return this.gemType;
    }
    
    public Optional<Supplier<BuddingGemClusterBlock>> getNextGemBlock() {
        return this.nextGemSupplierOpt;
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        VoxelShape retVal = this.shapes.get(pState.getValue(FACING));
        return retVal == null ? Shapes.block() : retVal;
    }

    @Override
    @NotNull
    public List<ItemStack> getDrops(@NotNull BlockState pState, @NotNull Builder pParams) {
        if (pParams.getOptionalParameter(LootContextParams.THIS_ENTITY) == null) {
            // Prevent dropping anything if there's no player
            return List.of();
        }
        return super.getDrops(pState, pParams);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction direction = pState.getValue(FACING);
        BlockPos blockPos = pPos.relative(direction.getOpposite());
        return pLevel.getBlockState(blockPos).isFaceSturdy(pLevel, blockPos, direction);
    }

    @Override
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        BlockState retVal = super.getStateForPlacement(pContext);
        if (retVal != null) {
            FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
            retVal = retVal.setValue(WATERLOGGED, fluidState.is(Fluids.WATER)).setValue(FACING, pContext.getClickedFace());
        }
        return retVal;
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
    @NotNull
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING, WATERLOGGED);
    }
}
