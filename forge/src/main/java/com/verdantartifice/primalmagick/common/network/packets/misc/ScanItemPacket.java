package com.verdantartifice.primalmagick.common.network.packets.misc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to trigger a server-side scan of a given item stack.  Used by the arcanometer for
 * scanning item entities in the world.
 * 
 * @author Daedalus4096
 */
public class ScanItemPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, ScanItemPacket> STREAM_CODEC = StreamCodec.ofMember(ScanItemPacket::encode, ScanItemPacket::decode);

    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final ItemStack stack;
    
    public ScanItemPacket(ItemStack stack) {
        this.stack = stack;
    }
    
    public static void encode(ScanItemPacket message, RegistryFriendlyByteBuf buf) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, message.stack);
    }
    
    public static ScanItemPacket decode(RegistryFriendlyByteBuf buf) {
        return new ScanItemPacket(ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
    }
    
    public static void onMessage(ScanItemPacket message, CustomPayloadEvent.Context ctx) {
        if (message.stack != null && !message.stack.isEmpty()) {
            ServerPlayer player = ctx.getSender();
            ResearchManager.isScannedAsync(message.stack, player).thenAccept(isScanned -> {
                if (isScanned) {
                    player.displayClientMessage(Component.translatable("event.primalmagick.scan.repeat").withStyle(ChatFormatting.RED), true);
                } else if (ResearchManager.setScanned(message.stack, player)) {
                    player.displayClientMessage(Component.translatable("event.primalmagick.scan.success").withStyle(ChatFormatting.GREEN), true);
                } else {
                    player.displayClientMessage(Component.translatable("event.primalmagick.scan.fail").withStyle(ChatFormatting.RED), true);
                }
            }).exceptionally(e -> {
                LOGGER.error("Failed to scan item stack " + message.stack.toString(), e);
                return null;
            });
        }
    }
}
