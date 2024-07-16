package com.verdantartifice.primalmagick.common.blocks.misc;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a bookshelf which can have books placed upon it.  Like a chiseled book shelf, but
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
            BlockStateProperties.CHISELED_BOOKSHELF_SLOT_0_OCCUPIED, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_1_OCCUPIED, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_2_OCCUPIED, 
            BlockStateProperties.CHISELED_BOOKSHELF_SLOT_3_OCCUPIED, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_4_OCCUPIED, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_5_OCCUPIED);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    
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
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
        SLOT_OCCUPIED_PROPERTIES.forEach(prop -> pBuilder.add(prop));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof CarvedBookshelfTileEntity tile) {
            if (!pStack.is(ItemTags.BOOKSHELF_BOOKS)) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            } else {
                OptionalInt slotOpt = this.getHitSlot(pHitResult, pState);
                if (slotOpt.isEmpty()) {
                    return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                } else if (pState.getValue(SLOT_OCCUPIED_PROPERTIES.get(slotOpt.getAsInt()))) {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                } else {
                    addBook(pLevel, pPos, pPlayer, tile, pStack, slotOpt.getAsInt());
                    return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
                }
            }
        } else {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof CarvedBookshelfTileEntity tile) {
            OptionalInt slotOpt = this.getHitSlot(pHitResult, pState);
            if (slotOpt.isEmpty()) {
                return InteractionResult.PASS;
            } else if (!pState.getValue(SLOT_OCCUPIED_PROPERTIES.get(slotOpt.getAsInt()))) {
                return InteractionResult.CONSUME;
            } else {
                removeBook(pLevel, pPos, pPlayer, tile, slotOpt.getAsInt());
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private OptionalInt getHitSlot(BlockHitResult pHitReselt, BlockState pState) {
        return getRelativeHitCoordinatesForBlockFace(pHitReselt, pState.getValue(HorizontalDirectionalBlock.FACING)).map(hitPos -> {
            int i = hitPos.y >= 0.5F ? 0 : 1;
            int j = getSection(hitPos.x);
            return OptionalInt.of(j + i * BOOKS_PER_ROW);
        }).orElseGet(OptionalInt::empty);
    }

    private static void addBook(Level pLevel, BlockPos pPos, Player pPlayer, CarvedBookshelfTileEntity tile, ItemStack bookStack, int slot) {
        if (!pLevel.isClientSide) {
            // Set the book stack into the tile entity's slot
            pPlayer.awardStat(Stats.ITEM_USED.get(bookStack.getItem()));
            SoundEvent soundEvent = bookStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_INSERT;
            tile.addBook(slot, bookStack.split(1));
            pLevel.playSound(null, pPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private static void removeBook(Level pLevel, BlockPos pPos, Player pPlayer, CarvedBookshelfTileEntity tile, int slot) {
        if (!pLevel.isClientSide) {
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
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (pState.getBlock() != pNewState.getBlock()) {
            if (pLevel.getBlockEntity(pPos) instanceof CarvedBookshelfTileEntity tile) {
                tile.dropContents(pLevel, pPos);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CarvedBookshelfTileEntity(pPos, pState);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        if (pLevel.isClientSide) {
            return 0;
        } else if (pLevel.getBlockEntity(pPos) instanceof CarvedBookshelfTileEntity tile) {
            return tile.getLastInteractedSlot() + 1;
        } else {
            return 0;
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
