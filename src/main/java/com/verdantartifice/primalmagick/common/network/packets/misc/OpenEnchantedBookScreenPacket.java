package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet sent from the server to trigger the opening of of an enchanted book on the client.
 * 
 * @author Daedalus4096
 */
public class OpenEnchantedBookScreenPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenEnchantedBookScreenPacket> STREAM_CODEC = StreamCodec.ofMember(OpenEnchantedBookScreenPacket::encode, OpenEnchantedBookScreenPacket::decode);

    protected final ResourceKey<Enchantment> bookKey;
    
    public OpenEnchantedBookScreenPacket(Holder<Enchantment> ench) {
        this.bookKey = ench.unwrapKey().orElse(null);
    }
    
    protected OpenEnchantedBookScreenPacket(ResourceKey<Enchantment> enchKey) {
        this.bookKey = enchKey;
    }
    
    public static void encode(OpenEnchantedBookScreenPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeResourceKey(message.bookKey);
    }
    
    public static OpenEnchantedBookScreenPacket decode(RegistryFriendlyByteBuf buf) {
        return new OpenEnchantedBookScreenPacket(buf.readResourceKey(Registries.ENCHANTMENT));
    }
    
    public static void onMessage(OpenEnchantedBookScreenPacket message, CustomPayloadEvent.Context ctx) {
        if (FMLEnvironment.dist.isClient()) {
            ClientUtils.openStaticBookScreen(message.bookKey, BookLanguagesPM.GALACTIC, 0, BookType.BOOK);
        }
    }
}
