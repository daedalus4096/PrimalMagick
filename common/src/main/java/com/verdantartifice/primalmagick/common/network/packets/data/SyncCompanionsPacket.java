package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
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
 * Packet to sync companion capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncCompanionsPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_companions");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCompanionsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncCompanionsPacket::encode, SyncCompanionsPacket::decode);

    protected final CompoundTag data;

    @SuppressWarnings("deprecation")
    public SyncCompanionsPacket(Player player) {
        IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
        this.data = (companions != null) ? companions.serializeNBT(player.registryAccess()) : null;
    }
    
    protected SyncCompanionsPacket(CompoundTag data) {
        this.data = data;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncCompanionsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncCompanionsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncCompanionsPacket(buf.readNbt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(PacketContext<SyncCompanionsPacket> ctx) {
        SyncCompanionsPacket message = ctx.message();
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
        if (companions != null) {
            companions.deserializeNBT(player.registryAccess(), message.data);
        }
    }
}
