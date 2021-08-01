package com.verdantartifice.primalmagic.common.tiles.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.TileToClientPacket;
import com.verdantartifice.primalmagic.common.network.packets.data.TileToServerPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

/**
 * Middleware class for a tile entity for the mod.  Handles things like tile syncing and relevant
 * messages between the client and server regarding the tile.
 * 
 * @author Daedalus4096
 */
public class TilePM extends BlockEntity {
    public TilePM(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    /**
     * Update this tile's block in the world, optionally re-rendering it.
     * 
     * @param rerender whether to re-render the tile's block
     */
    public void syncTile(boolean rerender) {
        BlockState state = this.level.getBlockState(this.worldPosition);
        int flags = Constants.BlockFlags.BLOCK_UPDATE;
        if (!rerender) {
            flags |= Constants.BlockFlags.NO_RERENDER;
        }
        this.level.sendBlockUpdated(this.worldPosition, state, state, flags);
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }
    
    /**
     * Sync the given data to the instance of this tile entity on the given player's client, or to
     * all clients in range if no player is specified.
     * 
     * @param nbt the data to be synced
     * @param player the player whose client is to receive the given data
     */
    public void sendMessageToClient(CompoundTag nbt, @Nullable ServerPlayer player) {
        if (player == null) {
            if (this.hasLevel()) {
                PacketHandler.sendToAllAround(new TileToClientPacket(this.worldPosition, nbt), this.level.dimension(), this.worldPosition, 128.0D);
            }
        } else {
            PacketHandler.sendToPlayer(new TileToClientPacket(this.worldPosition, nbt), player);
        }
    }
    
    /**
     * Sync the given data to the server instance of this tile entity.
     * 
     * @param nbt the data to be synced
     */
    public void sendMessageToServer(CompoundTag nbt) {
        PacketHandler.sendToServer(new TileToServerPacket(this.worldPosition, nbt));
    }
    
    /**
     * Process a message sent from a client instance of this tile entity.
     * 
     * @param nbt the received data
     * @param player the player whose client sent the given data
     * @see {@link #sendMessageToServer(CompoundNBT)}
     */
    public void onMessageFromClient(CompoundTag nbt, @Nonnull ServerPlayer player) {
        // Do nothing by default
    }
    
    /**
     * Process a message sent from the server instance of this tile entity.
     * 
     * @param nbt the received data
     * @see {@link #sendMessageToClient(CompoundNBT, ServerPlayerEntity)}
     */
    public void onMessageFromServer(CompoundTag nbt) {
        // Do nothing by default
    }
}
