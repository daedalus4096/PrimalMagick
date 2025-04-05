package com.verdantartifice.primalmagick.common.blocks.crafting;

import com.verdantartifice.primalmagick.common.menus.WandAssemblyTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Block definition for the wand assembly table.  A wand assembly table allows a player to construct a
 * modular wand from component cores, caps, and gems.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyTableBlock extends Block {
    protected static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public WandAssemblyTableBlock() {
        super(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion());

        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.WATERLOGGED, false));
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Make the block face the player when placed
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
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
        builder.add(FACING, BlockStateProperties.WATERLOGGED);
    }
    
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult hit) {
        if (!worldIn.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Open the GUI for the wand assembly table
            serverPlayer.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
                    return new WandAssemblyTableMenu(windowId, inv, ContainerLevelAccess.create(worldIn, pos));
                }

                @Override
                public Component getDisplayName() {
                    return Component.translatable(WandAssemblyTableBlock.this.getDescriptionId());
                }
            });
        }
        return InteractionResult.SUCCESS;
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
}
