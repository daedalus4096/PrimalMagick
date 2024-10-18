package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Packet to sync tile entity data from the server to the client.  Primarily used to sync tile entity
 * inventory data to the client for rendering purposes.  See the wand charger for an example.
 * 
 * @author Daedalus4096
 */
public class TileToClientPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("tile_to_client");
    public static final StreamCodec<RegistryFriendlyByteBuf, TileToClientPacket> STREAM_CODEC = StreamCodec.ofMember(TileToClientPacket::encode, TileToClientPacket::decode);

    protected final BlockPos pos;
    protected final CompoundTag data;
    
    public TileToClientPacket(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(TileToClientPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeNbt(message.data);
    }
    
    public static TileToClientPacket decode(RegistryFriendlyByteBuf buf) {
        return new TileToClientPacket(buf.readBlockPos(), buf.readNbt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(PacketContext<TileToClientPacket> ctx) {
        TileToClientPacket message = ctx.message();
        Level world = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentLevel() : null;
        // Only process tile entities that are currently loaded into the world.  Safety check to prevent
        // resource thrashing from falsified packets.
        if (world != null && world.hasChunkAt(message.pos)) {
            if (world.getBlockEntity(message.pos) instanceof AbstractTilePM tile) {
                tile.onMessageFromServer(message.data == null ? new CompoundTag() : message.data);
            }
        }
    }
}
