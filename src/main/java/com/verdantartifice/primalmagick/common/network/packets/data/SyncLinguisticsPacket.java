package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet to sync linguistics capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncLinguisticsPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncLinguisticsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncLinguisticsPacket::encode, SyncLinguisticsPacket::decode);

    protected final CompoundTag data;

    public SyncLinguisticsPacket(Player player) {
        IPlayerLinguistics linguistics = PrimalMagickCapabilities.getLinguistics(player).orElse(null);
        this.data = (linguistics != null) ? linguistics.serializeNBT(player.registryAccess()) : null;
    }
    
    protected SyncLinguisticsPacket(CompoundTag data) {
        this.data = data;
    }
    
    public static void encode(SyncLinguisticsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncLinguisticsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncLinguisticsPacket(buf.readNbt());
    }
    
    public static void onMessage(SyncLinguisticsPacket message, CustomPayloadEvent.Context ctx) {
        Player player = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentPlayer() : null;
        PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
            linguistics.deserializeNBT(player.registryAccess(), message.data);
        });
    }
}
