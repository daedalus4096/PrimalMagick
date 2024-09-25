package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.menus.AnalysisTableMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to trigger a server-side scan of the slotted item on an analysis table.  Necessary to
 * keep the inventories in sync and properly credit the resulting research.
 * 
 * @author Daedalus4096
 */
public class AnalysisActionPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, AnalysisActionPacket> STREAM_CODEC = StreamCodec.ofMember(AnalysisActionPacket::encode, AnalysisActionPacket::decode);

    protected final int windowId;
    
    public AnalysisActionPacket(int windowId) {
        this.windowId = windowId;
    }
    
    public static void encode(AnalysisActionPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
    }
    
    public static AnalysisActionPacket decode(RegistryFriendlyByteBuf buf) {
        return new AnalysisActionPacket(buf.readVarInt());
    }
    
    public static void onMessage(AnalysisActionPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof AnalysisTableMenu menu) {
            // Trigger the scan if the open menu window matches the given one
            menu.doScan();
        }
    }
}
