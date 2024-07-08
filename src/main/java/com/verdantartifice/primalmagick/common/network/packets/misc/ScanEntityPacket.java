package com.verdantartifice.primalmagick.common.network.packets.misc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Packet sent to trigger a server-side scan of a given entity.  Used by the arcanometer for
 * scanning entities in the world.
 * 
 * @author Daedalus4096
 */
public class ScanEntityPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, ScanEntityPacket> STREAM_CODEC = StreamCodec.ofMember(ScanEntityPacket::encode, ScanEntityPacket::decode);

    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final EntityType<?> type;
    
    public ScanEntityPacket(EntityType<?> type) {
        this.type = type;
    }
    
    protected ScanEntityPacket(ResourceLocation typeLoc) {
        this.type = ForgeRegistries.ENTITY_TYPES.getValue(typeLoc);
    }
    
    public static void encode(ScanEntityPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(message.type));
    }
    
    public static ScanEntityPacket decode(RegistryFriendlyByteBuf buf) {
        return new ScanEntityPacket(buf.readResourceLocation());
    }
    
    public static void onMessage(ScanEntityPacket message, CustomPayloadEvent.Context ctx) {
        if (message.type != null) {
            ServerPlayer player = ctx.getSender();
            ResearchManager.isScannedAsync(message.type, player).thenAccept(isScanned -> {
                if (isScanned) {
                    player.displayClientMessage(Component.translatable("event.primalmagick.scan.repeat").withStyle(ChatFormatting.RED), true);
                } else if (ResearchManager.setScanned(message.type, player)) {
                    player.displayClientMessage(Component.translatable("event.primalmagick.scan.success").withStyle(ChatFormatting.GREEN), true);
                } else {
                    player.displayClientMessage(Component.translatable("event.primalmagick.scan.fail").withStyle(ChatFormatting.RED), true);
                }
            }).exceptionally(e -> {
                LOGGER.error("Failed to scan entity type " + message.type.toString(), e);
                return null;
            });
        }
    }
}
