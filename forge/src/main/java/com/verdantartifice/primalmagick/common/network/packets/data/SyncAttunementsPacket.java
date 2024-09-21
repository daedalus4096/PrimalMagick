package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
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
 * Packet to sync attunements capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncAttunementsPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAttunementsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncAttunementsPacket::encode, SyncAttunementsPacket::decode);

    protected final CompoundTag data;

    @SuppressWarnings("deprecation")
    public SyncAttunementsPacket(Player player) {
        IPlayerAttunements attunements = PrimalMagickCapabilities.getAttunements(player);
        this.data = (attunements != null) ? attunements.serializeNBT(player.registryAccess()) : null;
    }
    
    protected SyncAttunementsPacket(CompoundTag data) {
        this.data = data;
    }
    
    public static void encode(SyncAttunementsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncAttunementsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncAttunementsPacket(buf.readNbt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(SyncAttunementsPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerAttunements attunements = PrimalMagickCapabilities.getAttunements(player);
        if (attunements != null) {
            attunements.deserializeNBT(player.registryAccess(), message.data);
        }
    }
}
