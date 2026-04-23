package com.verdantartifice.primalmagick.common.rituals;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PropMarkerPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.RemovePropMarkerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface indicating whether a block can serve as a prop for magickal rituals.
 * 
 * @author Daedalus4096
 */
public interface IRitualPropBlock extends ISaltPowered, IRitualStabilizer {
    boolean isPropActivated(BlockState state, Level world, BlockPos pos);
    
    default void onPropActivated(BlockState state, Level world, BlockPos pos, float stabilityBonus) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IRitualPropTileEntity propTile) {
            propTile.notifyAltarOfPropActivation(stabilityBonus);
        }
    }
    
    default boolean isPropOpen(BlockState state, Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        return (tile instanceof IRitualPropTileEntity propTile) && propTile.isPropOpen();
    }
    
    default void openProp(BlockState state, Level world, BlockPos pos, @Nullable Player player, BlockPos altarPos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IRitualPropTileEntity propTile) {
            propTile.setPropOpen(true);
            if (world instanceof ServerLevel serverLevel) {
                PacketHandler.sendToAllAround(new PropMarkerPacket(pos), serverLevel, pos, 32.0D);
            }
            if (player != null) {
                this.sendPropStatusMessage(player);
            }
        }
    }
    
    default void closeProp(BlockState state, Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IRitualPropTileEntity propTile) {
            propTile.setPropOpen(false);
            if (world instanceof ServerLevel serverLevel) {
                PacketHandler.sendToAllAround(new RemovePropMarkerPacket(pos), serverLevel, pos, 32.0D);
            }
        }
    }
    
    default void sendPropStatusMessage(@Nonnull Player player) {
        player.sendSystemMessage(Component.translatable(this.getPropTranslationKey()));
    }
    
    String getPropTranslationKey();
    
    /**
     * Indicated whether the block is a universal ritual prop that can and will be used by any
     * ritual started in range.
     * 
     * @return whether the block is a universal ritual prop
     */
    default boolean isUniversal() {
        return false;
    }
}
