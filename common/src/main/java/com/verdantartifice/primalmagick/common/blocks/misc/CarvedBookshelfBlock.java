package com.verdantartifice.primalmagick.common.blocks.misc;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Definition of a bookshelf which can have books placed upon it.  Like a chiseled bookshelf, but
 * its block entity can be assigned a loot table at structure generation time to randomly populate
 * the shelves with books.
 * 
 * @author Daedalus4096
 */
public class CarvedBookshelfBlock extends BaseEntityBlock {
    public static final MapCodec<CarvedBookshelfBlock> CODEC = simpleCodec(CarvedBookshelfBlock::new);
    
    public static final int MAX_BOOKS = 6;
    public static final int BOOKS_PER_ROW = 3;
    public static final List<BooleanProperty> SLOT_OCCUPIED_PROPERTIES = List.of(
            BlockStateProperties.SLOT_0_OCCUPIED, BlockStateProperties.SLOT_1_OCCUPIED, BlockStateProperties.SLOT_2_OCCUPIED,
            BlockStateProperties.SLOT_3_OCCUPIED, BlockStateProperties.SLOT_4_OCCUPIED, BlockStateProperties.SLOT_5_OCCUPIED);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    
    public CarvedBookshelfBlock(Block.Properties properties) {
        super(properties);
        BlockState blockState = this.stateDefinition.any().setValue(FACING, Direction.NORTH);
        for (BooleanProperty prop : SLOT_OCCUPIED_PROPERTIES) {
            blockState = blockState.setValue(prop, Boolean.FALSE);
        }
        this.registerDefaultState(blockState);
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
    protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
        SLOT_OCCUPIED_PROPERTIES.forEach(pBuilder::add);
    }

    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    @NotNull
    protected InteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel,
                                          @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand,
                                          @NotNull BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof CarvedBookshelfTileEntity tile) {
            if (!pStack.is(ItemTags.BOOKSHELF_BOOKS)) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
                OptionalInt slotOpt = this.getHitSlot(pHitResult, pState);
                if (slotOpt.isEmpty()) {
                    return InteractionResult.PASS;
                } else if (pState.getValue(SLOT_OCCUPIED_PROPERTIES.get(slotOpt.getAsInt()))) {
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                } else {
                    addBook(pLevel, pPos, pPlayer, tile, pStack, slotOpt.getAsInt());
                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    @NotNull
    protected InteractionResult useWithoutItem(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                               @NotNull Player pPlayer, @NotNull BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof CarvedBookshelfTileEntity tile) {
            OptionalInt slotOpt = this.getHitSlot(pHitResult, pState);
            if (slotOpt.isEmpty()) {
                return InteractionResult.PASS;
            } else if (!pState.getValue(SLOT_OCCUPIED_PROPERTIES.get(slotOpt.getAsInt()))) {
                return InteractionResult.CONSUME;
            } else {
                removeBook(pLevel, pPos, pPlayer, tile, slotOpt.getAsInt());
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private OptionalInt getHitSlot(BlockHitResult pHitResult, BlockState pState) {
        return getRelativeHitCoordinatesForBlockFace(pHitResult, pState.getValue(HorizontalDirectionalBlock.FACING)).map(hitPos -> {
            int i = hitPos.y >= 0.5F ? 0 : 1;
            int j = getSection(hitPos.x);
            return OptionalInt.of(j + i * BOOKS_PER_ROW);
        }).orElseGet(OptionalInt::empty);
    }

    private static void addBook(Level pLevel, BlockPos pPos, Player pPlayer, CarvedBookshelfTileEntity tile, ItemStack bookStack, int slot) {
        if (!pLevel.isClientSide()) {
            // Set the book stack into the tile entity's slot
            pPlayer.awardStat(Stats.ITEM_USED.get(bookStack.getItem()));
            SoundEvent soundEvent = bookStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_INSERT;
            tile.addBook(slot, bookStack.split(1));
            pLevel.playSound(null, pPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private static void removeBook(Level pLevel, BlockPos pPos, Player pPlayer, CarvedBookshelfTileEntity tile, int slot) {
        if (!pLevel.isClientSide()) {
            ItemStack oldStack = tile.removeBook(slot);
            SoundEvent soundEvent = oldStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_PICKUP;
            pLevel.playSound(null, pPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!pPlayer.getInventory().add(oldStack)) {
                pPlayer.drop(oldStack, false);
            }
            pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
        }
    }

    private static Optional<Vec2> getRelativeHitCoordinatesForBlockFace(BlockHitResult hitResult, Direction face) {
        Direction hitDirection = hitResult.getDirection();
        if (face != hitDirection) {
            return Optional.empty();
        } else {
            BlockPos pos = hitResult.getBlockPos().relative(hitDirection);
            Vec3 relVec = hitResult.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
            return switch (hitDirection) {
                case NORTH -> Optional.of(new Vec2((float)(1D - relVec.x()), (float)relVec.y()));
                case SOUTH -> Optional.of(new Vec2((float)relVec.x(), (float)relVec.y()));
                case WEST -> Optional.of(new Vec2((float)relVec.z(), (float)relVec.y()));
                case EAST -> Optional.of(new Vec2((float)(1D - relVec.z()), (float)relVec.y()));
                default -> Optional.empty();
            };
        }
    }

    private static int getSection(float x) {
        if (x < 0.375F) {
            return 0;
        } else if (x < 0.6875F) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        level.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return Services.BLOCK_ENTITY_PROTOTYPES.carvedBookshelf().create(pPos, pState);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Direction pDirection) {
        if (pLevel.isClientSide()) {
            return 0;
        } else if (pLevel.getBlockEntity(pPos) instanceof CarvedBookshelfTileEntity tile) {
            return tile.getLastInteractedSlot() + 1;
        } else {
            return 0;
        }
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
