package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
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
 * Packet to sync attunements capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncAttunementsPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_attunements");
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

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncAttunementsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncAttunementsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncAttunementsPacket(buf.readNbt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(PacketContext<SyncAttunementsPacket> ctx) {
        SyncAttunementsPacket message = ctx.message();
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerAttunements attunements = PrimalMagickCapabilities.getAttunements(player);
        if (attunements != null) {
            attunements.deserializeNBT(player.registryAccess(), message.data);
        }
    }
}
