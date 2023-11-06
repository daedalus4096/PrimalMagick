package com.verdantartifice.primalmagick.common.blocks.mana;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
 * Block definition for the auto-charger.  An auto-charger will automatically siphon mana from
 * nearby mana fonts and use it to charge the contained wand.
 * 
 * @author Daedalus4096
 */
public class AutoChargerBlock extends BaseEntityBlock {
    public AutoChargerBlock() {
        super(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).noOcclusion());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AutoChargerTileEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TileEntityTypesPM.AUTO_CHARGER.get(), AutoChargerTileEntity::tick);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide && handIn == InteractionHand.MAIN_HAND) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof AutoChargerTileEntity charger) {
                ItemStack stack = player.getItemInHand(handIn);
                if (charger.getItem().isEmpty() && (stack.getItem() instanceof IWand wand || stack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).isPresent())) {
                    // If a wand is in hand and the charger is empty, deposit the wand
                    charger.setItem(stack);
                    player.setItemInHand(handIn, ItemStack.EMPTY);
                    player.getInventory().setChanged();
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                    return InteractionResult.SUCCESS;
                } else if (!charger.getItem().isEmpty() && stack.isEmpty()) {
                    // If the hand is empty and a wand is in the charger, remove the wand
                    ItemStack chargerStack = charger.getItem();
                    charger.setItem(ItemStack.EMPTY);
                    player.setItemInHand(handIn, chargerStack);
                    player.getInventory().setChanged();
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, level, pos, player, handIn, hit);
    }
    
    @SuppressWarnings("deprecation")
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
}
