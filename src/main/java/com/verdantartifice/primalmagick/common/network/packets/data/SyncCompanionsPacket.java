package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
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
 * Packet to sync companion capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncCompanionsPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCompanionsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncCompanionsPacket::encode, SyncCompanionsPacket::decode);

    protected final CompoundTag data;

    public SyncCompanionsPacket(Player player) {
        IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
        this.data = (companions != null) ? companions.serializeNBT(player.registryAccess()) : null;
    }
    
    protected SyncCompanionsPacket(CompoundTag data) {
        this.data = data;
    }
    
    public static void encode(SyncCompanionsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncCompanionsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncCompanionsPacket(buf.readNbt());
    }
    
    public static void onMessage(SyncCompanionsPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
        if (companions != null) {
            companions.deserializeNBT(player.registryAccess(), message.data);
        }
    }
}
