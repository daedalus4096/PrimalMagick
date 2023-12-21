package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.menus.AnalysisTableMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent to trigger a server-side scan of the slotted item on an analysis table.  Necessary to
 * keep the inventories in sync and properly credit the resulting research.
 * 
 * @author Daedalus4096
 */
public class AnalysisActionPacket implements IMessageToServer {
    protected int windowId;
    
    public AnalysisActionPacket() {
        this.windowId = -1;
    }
    
    public AnalysisActionPacket(int windowId) {
        this.windowId = windowId;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(AnalysisActionPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
    }
    
    public static AnalysisActionPacket decode(FriendlyByteBuf buf) {
        AnalysisActionPacket message = new AnalysisActionPacket();
        message.windowId = buf.readVarInt();
        return message;
    }
    
    public static void onMessage(AnalysisActionPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof AnalysisTableMenu menu) {
            // Trigger the scan if the open menu window matches the given one
            menu.doScan();
        }
    }
}
