package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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
    
    public static class Handler {
        @SuppressWarnings("deprecation")
        public static void onMessage(TileToServerPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer sender = ctx.get().getSender();
                Level world = sender.level;
                // Only process tile entities that are currently loaded into the world.  Safety check to prevent
                // resource thrashing from falsified packets.
                if (world != null && world.hasChunkAt(message.pos)) {
                    BlockEntity tile = world.getBlockEntity(message.pos);
                    if (tile != null && tile instanceof TilePM) {
                        ((TilePM)tile).onMessageFromClient(message.data == null ? new CompoundTag() : message.data, sender);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
