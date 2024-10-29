package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

/**
 * Packet to sync the last active grimoire research topic from the client to the server.
 * 
 * @author Daedalus4096
 */
public class SetResearchTopicHistoryPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("set_research_topic_history");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetResearchTopicHistoryPacket> STREAM_CODEC = StreamCodec.ofMember(SetResearchTopicHistoryPacket::encode, SetResearchTopicHistoryPacket::decode);

    protected final AbstractResearchTopic<?> current;
    protected final List<AbstractResearchTopic<?>> history;
    
    public SetResearchTopicHistoryPacket(AbstractResearchTopic<?> current, List<AbstractResearchTopic<?>> history) {
        this.current = current;
        this.history = history;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
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
    
    public static void onMessage(PacketContext<SetResearchTopicHistoryPacket> ctx) {
        SetResearchTopicHistoryPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        Services.CAPABILITIES.knowledge(player).ifPresent(knowledge -> {
            knowledge.setLastResearchTopic(message.current);
            knowledge.setResearchTopicHistory(message.history);
        });
    }
}
