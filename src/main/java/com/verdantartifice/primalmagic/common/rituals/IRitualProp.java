package com.verdantartifice.primalmagic.common.rituals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.PropMarkerPacket;
import com.verdantartifice.primalmagic.common.network.packets.fx.RemovePropMarkerPacket;
import com.verdantartifice.primalmagic.common.tiles.rituals.AbstractRitualPropTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * Interface indicating whether a block can serve as a prop for magical rituals.
 * 
 * @author Daedalus4096
 */
public interface IRitualProp extends ISaltPowered {
    public boolean isPropActivated(BlockState state, World world, BlockPos pos);
    
    public default void onPropActivated(BlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AbstractRitualPropTileEntity) {
            ((AbstractRitualPropTileEntity)tile).notifyAltarOfPropActivation();
        }
    }
    
    public default boolean isPropOpen(BlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return (tile instanceof AbstractRitualPropTileEntity) && ((AbstractRitualPropTileEntity)tile).getAltarPos() != null;
    }
    
    public default void openProp(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player, BlockPos altarPos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AbstractRitualPropTileEntity) {
            ((AbstractRitualPropTileEntity)tile).setAltarPos(altarPos);
            PacketHandler.sendToAllAround(new PropMarkerPacket(pos), world.dimension.getType(), pos, 32.0D);
            if (player != null) {
                this.sendPropStatusMessage(player);
            }
        }
    }
    
    public default void closeProp(BlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AbstractRitualPropTileEntity) {
            ((AbstractRitualPropTileEntity)tile).setAltarPos(null);
            PacketHandler.sendToAllAround(new RemovePropMarkerPacket(pos), world.dimension.getType(), pos, 32.0D);
        }
    }
    
    public default void sendPropStatusMessage(@Nonnull PlayerEntity player) {
        player.sendStatusMessage(new TranslationTextComponent(this.getPropTranslationKey()), false);
    }
    
    public String getPropTranslationKey();
    
    public float getUsageStabilityBonus();
}
