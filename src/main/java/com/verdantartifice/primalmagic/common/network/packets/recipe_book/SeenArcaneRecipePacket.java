package com.verdantartifice.primalmagic.common.network.packets.recipe_book;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet that informs the server that a given recipe has been seen by the player.
 * 
 * @author Daedalus4096
 */
public class SeenArcaneRecipePacket implements IMessageToServer {
    protected ResourceLocation recipeId;
    
    public SeenArcaneRecipePacket() {
        this.recipeId = null;
    }
    
    public SeenArcaneRecipePacket(Recipe<?> recipe) {
        this.recipeId = recipe.getId();
    }
    
    public static void encode(SeenArcaneRecipePacket message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.recipeId);
    }
    
    public static SeenArcaneRecipePacket decode(FriendlyByteBuf buf) {
        SeenArcaneRecipePacket message = new SeenArcaneRecipePacket();
        message.recipeId = buf.readResourceLocation();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SeenArcaneRecipePacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                player.getServer().getRecipeManager().byKey(message.recipeId).ifPresent(recipe -> {
                    player.getRecipeBook().removeHighlight(recipe);
                    PrimalMagicCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
                        recipeBook.get().removeHighlight(recipe);
                    });
                });
            });

            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
