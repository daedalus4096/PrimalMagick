package com.verdantartifice.primalmagic.common.rituals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.PropMarkerPacket;
import com.verdantartifice.primalmagic.common.network.packets.fx.RemovePropMarkerPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Interface indicating whether a block can serve as a prop for magical rituals.
 * 
 * @author Daedalus4096
 */
public interface IRitualPropBlock extends ISaltPowered, IRitualStabilizer {
    public boolean isPropActivated(BlockState state, Level world, BlockPos pos);
    
    public default void onPropActivated(BlockState state, Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IRitualPropTileEntity) {
            ((IRitualPropTileEntity)tile).notifyAltarOfPropActivation();
        }
    }
    
    public default boolean isPropOpen(BlockState state, Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        return (tile instanceof IRitualPropTileEntity) && ((IRitualPropTileEntity)tile).getAltarPos() != null;
    }
    
    public default void openProp(BlockState state, Level world, BlockPos pos, @Nullable Player player, BlockPos altarPos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IRitualPropTileEntity) {
            ((IRitualPropTileEntity)tile).setAltarPos(altarPos);
            PacketHandler.sendToAllAround(new PropMarkerPacket(pos), world.dimension(), pos, 32.0D);
            if (player != null) {
                this.sendPropStatusMessage(player);
            }
        }
    }
    
    public default void closeProp(BlockState state, Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IRitualPropTileEntity) {
            ((IRitualPropTileEntity)tile).setAltarPos(null);
            PacketHandler.sendToAllAround(new RemovePropMarkerPacket(pos), world.dimension(), pos, 32.0D);
        }
    }
    
    public default void sendPropStatusMessage(@Nonnull Player player) {
        player.displayClientMessage(new TranslatableComponent(this.getPropTranslationKey()), false);
    }
    
    public String getPropTranslationKey();
    
    public float getUsageStabilityBonus();
}
