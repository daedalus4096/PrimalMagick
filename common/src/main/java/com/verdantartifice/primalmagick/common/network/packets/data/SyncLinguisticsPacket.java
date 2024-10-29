package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Packet to sync linguistics capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncLinguisticsPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_linguistics");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncLinguisticsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncLinguisticsPacket::encode, SyncLinguisticsPacket::decode);

    protected final CompoundTag data;

    public SyncLinguisticsPacket(Player player) {
        this.data = Services.CAPABILITIES.linguistics(player).map(l -> l.serializeNBT(player.registryAccess())).orElse(null);
    }
    
    protected SyncLinguisticsPacket(CompoundTag data) {
        this.data = data;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncLinguisticsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncLinguisticsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncLinguisticsPacket(buf.readNbt());
    }
    
    public static void onMessage(PacketContext<SyncLinguisticsPacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.linguistics(player).ifPresent(l -> l.deserializeNBT(player.registryAccess(), ctx.message().data));
        }
    }
}
