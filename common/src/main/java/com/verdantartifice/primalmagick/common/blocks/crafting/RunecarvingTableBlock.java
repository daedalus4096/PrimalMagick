package com.verdantartifice.primalmagick.common.blocks.crafting;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Block definition for the runecarving table.  The runecarving table lets the player turn stone
 * slabs and lapis lazuli into mystical runes, which can be scribed onto items to imbue them with
 * controllable enchantments.
 * 
 * @author Daedalus4096
 */
public class RunecarvingTableBlock extends BaseEntityBlock {
    public static final MapCodec<RunecarvingTableBlock> CODEC = simpleCodec(RunecarvingTableBlock::new);
    
    protected static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public RunecarvingTableBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult hit) {
        if (!worldIn.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Open the GUI for the research table
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof RunecarvingTableTileEntity tableTile) {
                serverPlayer.openMenu(tableTile, tile.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RunecarvingTableTileEntity(pos, state);
    }
    
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof RunecarvingTableTileEntity castTile) {
                castTile.dropContents(worldIn, pos);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
