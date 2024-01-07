package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
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
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(OpenEnchantedBookScreenPacket message, FriendlyByteBuf buf) {
        buf.writeResourceKey(message.bookKey);
    }
    
    public static OpenEnchantedBookScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenEnchantedBookScreenPacket(buf.readResourceKey(ForgeRegistries.Keys.ENCHANTMENTS));
    }
    
    public static void onMessage(OpenEnchantedBookScreenPacket message, CustomPayloadEvent.Context ctx) {
        if (FMLEnvironment.dist.isClient()) {
            ClientUtils.openStaticBookScreen(message.bookKey, BookLanguagesPM.GALACTIC.getId(), 0, BookType.BOOK);
        }
    }
}
