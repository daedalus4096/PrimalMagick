package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

/**
 * Packet sent from the server to remove a prop marker particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class RemovePropMarkerPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("remove_prop_marker");
    public static final StreamCodec<RegistryFriendlyByteBuf, RemovePropMarkerPacket> STREAM_CODEC = StreamCodec.ofMember(RemovePropMarkerPacket::encode, RemovePropMarkerPacket::decode);

    protected final BlockPos pos;
    
    public RemovePropMarkerPacket(@Nonnull BlockPos pos) {
        this.pos = pos;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(RemovePropMarkerPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }
    
    public static RemovePropMarkerPacket decode(RegistryFriendlyByteBuf buf) {
        return new RemovePropMarkerPacket(buf.readBlockPos());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(PacketContext<RemovePropMarkerPacket> ctx) {
        RemovePropMarkerPacket message = ctx.message();
        Level world = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentLevel() : null;
        // Only process positions that are currently loaded into the world.  Safety check to prevent
        // resource thrashing from falsified packets.
        if (world != null && world.hasChunkAt(message.pos)) {
            FxDispatcher.INSTANCE.removePropMarker(message.pos);
        }
    }
}
