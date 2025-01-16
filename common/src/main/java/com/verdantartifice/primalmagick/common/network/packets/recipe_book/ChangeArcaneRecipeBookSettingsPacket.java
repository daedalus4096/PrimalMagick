package com.verdantartifice.primalmagick.common.network.packets.recipe_book;

import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet that informs the server of updated settings to the player's arcane recipe book.
 * 
 * @author Daedalus4096
 */
public class ChangeArcaneRecipeBookSettingsPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("change_arcane_recipe_book_settings");
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

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(ChangeArcaneRecipeBookSettingsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeEnum(message.type);
        buf.writeBoolean(message.open);
        buf.writeBoolean(message.filtering);
    }
    
    public static ChangeArcaneRecipeBookSettingsPacket decode(RegistryFriendlyByteBuf buf) {
        return new ChangeArcaneRecipeBookSettingsPacket(buf.readEnum(ArcaneRecipeBookType.class), buf.readBoolean(), buf.readBoolean());
    }
    
    public static void onMessage(PacketContext<ChangeArcaneRecipeBookSettingsPacket> ctx) {
        ChangeArcaneRecipeBookSettingsPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        Services.CAPABILITIES.arcaneRecipeBook(player).ifPresent(recipeBook -> {
            recipeBook.get().setBookSettings(message.type, message.open, message.filtering);
        });
    }
}
