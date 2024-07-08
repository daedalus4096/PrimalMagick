package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Packet sent from the server to play a sound event for the receiving client only.
 * 
 * @author Daedalus4096
 */
public class PlayClientSoundPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayClientSoundPacket> STREAM_CODEC = StreamCodec.ofMember(PlayClientSoundPacket::encode, PlayClientSoundPacket::decode);

    protected final ResourceLocation eventLoc;
    protected final float volume;
    protected final float pitch;
    
    public PlayClientSoundPacket(SoundEvent event, float volume, float pitch) {
        this(ForgeRegistries.SOUND_EVENTS.getKey(event), volume, pitch);
    }
    
    protected PlayClientSoundPacket(ResourceLocation eventLoc, float volume, float pitch) {
        this.eventLoc = eventLoc;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public static void encode(PlayClientSoundPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeResourceLocation(message.eventLoc);
        buf.writeFloat(message.volume);
        buf.writeFloat(message.pitch);
    }
    
    public static PlayClientSoundPacket decode(RegistryFriendlyByteBuf buf) {
        return new PlayClientSoundPacket(buf.readResourceLocation(), buf.readFloat(), buf.readFloat());
    }
    
    public static void onMessage(PlayClientSoundPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        if (message.eventLoc != null && ForgeRegistries.SOUND_EVENTS.containsKey(message.eventLoc)) {
            player.playSound(ForgeRegistries.SOUND_EVENTS.getValue(message.eventLoc), message.volume, message.pitch);
        }
    }
}
