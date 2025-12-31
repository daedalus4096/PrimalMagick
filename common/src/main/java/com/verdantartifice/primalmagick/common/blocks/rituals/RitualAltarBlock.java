package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

/**
 * Block definition for the ritual altar.  It is the central component of magickal rituals, providing
 * the salt "power" that enables the functionality of other ritual props.  It's also where the ritual
 * output appears.
 * 
 * @author Daedalus4096
 */
public class RitualAltarBlock extends BaseEntityBlock implements ISaltPowered {
    public static final MapCodec<RitualAltarBlock> CODEC = simpleCodec(RitualAltarBlock::new);
    
    public RitualAltarBlock(Block.Properties properties) {
        super(properties);
    }
    
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        for (Direction dir : Direction.values()) {
            worldIn.updateNeighborsAt(pos.relative(dir), this);
        }
    }
    
    public int getMaxSaltPower() {
        return 15;
    }
    
    public int getMaxSafeSalt() {
        return 16;
    }
    
    @Override
    public int getStrongSaltPower(@NotNull BlockState blockState, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return (side != Direction.UP) ? this.getMaxSaltPower() : 0;
    }
    
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return Services.BLOCK_ENTITY_PROTOTYPES.ritualAltar().create(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityTypesPM.RITUAL_ALTAR.get(), Services.BLOCK_ENTITY_TICKERS.ritualAltar());
    }

    @Override
    @NotNull
    protected InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!worldIn.isClientSide()) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof RitualAltarTileEntity altarTile) {
                if (!altarTile.getItem().isEmpty()) {
                    // When activating a full altar, pick up the item
                    ItemStack stack = altarTile.getItem().copy();
                    altarTile.setItem(ItemStack.EMPTY);
                    if (!stack.isEmpty() && !player.getInventory().add(stack)) {
                        player.drop(stack, false);
                    }
                    player.getInventory().setChanged();
                    worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useWithoutItem(state, worldIn, pos, player, hit);
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        for (Direction dir : Direction.values()) {
            level.updateNeighborsAt(pos.relative(dir), state.getBlock());
        }
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
