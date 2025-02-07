package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.recipe_book.ArcaneSearchRegistry;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Packet to sync arcane recipe book capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncArcaneRecipeBookPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_arcane_recipe_book");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncArcaneRecipeBookPacket> STREAM_CODEC = StreamCodec.ofMember(SyncArcaneRecipeBookPacket::encode, SyncArcaneRecipeBookPacket::decode);

    protected final CompoundTag data;
    
    public SyncArcaneRecipeBookPacket(Player player) {
        this.data = Services.CAPABILITIES.arcaneRecipeBook(player).map(book -> book.serializeNBT(player.registryAccess())).orElse(null);
    }
    
    protected SyncArcaneRecipeBookPacket(CompoundTag data) {
        this.data = data;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncArcaneRecipeBookPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncArcaneRecipeBookPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncArcaneRecipeBookPacket(buf.readNbt());
    }
    
    public static void onMessage(PacketContext<SyncArcaneRecipeBookPacket> ctx) {
        SyncArcaneRecipeBookPacket message = ctx.message();
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.arcaneRecipeBook(player).ifPresent(recipeBook -> {
                recipeBook.deserializeNBT(player.registryAccess(), message.data, player.level().getRecipeManager());
            });
            ArcaneSearchRegistry.populate();
        }
    }
}
