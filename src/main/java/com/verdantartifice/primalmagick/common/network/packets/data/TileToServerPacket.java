package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet to sync tile entity data from the client to the server.  Primarily used to request a sync of
 * tile inventory data upon the tile entity loading into the world.
 * 
 * @author Daedalus4096
 */
public class TileToServerPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, TileToServerPacket> STREAM_CODEC = StreamCodec.ofMember(TileToServerPacket::encode, TileToServerPacket::decode);

    protected final BlockPos pos;
    protected final CompoundTag data;
    
    public TileToServerPacket(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }
    
    public static void encode(TileToServerPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeNbt(message.data);
    }
    
    public static TileToServerPacket decode(RegistryFriendlyByteBuf buf) {
        return new TileToServerPacket(buf.readBlockPos(), buf.readNbt());
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
