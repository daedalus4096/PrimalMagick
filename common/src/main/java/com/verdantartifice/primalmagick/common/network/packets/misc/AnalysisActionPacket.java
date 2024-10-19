package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.menus.AnalysisTableMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet sent to trigger a server-side scan of the slotted item on an analysis table.  Necessary to
 * keep the inventories in sync and properly credit the resulting research.
 * 
 * @author Daedalus4096
 */
public class AnalysisActionPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("analysis_action");
    public static final StreamCodec<RegistryFriendlyByteBuf, AnalysisActionPacket> STREAM_CODEC = StreamCodec.ofMember(AnalysisActionPacket::encode, AnalysisActionPacket::decode);

    protected final int windowId;
    
    public AnalysisActionPacket(int windowId) {
        this.windowId = windowId;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(AnalysisActionPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
    }
    
    public static AnalysisActionPacket decode(RegistryFriendlyByteBuf buf) {
        return new AnalysisActionPacket(buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<AnalysisActionPacket> ctx) {
        AnalysisActionPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        if (player.containerMenu instanceof AnalysisTableMenu menu && menu.containerId == message.windowId) {
            // Trigger the scan if the open menu window matches the given one
            menu.doScan();
        }
    }
}
