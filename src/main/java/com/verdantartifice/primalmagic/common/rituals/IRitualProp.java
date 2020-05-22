package com.verdantartifice.primalmagic.common.rituals;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.PropMarkerPacket;
import com.verdantartifice.primalmagic.common.network.packets.fx.RemovePropMarkerPacket;
import com.verdantartifice.primalmagic.common.tiles.rituals.AbstractRitualPropTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
    
    public default void openProp(BlockState state, World world, BlockPos pos, BlockPos altarPos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AbstractRitualPropTileEntity) {
            ((AbstractRitualPropTileEntity)tile).setAltarPos(altarPos);
            PacketHandler.sendToAllAround(new PropMarkerPacket(pos), world.dimension.getType(), pos, 32.0D);
        }
    }
    
    public default void closeProp(BlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AbstractRitualPropTileEntity) {
            ((AbstractRitualPropTileEntity)tile).setAltarPos(null);
            PacketHandler.sendToAllAround(new RemovePropMarkerPacket(pos), world.dimension.getType(), pos, 32.0D);
        }
    }
}
