package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
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
        this.eventLoc = ForgeRegistries.SOUND_EVENTS.getKey(event).toString();
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(PlayClientSoundPacket message, FriendlyByteBuf buf) {
        buf.writeUtf(message.eventLoc);
        buf.writeFloat(message.volume);
        buf.writeFloat(message.pitch);
    }
    
    public static PlayClientSoundPacket decode(FriendlyByteBuf buf) {
        PlayClientSoundPacket message = new PlayClientSoundPacket();
        message.eventLoc = buf.readUtf();
        message.volume = buf.readFloat();
        message.pitch = buf.readFloat();
        return message;
    }
    
    public static void onMessage(PlayClientSoundPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        ResourceLocation eventLoc = ResourceLocation.tryParse(message.eventLoc);
        if (eventLoc != null && ForgeRegistries.SOUND_EVENTS.containsKey(eventLoc)) {
            player.playSound(ForgeRegistries.SOUND_EVENTS.getValue(eventLoc), message.volume, message.pitch);
        }
    }
}
