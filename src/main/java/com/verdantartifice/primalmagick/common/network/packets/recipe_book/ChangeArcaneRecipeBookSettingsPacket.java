package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet that informs the server of updated settings to the player's arcane recipe book.
 * 
 * @author Daedalus4096
 */
public class ChangeArcaneRecipeBookSettingsPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeArcaneRecipeBookSettingsPacket> STREAM_CODEC = StreamCodec.ofMember(
            ChangeArcaneRecipeBookSettingsPacket::encode, ChangeArcaneRecipeBookSettingsPacket::decode);

    protected final ArcaneRecipeBookType type;
    protected final boolean open;
    protected final boolean filtering;
    
    public ChangeArcaneRecipeBookSettingsPacket(ArcaneRecipeBookType type, boolean open, boolean filtering) {
        this.type = type;
        this.open = open;
        this.filtering = filtering;
    }
    
    public static void encode(ChangeArcaneRecipeBookSettingsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeEnum(message.type);
        buf.writeBoolean(message.open);
        buf.writeBoolean(message.filtering);
    }
    
    public static ChangeArcaneRecipeBookSettingsPacket decode(RegistryFriendlyByteBuf buf) {
        return new ChangeArcaneRecipeBookSettingsPacket(buf.readEnum(ArcaneRecipeBookType.class), buf.readBoolean(), buf.readBoolean());
    }
    
    public static void onMessage(ChangeArcaneRecipeBookSettingsPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        PrimalMagickCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
            recipeBook.get().setBookSettings(message.type, message.open, message.filtering);
        });
    }
}
