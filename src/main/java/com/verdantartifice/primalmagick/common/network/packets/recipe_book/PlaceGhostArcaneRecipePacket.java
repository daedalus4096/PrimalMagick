package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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
    
    public PlaceGhostArcaneRecipePacket(int containerId, Recipe<?> recipe) {
        this.containerId = containerId;
        this.recipeId = recipe.getId();
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
    
    public static class Handler {
        public static void onMessage(PlaceGhostArcaneRecipePacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    ClientUtils.handlePlaceGhostRecipe(message.containerId, message.recipeId);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
