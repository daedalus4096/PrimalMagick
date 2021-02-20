package com.verdantartifice.primalmagic.common.tiles.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.TileToClientPacket;
import com.verdantartifice.primalmagic.common.network.packets.data.TileToServerPacket;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

/**
 * Middleware class for a tile entity for the mod.  Handles things like tile syncing and relevant
 * messages between the client and server regarding the tile.
 * 
 * @author Daedalus4096
 */
public class TilePM extends TileEntity {
    public TilePM(TileEntityType<?> type) {
        super(type);
    }
    
    /**
     * Update this tile's block in the world, optionally re-rendering it.
     * 
     * @param rerender whether to re-render the tile's block
     */
    public void syncTile(boolean rerender) {
        BlockState state = this.world.getBlockState(this.pos);
        int flags = Constants.BlockFlags.BLOCK_UPDATE;
        if (!rerender) {
            flags |= Constants.BlockFlags.NO_RERENDER;
        }
        this.world.notifyBlockUpdate(this.pos, state, state, flags);
    }
    
    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }
    
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.getBlockState(), pkt.getNbtCompound());
    }
    
    /**
     * Sync the given data to the instance of this tile entity on the given player's client, or to
     * all clients in range if no player is specified.
     * 
     * @param nbt the data to be synced
     * @param player the player whose client is to receive the given data
     */
    public void sendMessageToClient(CompoundNBT nbt, @Nullable ServerPlayerEntity player) {
        if (player == null) {
            if (this.hasWorld()) {
                PacketHandler.sendToAllAround(new TileToClientPacket(this.pos, nbt), this.world.getDimensionKey(), this.pos, 128.0D);
            }
        } else {
            PacketHandler.sendToPlayer(new TileToClientPacket(this.pos, nbt), player);
        }
    }
    
    /**
     * Sync the given data to the server instance of this tile entity.
     * 
     * @param nbt the data to be synced
     */
    public void sendMessageToServer(CompoundNBT nbt) {
        PacketHandler.sendToServer(new TileToServerPacket(this.pos, nbt));
    }
    
    /**
     * Process a message sent from a client instance of this tile entity.
     * 
     * @param nbt the received data
     * @param player the player whose client sent the given data
     * @see {@link #sendMessageToServer(CompoundNBT)}
     */
    public void onMessageFromClient(CompoundNBT nbt, @Nonnull ServerPlayerEntity player) {
        // Do nothing by default
    }
    
    /**
     * Process a message sent from the server instance of this tile entity.
     * 
     * @param nbt the received data
     * @see {@link #sendMessageToClient(CompoundNBT, ServerPlayerEntity)}
     */
    public void onMessageFromServer(CompoundNBT nbt) {
        // Do nothing by default
    }
}
