package com.verdantartifice.primalmagick.common.network.packets.scribe_table;

import com.verdantartifice.primalmagick.common.menus.ScribeTranscribeWorksMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to trigger a server-side transcription of the slotted static book on a scribe table.
 * Necessary to keep the inventories in sync and properly utilize the player's comprehension.
 * 
 * @author Daedalus4096
 */
public class TranscribeActionPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, TranscribeActionPacket> STREAM_CODEC = StreamCodec.ofMember(TranscribeActionPacket::encode, TranscribeActionPacket::decode);

    protected final int windowId;

    public TranscribeActionPacket(int windowId) {
        this.windowId = windowId;
    }
    
    public static void encode(TranscribeActionPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
    }
    
    public static TranscribeActionPacket decode(RegistryFriendlyByteBuf buf) {
        return new TranscribeActionPacket(buf.readVarInt());
    }
    
    public static void onMessage(TranscribeActionPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof ScribeTranscribeWorksMenu menu) {
            // Trigger the transcription if the open menu window matches the given one
            menu.doTranscribe();
        }
    }
}
