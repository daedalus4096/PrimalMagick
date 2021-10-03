package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.client.util.ClientUtils;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet to sync arcane recipe book capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncArcaneRecipeBookPacket implements IMessageToClient {
    protected CompoundTag data;
    
    public SyncArcaneRecipeBookPacket() {
        this.data = null;
    }
    
    public SyncArcaneRecipeBookPacket(Player player) {
        PrimalMagicCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
            this.data = recipeBook.serializeNBT();
        });
    }
    
    public static void encode(SyncArcaneRecipeBookPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncArcaneRecipeBookPacket decode(FriendlyByteBuf buf) {
        SyncArcaneRecipeBookPacket message = new SyncArcaneRecipeBookPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncArcaneRecipeBookPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
                if (player != null) {
                    PrimalMagicCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
                        recipeBook.deserializeNBT(message.data, player.level.getRecipeManager());
                    });
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
