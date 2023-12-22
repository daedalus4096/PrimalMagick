package com.verdantartifice.primalmagick.common.network.packets.fx;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent from the server to remove a prop marker particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class RemovePropMarkerPacket implements IMessageToClient {
    protected BlockPos pos;
    
    public RemovePropMarkerPacket() {}
    
    public RemovePropMarkerPacket(@Nonnull BlockPos pos) {
        this.pos = pos;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(RemovePropMarkerPacket message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }
    
    public static RemovePropMarkerPacket decode(FriendlyByteBuf buf) {
        RemovePropMarkerPacket message = new RemovePropMarkerPacket();
        message.pos = buf.readBlockPos();
        return message;
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
