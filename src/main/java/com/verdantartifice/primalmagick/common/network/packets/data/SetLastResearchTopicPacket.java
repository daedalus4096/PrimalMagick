package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicFactory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet to sync the last active grimoire research topic from the client to the server.
 * 
 * @author Daedalus4096
 */
public class SetLastResearchTopicPacket implements IMessageToServer {
    protected CompoundTag data;
    
    public SetLastResearchTopicPacket() {
        this.data = null;
    }
    
    public SetLastResearchTopicPacket(AbstractResearchTopic topic) {
        this.data = topic.serializeNBT();
    }
    
    public static void encode(SetLastResearchTopicPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SetLastResearchTopicPacket decode(FriendlyByteBuf buf) {
        SetLastResearchTopicPacket message = new SetLastResearchTopicPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SetLastResearchTopicPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                    knowledge.setLastResearchTopic(ResearchTopicFactory.deserializeNBT(message.data));
                });
            });

            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
