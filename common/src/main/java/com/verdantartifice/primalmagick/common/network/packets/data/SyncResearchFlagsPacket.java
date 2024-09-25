package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet to update research entry flag data on the server (e.g. when a user clicks an "updated" entry
 * in the grimoire, it should clear the flag).
 * 
 * @author Daedalus4096
 */
public class SyncResearchFlagsPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncResearchFlagsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncResearchFlagsPacket::encode, SyncResearchFlagsPacket::decode);

    protected final ResearchEntryKey key;
    protected final boolean isNew;
    protected final boolean isUpdated;
    protected final boolean isPopup;
    
    public SyncResearchFlagsPacket(Player player, ResearchEntryKey key) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElseThrow(() -> new IllegalArgumentException("No knowledge provider for player"));
        this.key = key;
        this.isNew = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.NEW);
        this.isUpdated = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.UPDATED);
        this.isPopup = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
    }
    
    protected SyncResearchFlagsPacket(ResearchEntryKey key, boolean isNew, boolean isUpdated, boolean isPopup) {
        this.key = key;
        this.isNew = isNew;
        this.isUpdated = isUpdated;
        this.isPopup = isPopup;
    }
    
    public static void encode(SyncResearchFlagsPacket message, RegistryFriendlyByteBuf buf) {
        message.key.toNetwork(buf);
        buf.writeBoolean(message.isNew);
        buf.writeBoolean(message.isUpdated);
        buf.writeBoolean(message.isPopup);
    }
    
    public static SyncResearchFlagsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncResearchFlagsPacket(ResearchEntryKey.fromNetwork(buf), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }
    
    public static void onMessage(SyncResearchFlagsPacket message, CustomPayloadEvent.Context ctx) {
        if (message.key != null) {
            Player player = ctx.getSender();
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                // Add or remove each flag from the research entry as appropriate
                if (message.isNew) {
                    knowledge.addResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.NEW);
                } else {
                    knowledge.removeResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.NEW);
                }
                if (message.isUpdated) {
                    knowledge.addResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.UPDATED);
                } else {
                    knowledge.removeResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.UPDATED);
                }
                if (message.isPopup) {
                    knowledge.addResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.POPUP);
                } else {
                    knowledge.removeResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.POPUP);
                }
            });
        }
    }
}
