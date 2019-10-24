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

public class ScanPacket implements IMessageToServer {
    protected ItemStack stack;
    
    public ScanPacket() {
        this.stack = ItemStack.EMPTY;
    }
    
    public ScanPacket(ItemStack stack) {
        this.stack = stack;
    }
    
    public static void encode(ScanPacket message, PacketBuffer buf) {
        buf.writeItemStack(message.stack);
    }
    
    public static ScanPacket decode(PacketBuffer buf) {
        ScanPacket message = new ScanPacket();
        message.stack = buf.readItemStack();
        return message;
    }

    public static class Handler {
        public static void onMessage(ScanPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if (message.stack != null && !message.stack.isEmpty()) {
                    ServerPlayerEntity player = ctx.get().getSender();
                    if (AffinityManager.isScanned(message.stack, player)) {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.repeat").applyTextStyle(TextFormatting.RED), true);
                    } else if (AffinityManager.setScanned(message.stack, player)) {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.success").applyTextStyle(TextFormatting.GREEN), true);
                    } else {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.fail").applyTextStyle(TextFormatting.RED), true);
                    }
                }
            });
        }
    }
}
