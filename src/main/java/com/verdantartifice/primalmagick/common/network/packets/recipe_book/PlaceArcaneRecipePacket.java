package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.menus.IArcaneRecipeBookMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet which informs a server to place a recipe in an arcane recipe book menu.
 * 
 * @author Daedalus4096
 */
public class PlaceArcaneRecipePacket implements IMessageToServer {
    protected int containerId;
    protected ResourceLocation recipeId;
    protected boolean shiftDown;

    public PlaceArcaneRecipePacket() {
        this.containerId = -1;
        this.recipeId = null;
        this.shiftDown = false;
    }
    
    public PlaceArcaneRecipePacket(int containerId, Recipe<?> recipe, boolean shiftDown) {
        this.containerId = containerId;
        this.recipeId = recipe.getId();
        this.shiftDown = shiftDown;
    }
    
    public static void encode(PlaceArcaneRecipePacket message, FriendlyByteBuf buf) {
        buf.writeByte(message.containerId);
        buf.writeResourceLocation(message.recipeId);
        buf.writeBoolean(message.shiftDown);
    }
    
    public static PlaceArcaneRecipePacket decode(FriendlyByteBuf buf) {
        PlaceArcaneRecipePacket message = new PlaceArcaneRecipePacket();
        message.containerId = buf.readByte();
        message.recipeId = buf.readResourceLocation();
        message.shiftDown = buf.readBoolean();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(PlaceArcaneRecipePacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                player.resetLastActionTime();
                if (!player.isSpectator() && player.containerMenu.containerId == message.containerId && player.containerMenu instanceof IArcaneRecipeBookMenu<?> bookMenu) {
                    player.getServer().getRecipeManager().byKey(message.recipeId).ifPresent(recipe -> {
                        bookMenu.handlePlacement(message.shiftDown, recipe, player);
                    });
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
