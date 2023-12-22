package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.recipe_book.ArcaneSearchRegistry;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
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
        PrimalMagickCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
            this.data = recipeBook.serializeNBT();
        });
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SyncArcaneRecipeBookPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncArcaneRecipeBookPacket decode(FriendlyByteBuf buf) {
        SyncArcaneRecipeBookPacket message = new SyncArcaneRecipeBookPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static void onMessage(SyncArcaneRecipeBookPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            PrimalMagickCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
                recipeBook.deserializeNBT(message.data, player.level().getRecipeManager());
            });
            ArcaneSearchRegistry.populate();
        }
    }
}
