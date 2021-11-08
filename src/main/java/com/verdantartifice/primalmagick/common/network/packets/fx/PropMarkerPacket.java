package com.verdantartifice.primalmagick.common.network.packets.fx;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet sent from the server to trigger a prop marker particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class PropMarkerPacket implements IMessageToClient {
    protected BlockPos pos;
    
    public PropMarkerPacket() {}
    
    public PropMarkerPacket(@Nonnull BlockPos pos) {
        this.pos = pos;
    }
    
    public static void encode(PropMarkerPacket message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }
    
    public static PropMarkerPacket decode(FriendlyByteBuf buf) {
        PropMarkerPacket message = new PropMarkerPacket();
        message.pos = buf.readBlockPos();
        return message;
    }
    
    public static class Handler {
        @SuppressWarnings("deprecation")
        public static void onMessage(PropMarkerPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                Level world = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentLevel() : null;
                // Only process positions that are currently loaded into the world.  Safety check to prevent
                // resource thrashing from falsified packets.
                if (world != null && world.hasChunkAt(message.pos)) {
                    FxDispatcher.INSTANCE.propMarker(message.pos);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
