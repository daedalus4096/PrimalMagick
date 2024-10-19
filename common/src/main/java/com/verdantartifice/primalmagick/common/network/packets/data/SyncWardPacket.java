package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
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
 * Packet to sync ward capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncWardPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_ward");
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

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncWardPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncWardPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncWardPacket(buf.readNbt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(PacketContext<SyncWardPacket> ctx) {
        SyncWardPacket message = ctx.message();
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            PrimalMagickCapabilities.getWard(player).ifPresent(ward -> {
                ward.deserializeNBT(player.registryAccess(), message.data);
            });
        }
    }
}
