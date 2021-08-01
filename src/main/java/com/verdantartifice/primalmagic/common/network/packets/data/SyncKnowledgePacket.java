package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.client.gui.ResearchToast;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagic.common.research.ResearchEntries;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet to sync knowledge capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncKnowledgePacket implements IMessageToClient {
    protected CompoundTag data;
    
    public SyncKnowledgePacket() {
        this.data = null;
    }
    
    public SyncKnowledgePacket(Player player) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        this.data = (knowledge != null) ?
                knowledge.serializeNBT() :
                null;
    }
    
    public static void encode(SyncKnowledgePacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncKnowledgePacket decode(FriendlyByteBuf buf) {
        SyncKnowledgePacket message = new SyncKnowledgePacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncKnowledgePacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
            	Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                if (knowledge != null) {
                    knowledge.deserializeNBT(message.data);
                    for (SimpleResearchKey key : knowledge.getResearchSet()) {
                        // Show a research completion toast for any research entries so flagged
                        if (knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP)) {
                            ResearchEntry entry = ResearchEntries.getEntry(key);
                            if (entry != null) {
                                Minecraft.getInstance().getToasts().addToast(new ResearchToast(entry));
                            }
                            knowledge.removeResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
                        }
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
