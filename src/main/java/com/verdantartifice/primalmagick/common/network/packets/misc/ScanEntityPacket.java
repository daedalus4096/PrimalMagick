package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Packet sent to trigger a server-side scan of a given entity.  Used by the arcanometer for
 * scanning entities in the world.
 * 
 * @author Daedalus4096
 */
public class ScanEntityPacket implements IMessageToServer {
    protected EntityType<?> type;
    
    public ScanEntityPacket() {
        this.type = null;
    }
    
    public ScanEntityPacket(EntityType<?> type) {
        this.type = type;
    }
    
    public static void encode(ScanEntityPacket message, FriendlyByteBuf buf) {
        buf.writeUtf(message.type.getRegistryName().toString());
    }
    
    public static ScanEntityPacket decode(FriendlyByteBuf buf) {
        ScanEntityPacket message = new ScanEntityPacket();
        message.type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(buf.readUtf()));
        return message;
    }
    
    public static class Handler {
        public static void onMessage(ScanEntityPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (message.type != null) {
                    ServerPlayer player = ctx.get().getSender();
                    if (ResearchManager.isScanned(message.type, player)) {
                        player.displayClientMessage(new TranslatableComponent("event.primalmagick.scan.repeat").withStyle(ChatFormatting.RED), true);
                    } else if (ResearchManager.setScanned(message.type, player)) {
                        player.displayClientMessage(new TranslatableComponent("event.primalmagick.scan.success").withStyle(ChatFormatting.GREEN), true);
                    } else {
                        player.displayClientMessage(new TranslatableComponent("event.primalmagick.scan.fail").withStyle(ChatFormatting.RED), true);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
