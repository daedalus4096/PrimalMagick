package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet to sync statistics capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncStatsPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncStatsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncStatsPacket::encode, SyncStatsPacket::decode);

    protected final CompoundTag data;

    public SyncStatsPacket(Player player) {
        IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
        this.data = (stats != null) ? stats.serializeNBT(player.registryAccess()) : null;
    }
    
    protected SyncStatsPacket(CompoundTag data) {
        this.data = data;
    }
    
    public static void encode(SyncStatsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncStatsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncStatsPacket(buf.readNbt());
    }
    
    public static void onMessage(SyncStatsPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
        if (stats != null) {
            stats.deserializeNBT(player.registryAccess(), message.data);
        }
    }
}
