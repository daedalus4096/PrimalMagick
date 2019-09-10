package com.verdantartifice.primalmagic.common.network;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncKnowledgePacket;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncProgressPacket;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    
    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(PrimalMagic.MODID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    
    public static void registerMessages() {
        int disc = 0;
        
        INSTANCE.registerMessage(disc++, SyncKnowledgePacket.class, SyncKnowledgePacket::encode, SyncKnowledgePacket::decode, SyncKnowledgePacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, SyncProgressPacket.class, SyncProgressPacket::encode, SyncProgressPacket::decode, SyncProgressPacket.Handler::onMessage);
    }
    
    public static void sendToServer(IMessageToServer message) {
        INSTANCE.sendToServer(message);
    }
    
    public static void sendToPlayer(IMessageToClient message, ServerPlayerEntity player) {
        INSTANCE.sendTo(message, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
