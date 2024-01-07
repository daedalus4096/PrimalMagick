package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.LinkedList;
import java.util.List;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicFactory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet to sync the last active grimoire research topic from the client to the server.
 * 
 * @author Daedalus4096
 */
public class SetResearchTopicHistoryPacket implements IMessageToServer {
    protected CompoundTag data;
    
    public SetResearchTopicHistoryPacket() {
        this.data = null;
    }
    
    public SetResearchTopicHistoryPacket(AbstractResearchTopic current, List<AbstractResearchTopic> history) {
        this.data = new CompoundTag();
        this.data.put("Current", current.serializeNBT());
        ListTag list = new ListTag();
        for (AbstractResearchTopic topic : history) {
            list.add(topic.serializeNBT());
        }
        this.data.put("History", list);
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(SetResearchTopicHistoryPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SetResearchTopicHistoryPacket decode(FriendlyByteBuf buf) {
        SetResearchTopicHistoryPacket message = new SetResearchTopicHistoryPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static void onMessage(SetResearchTopicHistoryPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            knowledge.setLastResearchTopic(ResearchTopicFactory.deserializeNBT(message.data.getCompound("Current")));
            List<AbstractResearchTopic> historyList = new LinkedList<>();
            ListTag historyTag = message.data.getList("History", Tag.TAG_COMPOUND);
            for (int index = 0; index < historyTag.size(); index++) {
                AbstractResearchTopic topic = ResearchTopicFactory.deserializeNBT(historyTag.getCompound(index));
                if (topic != null) {
                    historyList.add(topic);
                }
            }
            knowledge.setResearchTopicHistory(historyList);
        });
    }
}
