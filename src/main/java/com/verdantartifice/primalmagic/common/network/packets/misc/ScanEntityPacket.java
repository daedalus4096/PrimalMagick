package com.verdantartifice.primalmagic.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.research.ResearchManager;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
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
    
    public static void encode(ScanEntityPacket message, PacketBuffer buf) {
        buf.writeString(message.type.getRegistryName().toString());
    }
    
    public static ScanEntityPacket decode(PacketBuffer buf) {
        ScanEntityPacket message = new ScanEntityPacket();
        message.type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(buf.readString()));
        return message;
    }
    
    public static class Handler {
        public static void onMessage(ScanEntityPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (message.type != null) {
                    ServerPlayerEntity player = ctx.get().getSender();
                    if (ResearchManager.isScanned(message.type, player)) {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.repeat").mergeStyle(TextFormatting.RED), true);
                    } else if (ResearchManager.setScanned(message.type, player)) {
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
