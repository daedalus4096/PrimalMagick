package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet that informs the server that a given recipe has been seen by the player.
 * 
 * @author Daedalus4096
 */
public class SeenArcaneRecipePacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, SeenArcaneRecipePacket> STREAM_CODEC = StreamCodec.ofMember(SeenArcaneRecipePacket::encode, SeenArcaneRecipePacket::decode);

    protected final ResourceLocation recipeId;
    
    public SeenArcaneRecipePacket(RecipeHolder<?> recipe) {
        this(recipe.id());
    }
    
    public SeenArcaneRecipePacket(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }
    
    public static void encode(SeenArcaneRecipePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeResourceLocation(message.recipeId);
    }
    
    public static SeenArcaneRecipePacket decode(RegistryFriendlyByteBuf buf) {
        return new SeenArcaneRecipePacket(buf.readResourceLocation());
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
