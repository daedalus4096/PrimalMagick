package com.verdantartifice.primalmagick.common.network.packets.misc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent to trigger a server-side scan of a given item stack.  Used by the arcanometer for
 * scanning item entities in the world.
 * 
 * @author Daedalus4096
 */
public class ScanItemPacket implements IMessageToServer {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected ItemStack stack;
    
    public ScanItemPacket() {
        this.stack = ItemStack.EMPTY;
    }
    
    public ScanItemPacket(ItemStack stack) {
        this.stack = stack;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(ScanItemPacket message, FriendlyByteBuf buf) {
        buf.writeItem(message.stack);
    }
    
    public static ScanItemPacket decode(FriendlyByteBuf buf) {
        ScanItemPacket message = new ScanItemPacket();
        message.stack = buf.readItem();
        return message;
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
