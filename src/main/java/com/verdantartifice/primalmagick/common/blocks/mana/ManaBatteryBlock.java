package com.verdantartifice.primalmagick.common.blocks.mana;

import java.util.List;

import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.sources.ManaContainerHelper;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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

/**
 * Block definition for a mana battery, such as the Mana Nexus.  A mana battery will automatically
 * siphon mana from nearby fonts and use that to charge its internal storage, which can then in turn
 * be used to charge wands or other devices.  Also accepts input mana from essence, breaking it down
 * like the Wand Charger does.
 * 
 * @author Daedalus4096
 */
public class ManaBatteryBlock extends BaseEntityBlock implements ITieredDevice {
    protected final DeviceTier tier;
    
    public ManaBatteryBlock(DeviceTier tier, Block.Properties properties) {
        super(properties);
        this.tier = tier;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ManaBatteryTileEntity(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, TileEntityTypesPM.MANA_BATTERY.get(), ManaBatteryTileEntity::tick);
    }

    @Override
    public void appendHoverText(ItemStack pStack, BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        ManaContainerHelper.appendHoverText(pStack, pTooltip);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            // Open the GUI for the battery
            BlockEntity tile = pLevel.getBlockEntity(pPos);
            if (tile instanceof ManaBatteryTileEntity batteryTile) {
                serverPlayer.openMenu(batteryTile, tile.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        ManaContainerHelper.setManaOnPlace(pLevel, pPos, pStack);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity tile = pLevel.getBlockEntity(pPos);
            if (tile instanceof ManaBatteryTileEntity batteryTile) {
                batteryTile.dropContents(pLevel, pPos);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }

    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }
}
