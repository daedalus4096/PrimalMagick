package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncKnowledgePacket implements IMessage {
    protected CompoundNBT data;
    
    public SyncKnowledgePacket() {
        this.data = null;
    }
    
    public SyncKnowledgePacket(PlayerEntity player) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        this.data = (knowledge != null) ?
                knowledge.serializeNBT() :
                null;
    }
    
    public static void encode(SyncKnowledgePacket message, PacketBuffer buf) {
        buf.writeCompoundTag(message.data);
    }
    
    public static SyncKnowledgePacket decode(PacketBuffer buf) {
        SyncKnowledgePacket message = new SyncKnowledgePacket();
        message.data = buf.readCompoundTag();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncKnowledgePacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                PlayerEntity player = Minecraft.getInstance().player;
                IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                if (knowledge != null) {
                    knowledge.deserializeNBT(message.data);
                }
            });
        }
    }
}
