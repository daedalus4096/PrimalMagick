package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.menus.IRecipeDisplayListener;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

public class SetActiveRecipeDisplayPacket implements IMessageToClient {
    public static final Identifier CHANNEL = ResourceUtils.loc("set_active_recipe_display");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetActiveRecipeDisplayPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, p -> p.containerId,
            RecipeDisplay.STREAM_CODEC, p -> p.display,
            SetActiveRecipeDisplayPacket::new);

    private final int containerId;
    private final RecipeDisplay display;

    public SetActiveRecipeDisplayPacket(int containerId, RecipeDisplay display) {
        this.containerId = containerId;
        this.display = display;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<SetActiveRecipeDisplayPacket> ctx) {
        SetActiveRecipeDisplayPacket message = ctx.message();
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null && player.containerMenu instanceof IRecipeDisplayListener listener && player.containerMenu.containerId == message.containerId) {
            listener.setRecipeDisplay(message.display);
        }
    }
}
