package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.toast.ToastManager;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerKnowledge;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Packet to sync knowledge capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncKnowledgePacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_knowledge");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncKnowledgePacket> STREAM_CODEC = StreamCodec.composite(
            PlayerKnowledge.STREAM_CODEC, p -> p.packetKnowledge,
            SyncKnowledgePacket::new);

    protected final PlayerKnowledge packetKnowledge;
    
    public SyncKnowledgePacket(PlayerKnowledge knowledge) {
        this.packetKnowledge = knowledge;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<SyncKnowledgePacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.knowledge(player).ifPresent(knowledge -> {
                RegistryAccess registryAccess = player.level().registryAccess();
                if (knowledge instanceof PlayerKnowledge playerKnowledge) {
                    // FIXME The rest of this packet is implementation specific; how important is it to have this import respect the interface boundary?
                    playerKnowledge.copyFrom(ctx.message().packetKnowledge);
                }
                for (AbstractResearchKey<?> key : knowledge.getResearchSet()) {
                    // Show a research completion toast for any research entries so flagged
                    if (key instanceof ResearchEntryKey entryKey && knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP)) {
                        ResearchEntry entry = ResearchEntries.getEntry(registryAccess, entryKey);
                        if (entry != null && !entry.flags().internal() && Side.CLIENT.equals(ctx.side())) {
                            ToastManager.showResearchToast(entry, knowledge.isResearchComplete(registryAccess, entry.key()));
                        }
                        knowledge.removeResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
                    }
                }
            });
        }
    }
}
