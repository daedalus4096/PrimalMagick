package com.verdantartifice.primalmagick.common.network.packets.fx;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet sent from the server to remove a prop marker particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class RemovePropMarkerPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, RemovePropMarkerPacket> STREAM_CODEC = StreamCodec.ofMember(RemovePropMarkerPacket::encode, RemovePropMarkerPacket::decode);

    protected final BlockPos pos;
    
    public RemovePropMarkerPacket(@Nonnull BlockPos pos) {
        this.pos = pos;
    }
    
    public static void encode(RemovePropMarkerPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }
    
    public static RemovePropMarkerPacket decode(RegistryFriendlyByteBuf buf) {
        return new RemovePropMarkerPacket(buf.readBlockPos());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(RemovePropMarkerPacket message, CustomPayloadEvent.Context ctx) {
        Level world = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentLevel() : null;
        // Only process positions that are currently loaded into the world.  Safety check to prevent
        // resource thrashing from falsified packets.
        if (world != null && world.hasChunkAt(message.pos)) {
            FxDispatcher.INSTANCE.removePropMarker(message.pos);
        }
    }
}
