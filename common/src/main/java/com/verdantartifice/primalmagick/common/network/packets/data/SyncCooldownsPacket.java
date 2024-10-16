package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
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
 * Packet to sync cooldown capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncCooldownsPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_cooldowns");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCooldownsPacket> STREAM_CODEC = StreamCodec.ofMember(SyncCooldownsPacket::encode, SyncCooldownsPacket::decode);

    protected final CompoundTag data;

    @SuppressWarnings("deprecation")
    public SyncCooldownsPacket(Player player) {
        IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
        this.data = (cooldowns != null) ? cooldowns.serializeNBT(player.registryAccess()) : null;
    }
    
    protected SyncCooldownsPacket(CompoundTag data) {
        this.data = data;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncCooldownsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncCooldownsPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncCooldownsPacket(buf.readNbt());
    }
    
    @SuppressWarnings("deprecation")
    public static void onMessage(PacketContext<SyncCooldownsPacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
        if (cooldowns != null) {
            cooldowns.deserializeNBT(player.registryAccess(), ctx.message().data);
        }
    }
}
