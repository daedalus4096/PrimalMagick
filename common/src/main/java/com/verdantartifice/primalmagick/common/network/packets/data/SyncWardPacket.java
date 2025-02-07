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
 * Packet to sync ward capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncWardPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_ward");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncWardPacket> STREAM_CODEC = StreamCodec.ofMember(SyncWardPacket::encode, SyncWardPacket::decode);

    protected final CompoundTag data;

    public SyncWardPacket(Player player) {
        this.data = Services.CAPABILITIES.ward(player).map(w -> w.serializeNBT(player.registryAccess())).orElse(null);
    }
    
    protected SyncWardPacket(CompoundTag data) {
        this.data = data;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncWardPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncWardPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncWardPacket(buf.readNbt());
    }
    
    public static void onMessage(PacketContext<SyncWardPacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.ward(player).ifPresent(ward -> ward.deserializeNBT(player.registryAccess(), ctx.message().data));
        }
    }
}
