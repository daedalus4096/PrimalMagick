package com.verdantartifice.primalmagick.common.blocks.devices;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.SanguineCoreItem;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * Block definition for a sanguine crucible.  Uses blood cores and captured souls to summon creatures.
 * 
 * @author Daedalus4096
 */
public class SanguineCrucibleBlock extends BaseEntityBlock {
    public static final MapCodec<SanguineCrucibleBlock> CODEC = simpleCodec(SanguineCrucibleBlock::new);
    
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);

    public SanguineCrucibleBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.FALSE));
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    @NotNull
    public VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        return INSIDE;
    }

    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, LIT);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Make the block face the player when placed
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    @Override
    @NotNull
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    @NotNull
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return Services.BLOCK_ENTITY_PROTOTYPES.sanguineCrucible().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityTypesPM.SANGUINE_CRUCIBLE.get(), Services.BLOCK_ENTITY_TICKERS.sanguineCrucible());
    }

    @Override
    public void entityInside(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                             @NotNull Entity pEntity, @NotNull InsideBlockEffectApplier applier, boolean intersects) {
        if (!pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos) instanceof SanguineCrucibleTileEntity crucibleTile) {
                if (pEntity instanceof ItemEntity itemEntity) {
                    if (itemEntity.getItem().getItem() == ItemsPM.SOUL_GEM.get()) {
                        crucibleTile.addSouls(itemEntity.getItem().getCount());
                        pEntity.discard();
                        pLevel.playSound(null, pPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @Override
    @NotNull
    protected InteractionResult useWithoutItem(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                               @NotNull Player pPlayer, @NotNull BlockHitResult pHitResult) {
        if (!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof SanguineCrucibleTileEntity crucibleTile) {
            if (pPlayer.isSecondaryUseActive() && crucibleTile.hasCore()) {
                popResource(pLevel, pPos.relative(pHitResult.getDirection()), crucibleTile.removeItem(1));
                pLevel.playSound(null, pPos, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.5F);
                pLevel.setBlock(pPos, pState.setValue(LIT, false), Block.UPDATE_ALL_IMMEDIATE);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @NotNull
    protected InteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel,
                                          @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand,
                                          @NotNull BlockHitResult pHitResult) {
        if (!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof SanguineCrucibleTileEntity crucibleTile) {
            if (pStack.getItem() instanceof SanguineCoreItem && !crucibleTile.hasCore()) {
                crucibleTile.setItem(pStack.copyWithCount(1));
                pStack.shrink(1);
                pLevel.playSound(null, pPos, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
                pLevel.setBlock(pPos, pState.setValue(LIT, true), Block.UPDATE_ALL_IMMEDIATE);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        level.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (rand.nextDouble() < 0.1D) {
            worldIn.playSound(null, pos, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.1F + (rand.nextFloat() * 0.1F), 0.8F + (rand.nextFloat() * 0.4F));
        }
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
