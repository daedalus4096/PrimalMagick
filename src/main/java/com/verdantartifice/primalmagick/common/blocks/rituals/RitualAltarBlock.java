package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.verdantartifice.primalmagick.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
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
 * Block definition for the ritual altar.  It is the central component of magickal rituals, providing
 * the salt "power" that enables the functionality of other ritual props.  It's also where the ritual
 * output appears.
 * 
 * @author Daedalus4096
 */
public class RitualAltarBlock extends BaseEntityBlock implements ISaltPowered {
    public RitualAltarBlock() {
        super(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion());
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        for (Direction dir : Direction.values()) {
            worldIn.updateNeighborsAt(pos.relative(dir), this);
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving) {
            for (Direction dir : Direction.values()) {
                worldIn.updateNeighborsAt(pos.relative(dir), this);
            }
        }
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof RitualAltarTileEntity altarTile) {
                altarTile.dropContents(worldIn, pos);
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
    
    public int getMaxSaltPower() {
        return 15;
    }
    
    public int getMaxSafeSalt() {
        return 16;
    }
    
    @Override
    public int getStrongSaltPower(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return (side != Direction.UP) ? this.getMaxSaltPower() : 0;
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RitualAltarTileEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TileEntityTypesPM.RITUAL_ALTAR.get(), RitualAltarTileEntity::tick);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && handIn == InteractionHand.MAIN_HAND) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof RitualAltarTileEntity altarTile) {
                if (!altarTile.getItem().isEmpty() && player.getItemInHand(handIn).isEmpty()) {
                    // When activating a full altar with an empty hand, pick up the item
                    ItemStack stack = altarTile.getItem().copy();
                    altarTile.setItem(ItemStack.EMPTY);
                    player.setItemInHand(handIn, stack);
                    player.getInventory().setChanged();
                    worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }
}
