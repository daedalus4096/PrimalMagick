package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.recipe_book.ArcaneSearchRegistry;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet to sync arcane recipe book capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncArcaneRecipeBookPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncArcaneRecipeBookPacket> STREAM_CODEC = StreamCodec.ofMember(SyncArcaneRecipeBookPacket::encode, SyncArcaneRecipeBookPacket::decode);

    protected final CompoundTag data;
    
    public SyncArcaneRecipeBookPacket(Player player) {
        if (PrimalMagickCapabilities.getArcaneRecipeBook(player).isPresent()) {
            this.data = PrimalMagickCapabilities.getArcaneRecipeBook(player).resolve().get().serializeNBT(player.registryAccess());
        } else {
            this.data = null;
        }
    }
    
    protected SyncArcaneRecipeBookPacket(CompoundTag data) {
        this.data = data;
    }
    
    public static void encode(SyncArcaneRecipeBookPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncArcaneRecipeBookPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncArcaneRecipeBookPacket(buf.readNbt());
    }
    
    public static void onMessage(SyncArcaneRecipeBookPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            PrimalMagickCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
                recipeBook.deserializeNBT(player.registryAccess(), message.data, player.level().getRecipeManager());
            });
            ArcaneSearchRegistry.populate();
        }
    }
}
