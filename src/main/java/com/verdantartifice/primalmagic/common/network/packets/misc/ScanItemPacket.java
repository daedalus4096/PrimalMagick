package com.verdantartifice.primalmagic.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

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
    
    public static void encode(ScanItemPacket message, PacketBuffer buf) {
        buf.writeItemStack(message.stack);
    }
    
    public static ScanItemPacket decode(PacketBuffer buf) {
        ScanItemPacket message = new ScanItemPacket();
        message.stack = buf.readItemStack();
        return message;
    }

    public static class Handler {
        public static void onMessage(ScanItemPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (message.stack != null && !message.stack.isEmpty()) {
                    ServerPlayerEntity player = ctx.get().getSender();
                    if (AffinityManager.isScanned(message.stack, player)) {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.repeat").mergeStyle(TextFormatting.RED), true);
                    } else if (AffinityManager.setScanned(message.stack, player)) {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.success").mergeStyle(TextFormatting.GREEN), true);
                    } else {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.fail").mergeStyle(TextFormatting.RED), true);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
