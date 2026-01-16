package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.PlayerCooldowns;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * Packet to sync cooldown capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncCooldownsPacket implements IMessageToClient {
    public static final Identifier CHANNEL = ResourceUtils.loc("sync_cooldowns");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCooldownsPacket> STREAM_CODEC = StreamCodec.composite(
            PlayerCooldowns.STREAM_CODEC, p -> p.packetCooldowns,
            SyncCooldownsPacket::new);

    protected final PlayerCooldowns packetCooldowns;

    public SyncCooldownsPacket(PlayerCooldowns cooldowns) {
        this.packetCooldowns = cooldowns;
    }
    
    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<SyncCooldownsPacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.cooldowns(player).ifPresent(cooldowns -> {
                if (cooldowns instanceof PlayerCooldowns playerCooldowns) {
                    playerCooldowns.copyFrom(ctx.message().packetCooldowns);
                }
            });
        }
    }
}
