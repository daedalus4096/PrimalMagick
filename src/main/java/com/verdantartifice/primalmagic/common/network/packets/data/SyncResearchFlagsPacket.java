package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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
    
    public SyncResearchFlagsPacket(PlayerEntity player, SimpleResearchKey key) {
        this();
        this.key = key;
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge != null) {
            this.isNew = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.NEW);
            this.isUpdated = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.UPDATED);
            this.isPopup = knowledge.hasResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
        }
    }
    
    public static void encode(SyncResearchFlagsPacket message, PacketBuffer buf) {
        buf.writeString(message.key.getRootKey());
        buf.writeBoolean(message.isNew);
        buf.writeBoolean(message.isUpdated);
        buf.writeBoolean(message.isPopup);
    }
    
    public static SyncResearchFlagsPacket decode(PacketBuffer buf) {
        SyncResearchFlagsPacket message = new SyncResearchFlagsPacket();
        message.key = SimpleResearchKey.parse(buf.readString());
        message.isNew = buf.readBoolean();
        message.isUpdated = buf.readBoolean();
        message.isPopup = buf.readBoolean();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncResearchFlagsPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if (message.key != null) {
                    PlayerEntity player = ctx.get().getSender();
                    IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                    if (knowledge != null) {
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
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
