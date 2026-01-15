package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

/**
 * Packet sent from the server to play a sound event for the receiving client only.
 * 
 * @author Daedalus4096
 */
public class PlayClientSoundPacket implements IMessageToClient {
    public static final Identifier CHANNEL = ResourceUtils.loc("play_client_sound");
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayClientSoundPacket> STREAM_CODEC = StreamCodec.ofMember(PlayClientSoundPacket::encode, PlayClientSoundPacket::decode);

    protected final Identifier eventLoc;
    protected final float volume;
    protected final float pitch;
    
    public PlayClientSoundPacket(SoundEvent event, float volume, float pitch) {
        this(Services.SOUND_EVENTS_REGISTRY.getKey(event), volume, pitch);
    }
    
    protected PlayClientSoundPacket(Identifier eventLoc, float volume, float pitch) {
        this.eventLoc = eventLoc;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(PlayClientSoundPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeIdentifier(message.eventLoc);
        buf.writeFloat(message.volume);
        buf.writeFloat(message.pitch);
    }
    
    public static PlayClientSoundPacket decode(RegistryFriendlyByteBuf buf) {
        return new PlayClientSoundPacket(buf.readIdentifier(), buf.readFloat(), buf.readFloat());
    }
    
    public static void onMessage(PacketContext<PlayClientSoundPacket> ctx) {
        PlayClientSoundPacket message = ctx.message();
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null && message.eventLoc != null) {
            SoundEvent soundEvent = Services.SOUND_EVENTS_REGISTRY.get(message.eventLoc);
            if (soundEvent != null) {
                player.playSound(soundEvent, message.volume, message.pitch);
            }
        }
    }
}
