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
 * Packet sent from the server to trigger a prop marker particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class PropMarkerPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("prop_marker");
    public static final StreamCodec<RegistryFriendlyByteBuf, PropMarkerPacket> STREAM_CODEC = StreamCodec.ofMember(PropMarkerPacket::encode, PropMarkerPacket::decode);

    protected final BlockPos pos;
    protected final int lifetime;
    
    public PropMarkerPacket(@Nonnull BlockPos pos) {
        this(pos, FxDispatcher.DEFAULT_PROP_MARKER_LIFETIME);
    }
    
    public PropMarkerPacket(@Nonnull BlockPos pos, int lifetime) {
        this.pos = pos;
        this.lifetime = lifetime;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(PropMarkerPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeVarInt(message.lifetime);
    }
    
    public static PropMarkerPacket decode(RegistryFriendlyByteBuf buf) {
        return new PropMarkerPacket(buf.readBlockPos(), buf.readVarInt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(PacketContext<PropMarkerPacket> ctx) {
        PropMarkerPacket message = ctx.message();
        Level world = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentLevel() : null;
        // Only process positions that are currently loaded into the world.  Safety check to prevent
        // resource thrashing from falsified packets.
        if (world != null && world.hasChunkAt(message.pos)) {
            FxDispatcher.INSTANCE.propMarker(message.pos, message.lifetime);
        }
    }
}
