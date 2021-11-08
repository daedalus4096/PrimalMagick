package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet that informs the server of updated settings to the player's arcane recipe book.
 * 
 * @author Daedalus4096
 */
public class ChangeArcaneRecipeBookSettingsPacket implements IMessageToServer {
    protected ArcaneRecipeBookType type;
    protected boolean open;
    protected boolean filtering;
    
    public ChangeArcaneRecipeBookSettingsPacket() {
        this.type = null;
        this.open = false;
        this.filtering = false;
    }
    
    public ChangeArcaneRecipeBookSettingsPacket(ArcaneRecipeBookType type, boolean open, boolean filtering) {
        this.type = type;
        this.open = open;
        this.filtering = filtering;
    }
    
    public static void encode(ChangeArcaneRecipeBookSettingsPacket message, FriendlyByteBuf buf) {
        buf.writeEnum(message.type);
        buf.writeBoolean(message.open);
        buf.writeBoolean(message.filtering);
    }
    
    public static ChangeArcaneRecipeBookSettingsPacket decode(FriendlyByteBuf buf) {
        ChangeArcaneRecipeBookSettingsPacket message = new ChangeArcaneRecipeBookSettingsPacket();
        message.type = buf.readEnum(ArcaneRecipeBookType.class);
        message.open = buf.readBoolean();
        message.filtering = buf.readBoolean();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(ChangeArcaneRecipeBookSettingsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                PrimalMagicCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
                    recipeBook.get().setBookSettings(message.type, message.open, message.filtering);
                });
            });

            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
