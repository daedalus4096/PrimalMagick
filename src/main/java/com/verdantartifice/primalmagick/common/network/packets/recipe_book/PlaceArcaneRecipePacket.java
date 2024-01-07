package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import com.verdantartifice.primalmagick.common.menus.base.IArcaneRecipeBookMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

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
    
    public PlaceArcaneRecipePacket(int containerId, RecipeHolder<?> recipe, boolean shiftDown) {
        this.containerId = containerId;
        this.recipeId = recipe.id();
        this.shiftDown = shiftDown;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
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
    
    public static void onMessage(PlaceArcaneRecipePacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        player.resetLastActionTime();
        if (!player.isSpectator() && player.containerMenu.containerId == message.containerId && player.containerMenu instanceof IArcaneRecipeBookMenu<?> bookMenu) {
            player.getServer().getRecipeManager().byKey(message.recipeId).ifPresent(recipe -> {
                bookMenu.handlePlacement(message.shiftDown, recipe, player);
            });
        }
    }
}
