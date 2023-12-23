package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet which informs a client to show a ghost arcane recipe in the GUI.
 * 
 * @author Daedalus4096
 */
public class PlaceGhostArcaneRecipePacket implements IMessageToClient {
    protected int containerId;
    protected ResourceLocation recipeId;
    
    public PlaceGhostArcaneRecipePacket() {
        this.containerId = -1;
        this.recipeId = null;
    }
    
    public PlaceGhostArcaneRecipePacket(int containerId, RecipeHolder<?> recipe) {
        this.containerId = containerId;
        this.recipeId = recipe.id();
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(PlaceGhostArcaneRecipePacket message, FriendlyByteBuf buf) {
        buf.writeByte(message.containerId);
        buf.writeResourceLocation(message.recipeId);
    }
    
    public static PlaceGhostArcaneRecipePacket decode(FriendlyByteBuf buf) {
        PlaceGhostArcaneRecipePacket message = new PlaceGhostArcaneRecipePacket();
        message.containerId = buf.readByte();
        message.recipeId = buf.readResourceLocation();
        return message;
    }
    
    public static void onMessage(PlaceGhostArcaneRecipePacket message, CustomPayloadEvent.Context ctx) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientUtils.handlePlaceGhostRecipe(message.containerId, message.recipeId);
        }
    }
}
