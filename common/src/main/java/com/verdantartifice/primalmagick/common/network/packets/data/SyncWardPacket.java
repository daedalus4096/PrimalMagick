package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet to sync ward capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncWardPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncWardPacket> STREAM_CODEC = StreamCodec.ofMember(SyncWardPacket::encode, SyncWardPacket::decode);

    protected final CompoundTag data;

    @SuppressWarnings("deprecation")
    public SyncWardPacket(Player player) {
        if (PrimalMagickCapabilities.getWard(player).isPresent()) {
            this.data = PrimalMagickCapabilities.getWard(player).resolve().get().serializeNBT(player.registryAccess());
        } else {
            this.data = null;
        }
    }
    
    protected SyncWardPacket(CompoundTag data) {
        this.data = data;
    }
    
    public static void encode(SyncWardPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncWardPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncWardPacket(buf.readNbt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(SyncWardPacket message, CustomPayloadEvent.Context ctx) {
        Player player = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            PrimalMagickCapabilities.getWard(player).ifPresent(ward -> {
                ward.deserializeNBT(player.registryAccess(), message.data);
            });
        }
    }
}
