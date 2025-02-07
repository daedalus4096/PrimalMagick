package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Packet which informs a client to show a ghost arcane recipe in the GUI.
 * 
 * @author Daedalus4096
 */
public class PlaceGhostArcaneRecipePacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("place_ghost_arcane_recipe");
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

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(PlaceGhostArcaneRecipePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.containerId);
        buf.writeResourceLocation(message.recipeId);
    }
    
    public static PlaceGhostArcaneRecipePacket decode(RegistryFriendlyByteBuf buf) {
        return new PlaceGhostArcaneRecipePacket(buf.readVarInt(), buf.readResourceLocation());
    }
    
    public static void onMessage(PacketContext<PlaceGhostArcaneRecipePacket> ctx) {
        PlaceGhostArcaneRecipePacket message = ctx.message();
        if (Side.CLIENT.equals(ctx.side())) {
            ClientUtils.handlePlaceGhostRecipe(message.containerId, message.recipeId);
        }
    }
}
