package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncCooldownsPacket implements IMessageToClient {
    protected CompoundNBT data;

    public SyncCooldownsPacket() {
        this.data = null;
    }
    
    public SyncCooldownsPacket(PlayerEntity player) {
        IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
        this.data = (cooldowns != null) ? cooldowns.serializeNBT() : null;
    }
    
    public static void encode(SyncCooldownsPacket message, PacketBuffer buf) {
        buf.writeCompoundTag(message.data);
    }
    
    public static SyncCooldownsPacket decode(PacketBuffer buf) {
        SyncCooldownsPacket message = new SyncCooldownsPacket();
        message.data = buf.readCompoundTag();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncCooldownsPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                PlayerEntity player = Minecraft.getInstance().player;
                IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
                if (cooldowns != null) {
                    cooldowns.deserializeNBT(message.data);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
