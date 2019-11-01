package com.verdantartifice.primalmagic.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.containers.AnalysisTableContainer;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class AnalysisActionPacket implements IMessageToServer {
    protected int windowId;
    
    public AnalysisActionPacket() {
        this.windowId = -1;
    }
    
    public AnalysisActionPacket(int windowId) {
        this.windowId = windowId;
    }
    
    public static void encode(AnalysisActionPacket message, PacketBuffer buf) {
        buf.writeVarInt(message.windowId);
    }
    
    public static AnalysisActionPacket decode(PacketBuffer buf) {
        AnalysisActionPacket message = new AnalysisActionPacket();
        message.windowId = buf.readVarInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(AnalysisActionPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player.openContainer != null && player.openContainer.windowId == message.windowId && player.openContainer instanceof AnalysisTableContainer) {
                    ((AnalysisTableContainer)player.openContainer).doScan();
                }
            });
        }
    }
}
