package com.verdantartifice.primalmagick.common.rituals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PropMarkerPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.RemovePropMarkerPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Interface indicating whether a block can serve as a prop for magickal rituals.
 * 
 * @author Daedalus4096
 */
public interface IRitualPropBlock extends ISaltPowered, IRitualStabilizer {
    public boolean isPropActivated(BlockState state, Level world, BlockPos pos);
    
    public default void onPropActivated(BlockState state, Level world, BlockPos pos, float stabilityBonus) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IRitualPropTileEntity propTile) {
            propTile.notifyAltarOfPropActivation(stabilityBonus);
        }
    }
    
    public default boolean isPropOpen(BlockState state, Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        return (tile instanceof IRitualPropTileEntity propTile) && propTile.isPropOpen();
    }
    
    public default void openProp(BlockState state, Level world, BlockPos pos, @Nullable Player player, BlockPos altarPos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IRitualPropTileEntity propTile) {
            propTile.setPropOpen(true);
            PacketHandler.sendToAllAround(new PropMarkerPacket(pos), world.dimension(), pos, 32.0D);
            if (player != null) {
                this.sendPropStatusMessage(player);
            }
        }
    }
    
    public default void closeProp(BlockState state, Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IRitualPropTileEntity propTile) {
            propTile.setPropOpen(false);
            PacketHandler.sendToAllAround(new RemovePropMarkerPacket(pos), world.dimension(), pos, 32.0D);
        }
    }
    
    public default void sendPropStatusMessage(@Nonnull Player player) {
        player.displayClientMessage(Component.translatable(this.getPropTranslationKey()), false);
    }
    
    public String getPropTranslationKey();
    
    /**
     * Indicated whether the block is a universal ritual prop that can and will be used by any
     * ritual started in range.
     * 
     * @return whether the block is a universal ritual prop
     */
    public default boolean isUniversal() {
        return false;
    }
}
