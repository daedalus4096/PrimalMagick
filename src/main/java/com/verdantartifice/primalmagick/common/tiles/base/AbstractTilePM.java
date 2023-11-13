package com.verdantartifice.primalmagick.common.tiles.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.TileToClientPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.TileToServerPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Middleware class for a tile entity for the mod.  Handles things like tile syncing and relevant
 * messages between the client and server regarding the tile.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTilePM extends BlockEntity {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    public AbstractTilePM(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    /**
     * Update this tile's block in the world, optionally re-rendering it.
     * 
     * @param rerender whether to re-render the tile's block
     */
    public void syncTile(boolean rerender) {
        if (this.hasLevel()) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            int flags = Block.UPDATE_CLIENTS;
            if (!rerender) {
                flags |= Block.UPDATE_INVISIBLE;
            }
            this.level.sendBlockUpdated(this.worldPosition, state, state, flags);
        }
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag retVal = new CompoundTag();
        this.saveAdditional(retVal);
        return retVal;
    }
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) {
            this.load(pkt.getTag());
        }
    }
    
    /**
     * Sync the given data to the instance of this tile entity on the given player's client, or to
     * all clients in range if no player is specified.
     * 
     * @param nbt the data to be synced
     * @param player the player whose client is to receive the given data
     */
    public void sendMessageToClient(CompoundTag nbt, @Nullable ServerPlayer player) {
        if (this.hasLevel() && !this.getLevel().isClientSide) {
            if (player == null) {
                PacketHandler.sendToAllAround(new TileToClientPacket(this.worldPosition, nbt), this.level.dimension(), this.worldPosition, 128.0D);
            } else {
                PacketHandler.sendToPlayer(new TileToClientPacket(this.worldPosition, nbt), player);
            }
        }
    }
    
    /**
     * Sync the given data to the server instance of this tile entity.
     * 
     * @param nbt the data to be synced
     */
    public void sendMessageToServer(CompoundTag nbt) {
        if (this.hasLevel() && this.getLevel().isClientSide) {
            PacketHandler.sendToServer(new TileToServerPacket(this.worldPosition, nbt));
        }
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

    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }
}
