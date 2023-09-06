package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Packet sent from the server to trigger the opening of of an enchanted book on the client.
 * 
 * @author Daedalus4096
 */
public class OpenEnchantedBookScreenPacket implements IMessageToClient {
    protected final ResourceKey<?> bookKey;
    
    public OpenEnchantedBookScreenPacket(Enchantment ench) {
        this.bookKey = ForgeRegistries.ENCHANTMENTS.getResourceKey(ench).orElseThrow();
    }
    
    private OpenEnchantedBookScreenPacket(ResourceKey<?> bookKey) {
        this.bookKey = bookKey;
    }
    
    public static void encode(OpenEnchantedBookScreenPacket message, FriendlyByteBuf buf) {
        buf.writeResourceKey(message.bookKey);
    }
    
    public static OpenEnchantedBookScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenEnchantedBookScreenPacket(buf.readResourceKey(ForgeRegistries.Keys.ENCHANTMENTS));
    }
    
    public static class Handler {
        public static void onMessage(OpenEnchantedBookScreenPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    ClientUtils.openStaticBookScreen(message.bookKey, BookLanguagesPM.GALACTIC.getId());
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
