package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.List;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet to sync the last active grimoire research topic from the client to the server.
 * 
 * @author Daedalus4096
 */
public class SetResearchTopicHistoryPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, SetResearchTopicHistoryPacket> STREAM_CODEC = StreamCodec.ofMember(SetResearchTopicHistoryPacket::encode, SetResearchTopicHistoryPacket::decode);

    protected final AbstractResearchTopic<?> current;
    protected final List<AbstractResearchTopic<?>> history;
    
    public SetResearchTopicHistoryPacket(AbstractResearchTopic<?> current, List<AbstractResearchTopic<?>> history) {
        this.current = current;
        this.history = history;
    }
    
    public static void encode(SetResearchTopicHistoryPacket message, RegistryFriendlyByteBuf buf) {
        AbstractResearchTopic.dispatchStreamCodec().encode(buf, message.current);
        AbstractResearchTopic.dispatchStreamCodec().apply(ByteBufCodecs.list()).encode(buf, message.history);
    }
    
    public static SetResearchTopicHistoryPacket decode(RegistryFriendlyByteBuf buf) {
        return new SetResearchTopicHistoryPacket(
                AbstractResearchTopic.dispatchStreamCodec().decode(buf),
                AbstractResearchTopic.dispatchStreamCodec().apply(ByteBufCodecs.list()).decode(buf));
    }
    
    public static void onMessage(SetResearchTopicHistoryPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            knowledge.setLastResearchTopic(message.current);
            knowledge.setResearchTopicHistory(message.history);
        });
    }
}
