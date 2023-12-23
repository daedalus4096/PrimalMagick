package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

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
    
    public SeenArcaneRecipePacket(RecipeHolder<?> recipe) {
        this.recipeId = recipe.id();
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(SeenArcaneRecipePacket message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.recipeId);
    }
    
    public static SeenArcaneRecipePacket decode(FriendlyByteBuf buf) {
        SeenArcaneRecipePacket message = new SeenArcaneRecipePacket();
        message.recipeId = buf.readResourceLocation();
        return message;
    }
    
    public static void onMessage(SeenArcaneRecipePacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        player.getServer().getRecipeManager().byKey(message.recipeId).ifPresent(recipe -> {
            player.getRecipeBook().removeHighlight(recipe);
            PrimalMagickCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
                recipeBook.get().removeHighlight(recipe);
            });
        });
    }
}
