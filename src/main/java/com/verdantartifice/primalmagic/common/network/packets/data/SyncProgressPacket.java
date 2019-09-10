package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncProgressPacket implements IMessageToServer {
    protected SimpleResearchKey key;
    
    public SyncProgressPacket() {
        this.key = null;
    }
    
    public SyncProgressPacket(SimpleResearchKey key) {
        this.key = key;
    }
    
    public static void encode(SyncProgressPacket message, PacketBuffer buf) {
        buf.writeString(message.key.getRootKey());
    }
    
    public static SyncProgressPacket decode(PacketBuffer buf) {
        SyncProgressPacket message = new SyncProgressPacket();
        message.key = SimpleResearchKey.parse(buf.readString());
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncProgressPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if (message.key != null) {
                    PlayerEntity player = ctx.get().getSender();
                    PrimalMagic.LOGGER.info("Progressing research {} for player {}", message.key.getRootKey(), player.getName().getString());
                    ResearchManager.progressResearch(player, message.key);
                }
            });
        }
    }
}
