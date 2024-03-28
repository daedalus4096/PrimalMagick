package com.verdantartifice.primalmagick.common.network.packets.scribe_table;

import com.verdantartifice.primalmagick.common.menus.ScribeStudyVocabularyMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent to trigger a server-side vocabulary study of the slotted static book on a scribe table.
 * 
 * @author Daedalus4096
 */
public class StudyVocabularyActionPacket implements IMessageToServer {
    protected final int windowId;
    protected final int slotId;
    
    public StudyVocabularyActionPacket(int windowId, int slotId) {
        this.windowId = windowId;
        this.slotId = slotId;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(StudyVocabularyActionPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
        buf.writeVarInt(message.slotId);
    }
    
    public static StudyVocabularyActionPacket decode(FriendlyByteBuf buf) {
        return new StudyVocabularyActionPacket(buf.readVarInt(), buf.readVarInt());
    }
    
    public static void onMessage(StudyVocabularyActionPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof ScribeStudyVocabularyMenu menu) {
            // Trigger the study if the open menu window matches the given one
            menu.doStudyClick(player, message.slotId);
        }
    }
}
