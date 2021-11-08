package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet sent to trigger a server-side scan of a given item stack.  Used by the arcanometer for
 * scanning item entities in the world.
 * 
 * @author Daedalus4096
 */
public class ScanItemPacket implements IMessageToServer {
    protected ItemStack stack;
    
    public ScanItemPacket() {
        this.stack = ItemStack.EMPTY;
    }
    
    public ScanItemPacket(ItemStack stack) {
        this.stack = stack;
    }
    
    public static void encode(ScanItemPacket message, FriendlyByteBuf buf) {
        buf.writeItem(message.stack);
    }
    
    public static ScanItemPacket decode(FriendlyByteBuf buf) {
        ScanItemPacket message = new ScanItemPacket();
        message.stack = buf.readItem();
        return message;
    }

    public static class Handler {
        public static void onMessage(ScanItemPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (message.stack != null && !message.stack.isEmpty()) {
                    ServerPlayer player = ctx.get().getSender();
                    if (ResearchManager.isScanned(message.stack, player)) {
                        player.displayClientMessage(new TranslatableComponent("event.primalmagic.scan.repeat").withStyle(ChatFormatting.RED), true);
                    } else if (ResearchManager.setScanned(message.stack, player)) {
                        player.displayClientMessage(new TranslatableComponent("event.primalmagic.scan.success").withStyle(ChatFormatting.GREEN), true);
                    } else {
                        player.displayClientMessage(new TranslatableComponent("event.primalmagic.scan.fail").withStyle(ChatFormatting.RED), true);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
