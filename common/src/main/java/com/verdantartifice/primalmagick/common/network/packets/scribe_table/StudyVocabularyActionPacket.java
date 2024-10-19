package com.verdantartifice.primalmagick.common.network.packets.scribe_table;

import com.verdantartifice.primalmagick.common.menus.ScribeStudyVocabularyMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet sent to trigger a server-side vocabulary study of the slotted static book on a scribe table.
 * 
 * @author Daedalus4096
 */
public class StudyVocabularyActionPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("study_vocabulary_action");
    public static final StreamCodec<RegistryFriendlyByteBuf, StudyVocabularyActionPacket> STREAM_CODEC = StreamCodec.ofMember(StudyVocabularyActionPacket::encode, StudyVocabularyActionPacket::decode);

    protected final int windowId;
    protected final int slotId;
    
    public StudyVocabularyActionPacket(int windowId, int slotId) {
        this.windowId = windowId;
        this.slotId = slotId;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(StudyVocabularyActionPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
        buf.writeVarInt(message.slotId);
    }
    
    public static StudyVocabularyActionPacket decode(RegistryFriendlyByteBuf buf) {
        return new StudyVocabularyActionPacket(buf.readVarInt(), buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<StudyVocabularyActionPacket> ctx) {
        StudyVocabularyActionPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        if (player.containerMenu instanceof ScribeStudyVocabularyMenu menu && menu.containerId == message.windowId) {
            // Trigger the study if the open menu window matches the given one
            menu.doStudyClick(player, message.slotId);
        }
    }
}
