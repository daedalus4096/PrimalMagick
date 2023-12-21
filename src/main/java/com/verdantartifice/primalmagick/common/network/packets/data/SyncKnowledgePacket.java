package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.toast.ToastManager;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;

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
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        this.data = (knowledge != null) ?
                knowledge.serializeNBT() :
                null;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SyncKnowledgePacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncKnowledgePacket decode(FriendlyByteBuf buf) {
        SyncKnowledgePacket message = new SyncKnowledgePacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static void onMessage(SyncKnowledgePacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            knowledge.deserializeNBT(message.data);
            for (SimpleResearchKey key : knowledge.getResearchSet()) {
                // Show a research completion toast for any research entries so flagged
                if (knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP)) {
                    ResearchEntry entry = ResearchEntries.getEntry(key);
                    if (entry != null && FMLEnvironment.dist == Dist.CLIENT) {
                        ToastManager.showResearchToast(entry, knowledge.isResearchComplete(entry.getKey()));
                    }
                    knowledge.removeResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
                }
            }
        });
    }
}
