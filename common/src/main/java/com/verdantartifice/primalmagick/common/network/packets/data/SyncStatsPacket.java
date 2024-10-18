package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Packet to sync statistics capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncStatsPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_stats");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncStatsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncStatsPacket::encode, SyncStatsPacket::decode);

    protected final CompoundTag data;

    @SuppressWarnings("deprecation")
    public SyncStatsPacket(Player player) {
        IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
        this.data = (stats != null) ? stats.serializeNBT(player.registryAccess()) : null;
    }
    
    protected SyncStatsPacket(CompoundTag data) {
        this.data = data;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncStatsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncStatsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncStatsPacket(buf.readNbt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(PacketContext<SyncStatsPacket> ctx) {
        SyncStatsPacket message = ctx.message();
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
        if (stats != null) {
            stats.deserializeNBT(player.registryAccess(), message.data);
        }
    }
}
