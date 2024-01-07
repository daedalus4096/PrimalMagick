package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet to sync tile entity data from the client to the server.  Primarily used to request a sync of
 * tile inventory data upon the tile entity loading into the world.
 * 
 * @author Daedalus4096
 */
public class TileToServerPacket implements IMessageToServer {
    protected BlockPos pos;
    protected CompoundTag data;
    
    public TileToServerPacket() {
        this.pos = BlockPos.ZERO;
        this.data = null;
    }
    
    public TileToServerPacket(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(TileToServerPacket message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeNbt(message.data);
    }
    
    public static TileToServerPacket decode(FriendlyByteBuf buf) {
        TileToServerPacket message = new TileToServerPacket();
        message.pos = buf.readBlockPos();
        message.data = buf.readNbt();
        return message;
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(TileToServerPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer sender = ctx.getSender();
        Level world = sender.level();
        // Only process tile entities that are currently loaded into the world.  Safety check to prevent
        // resource thrashing from falsified packets.
        if (world != null && world.hasChunkAt(message.pos)) {
            if (world.getBlockEntity(message.pos) instanceof AbstractTilePM tile) {
                tile.onMessageFromClient(message.data == null ? new CompoundTag() : message.data, sender);
            }
        }
    }
}
