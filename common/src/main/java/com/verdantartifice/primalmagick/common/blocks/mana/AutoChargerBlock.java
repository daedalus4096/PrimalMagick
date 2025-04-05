package com.verdantartifice.primalmagick.common.blocks.mana;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Block definition for the auto-charger.  An auto-charger will automatically siphon mana from
 * nearby mana fonts and use it to charge the contained wand.
 * 
 * @author Daedalus4096
 */
public class AutoChargerBlock extends BaseEntityBlock {
    public static final MapCodec<AutoChargerBlock> CODEC = simpleCodec(AutoChargerBlock::new);
    
    public AutoChargerBlock(Block.Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Services.BLOCK_ENTITY_PROTOTYPES.autoCharger().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityTypesPM.AUTO_CHARGER.get(), Services.BLOCK_ENTITY_TICKERS.autoCharger());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide && handIn == InteractionHand.MAIN_HAND) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof AutoChargerTileEntity charger) {
                if (charger.getItem().isEmpty() && stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
                    // If a wand is in hand and the charger is empty, deposit the wand
                    charger.setItem(stack.copyWithCount(1));
                    stack.shrink(1);
                    if (stack.getCount() <= 0) {
                        player.setItemInHand(handIn, ItemStack.EMPTY);
                    }
                    player.getInventory().setChanged();
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                    return ItemInteractionResult.SUCCESS;
                } else if (!charger.getItem().isEmpty() && stack.isEmpty()) {
                    // If the hand is empty and a wand is in the charger, remove the wand
                    ItemStack chargerStack = charger.getItem();
                    charger.setItem(ItemStack.EMPTY);
                    if (!chargerStack.isEmpty() && !player.getInventory().add(chargerStack)) {
                        player.drop(chargerStack, false);
                    }
                    player.getInventory().setChanged();
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, handIn, hit);
    }
    
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof AutoChargerTileEntity castTile) {
                castTile.dropContents(worldIn, pos);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, pContext.getLevel().getFluidState(pContext.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    protected FluidState getFluidState(BlockState pState) {
        return pState.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if(pState.getValue(BlockStateProperties.WATERLOGGED)){
            pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
