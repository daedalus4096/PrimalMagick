package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Packet to update research entry flag data on the server (e.g. when a user clicks an "updated" entry
 * in the grimoire, it should clear the flag).
 * 
 * @author Daedalus4096
 */
public class SyncResearchFlagsPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_research_flags");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncResearchFlagsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncResearchFlagsPacket::encode, SyncResearchFlagsPacket::decode);

    protected final ResearchEntryKey key;
    protected final boolean isNew;
    protected final boolean isUpdated;
    protected final boolean isPopup;
    protected final boolean isHighlight;
    protected final boolean isRead;
    
    public SyncResearchFlagsPacket(Player player, ResearchEntryKey key) {
        IPlayerKnowledge knowledge = Services.CAPABILITIES.knowledge(player).orElseThrow(() -> new IllegalArgumentException("No knowledge provider for player"));
        this.key = key;
        this.isNew = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.NEW);
        this.isUpdated = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.UPDATED);
        this.isPopup = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
        this.isHighlight = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.HIGHLIGHT);
        this.isRead = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.READ);
    }
    
    protected SyncResearchFlagsPacket(ResearchEntryKey key, boolean isNew, boolean isUpdated, boolean isPopup, boolean isHighlight, boolean isRead) {
        this.key = key;
        this.isNew = isNew;
        this.isUpdated = isUpdated;
        this.isPopup = isPopup;
        this.isHighlight = isHighlight;
        this.isRead = isRead;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncResearchFlagsPacket message, RegistryFriendlyByteBuf buf) {
        message.key.toNetwork(buf);
        buf.writeBoolean(message.isNew);
        buf.writeBoolean(message.isUpdated);
        buf.writeBoolean(message.isPopup);
        buf.writeBoolean(message.isHighlight);
        buf.writeBoolean(message.isRead);
    }
    
    public static SyncResearchFlagsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncResearchFlagsPacket(ResearchEntryKey.fromNetwork(buf), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }
    
    public static void onMessage(PacketContext<SyncResearchFlagsPacket> ctx) {
        SyncResearchFlagsPacket message = ctx.message();
        if (message.key != null) {
            Player player = ctx.sender();
            Services.CAPABILITIES.knowledge(player).ifPresent(knowledge -> {
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
                if (message.isHighlight) {
                    knowledge.addResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.HIGHLIGHT);
                } else {
                    knowledge.removeResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.HIGHLIGHT);
                }
                if (message.isRead) {
                    knowledge.addResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.READ);
                } else {
                    knowledge.removeResearchFlag(message.key, IPlayerKnowledge.ResearchFlag.READ);
                }
            });
        }
    }
}
