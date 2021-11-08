package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet to update research entry flag data on the server (e.g. when a user clicks an "updated" entry
 * in the grimoire, it should clear the flag).
 * 
 * @author Daedalus4096
 */
public class SyncResearchFlagsPacket implements IMessageToServer {
    protected SimpleResearchKey key;
    protected boolean isNew;
    protected boolean isUpdated;
    protected boolean isPopup;
    
    public SyncResearchFlagsPacket() {
        this.key = null;
        this.isNew = false;
        this.isUpdated = false;
        this.isPopup = false;
    }
    
    public SyncResearchFlagsPacket(Player player, SimpleResearchKey key) {
        this();
        this.key = key;
        PrimalMagicCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            this.isNew = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.NEW);
            this.isUpdated = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.UPDATED);
            this.isPopup = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
        });
    }
    
    public static void encode(SyncResearchFlagsPacket message, FriendlyByteBuf buf) {
        buf.writeUtf(message.key.getRootKey());
        buf.writeBoolean(message.isNew);
        buf.writeBoolean(message.isUpdated);
        buf.writeBoolean(message.isPopup);
    }
    
    public static SyncResearchFlagsPacket decode(FriendlyByteBuf buf) {
        SyncResearchFlagsPacket message = new SyncResearchFlagsPacket();
        message.key = SimpleResearchKey.parse(buf.readUtf());
        message.isNew = buf.readBoolean();
        message.isUpdated = buf.readBoolean();
        message.isPopup = buf.readBoolean();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncResearchFlagsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (message.key != null) {
                    Player player = ctx.get().getSender();
                    PrimalMagicCapabilities.getKnowledge(player).ifPresent(knowledge -> {
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
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
