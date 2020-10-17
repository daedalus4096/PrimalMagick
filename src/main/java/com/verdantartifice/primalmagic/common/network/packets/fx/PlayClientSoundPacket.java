package com.verdantartifice.primalmagic.common.network.packets.fx;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Packet sent from the server to play a sound event for the receiving client only.
 * 
 * @author Daedalus4096
 */
public class PlayClientSoundPacket implements IMessageToClient {
    protected String eventLoc;
    protected float volume;
    protected float pitch;
    
    public PlayClientSoundPacket() {
        this.eventLoc = "";
        this.volume = 0.0F;
        this.pitch = 0.0F;
    }
    
    public PlayClientSoundPacket(SoundEvent event, float volume, float pitch) {
        this.eventLoc = event.getRegistryName().toString();
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public static void encode(PlayClientSoundPacket message, PacketBuffer buf) {
        buf.writeString(message.eventLoc);
        buf.writeFloat(message.volume);
        buf.writeFloat(message.pitch);
    }
    
    public static PlayClientSoundPacket decode(PacketBuffer buf) {
        PlayClientSoundPacket message = new PlayClientSoundPacket();
        message.eventLoc = buf.readString();
        message.volume = buf.readFloat();
        message.pitch = buf.readFloat();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(PlayClientSoundPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
        	Minecraft mc = Minecraft.getInstance();
            ctx.get().enqueueWork(() -> {
                ResourceLocation eventLoc = ResourceLocation.tryCreate(message.eventLoc);
                if (eventLoc != null && ForgeRegistries.SOUND_EVENTS.containsKey(eventLoc)) {
                    mc.player.playSound(ForgeRegistries.SOUND_EVENTS.getValue(eventLoc), message.volume, message.pitch);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
