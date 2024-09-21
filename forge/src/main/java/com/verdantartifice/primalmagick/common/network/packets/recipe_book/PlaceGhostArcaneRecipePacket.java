package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet which informs a client to show a ghost arcane recipe in the GUI.
 * 
 * @author Daedalus4096
 */
public class PlaceGhostArcaneRecipePacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, PlaceGhostArcaneRecipePacket> STREAM_CODEC = StreamCodec.ofMember(PlaceGhostArcaneRecipePacket::encode, PlaceGhostArcaneRecipePacket::decode);

    protected final int containerId;
    protected final ResourceLocation recipeId;
    
    public PlaceGhostArcaneRecipePacket(int containerId, RecipeHolder<?> recipe) {
        this(containerId, recipe.id());
    }
    
    protected PlaceGhostArcaneRecipePacket(int containerId, ResourceLocation recipeId) {
        this.containerId = containerId;
        this.recipeId = recipeId;
    }
    
    public static void encode(PlaceGhostArcaneRecipePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.containerId);
        buf.writeResourceLocation(message.recipeId);
    }
    
    public static PlaceGhostArcaneRecipePacket decode(RegistryFriendlyByteBuf buf) {
        return new PlaceGhostArcaneRecipePacket(buf.readVarInt(), buf.readResourceLocation());
    }
    
    public static void onMessage(PlaceGhostArcaneRecipePacket message, CustomPayloadEvent.Context ctx) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientUtils.handlePlaceGhostRecipe(message.containerId, message.recipeId);
        }
    }
}
