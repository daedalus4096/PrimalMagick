package com.verdantartifice.primalmagick.common.blocks.mana;

import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.mana.WandChargerTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Block definition for the wand charger.  A wand charger allows a player to charge a wand with mana by
 * feeding it magickal essence.  The type and amount of mana gained are determined by the essence used.
 * It is intended as an alternative to mana fonts, especially early in mod progression.
 * 
 * @author Daedalus4096
 */
public class WandChargerBlock extends BaseEntityBlock {
    public WandChargerBlock() {
        super(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion());
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WandChargerTileEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TileEntityTypesPM.WAND_CHARGER.get(), WandChargerTileEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Open the GUI for the wand charger
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof WandChargerTileEntity chargerTile) {
                serverPlayer.openMenu(chargerTile, tile.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof WandChargerTileEntity chargerTile) {
                chargerTile.dropContents(worldIn, pos);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }
}
