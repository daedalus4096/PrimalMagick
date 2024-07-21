package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.toast.ToastManager;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet to sync knowledge capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncKnowledgePacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncKnowledgePacket> STREAM_CODEC = StreamCodec.ofMember(SyncKnowledgePacket::encode, SyncKnowledgePacket::decode);

    protected final CompoundTag data;
    
    @SuppressWarnings("deprecation")
    public SyncKnowledgePacket(Player player) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElseThrow(() -> new IllegalArgumentException("No knowledge provider for player"));
        this.data = knowledge.serializeNBT(player.registryAccess());
    }
    
    protected SyncKnowledgePacket(CompoundTag data) {
        this.data = data;
    }
    
    public static void encode(SyncKnowledgePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncKnowledgePacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncKnowledgePacket(buf.readNbt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(SyncKnowledgePacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            RegistryAccess registryAccess = player.level().registryAccess();
            knowledge.deserializeNBT(player.registryAccess(), message.data);
            for (AbstractResearchKey<?> key : knowledge.getResearchSet()) {
                // Show a research completion toast for any research entries so flagged
                if (key instanceof ResearchEntryKey entryKey && knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP)) {
                    ResearchEntry entry = ResearchEntries.getEntry(registryAccess, entryKey);
                    if (entry != null && !entry.flags().internal() && FMLEnvironment.dist == Dist.CLIENT) {
                        ToastManager.showResearchToast(entry, knowledge.isResearchComplete(registryAccess, entry.key()));
                    }
                    knowledge.removeResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
                }
            }
        });
    }
}
