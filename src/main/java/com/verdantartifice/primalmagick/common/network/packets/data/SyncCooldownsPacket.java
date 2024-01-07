package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet to sync cooldown capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncCooldownsPacket implements IMessageToClient {
    protected CompoundTag data;

    public SyncCooldownsPacket() {
        this.data = null;
    }
    
    public SyncCooldownsPacket(Player player) {
        IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
        this.data = (cooldowns != null) ? cooldowns.serializeNBT() : null;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SyncCooldownsPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncCooldownsPacket decode(FriendlyByteBuf buf) {
        SyncCooldownsPacket message = new SyncCooldownsPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static void onMessage(SyncCooldownsPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
        if (cooldowns != null) {
            cooldowns.deserializeNBT(message.data);
        }
    }
}
