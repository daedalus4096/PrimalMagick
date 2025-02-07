package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.google.common.base.Preconditions;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.books.BookView;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Packet sent from the server to trigger the opening of a static book on the client.
 * 
 * @author Daedalus4096
 */
public class OpenStaticBookScreenPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("open_static_book_screen");
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenStaticBookScreenPacket> STREAM_CODEC = StreamCodec.ofMember(OpenStaticBookScreenPacket::encode, OpenStaticBookScreenPacket::decode);

    // TODO Can this be strongly typed as a ResourceKey<BookDefinition> instead?
    protected final BookView view;
    protected final BookType bookType;
    
    public OpenStaticBookScreenPacket(ItemStack bookStack, BookType bookType, HolderLookup.Provider registries) {
        Preconditions.checkArgument(bookStack.is(ItemTagsPM.STATIC_BOOKS), "Packet item stack must be a static book or tablet");
        this.view = StaticBookItem.makeBookView(bookStack, registries);
        this.bookType = bookType;
    }
    
    private OpenStaticBookScreenPacket(BookView view, BookType bookType) {
        this.view = view;
        this.bookType = bookType;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(OpenStaticBookScreenPacket message, RegistryFriendlyByteBuf buf) {
        BookView.STREAM_CODEC.encode(buf, message.view);
        buf.writeEnum(message.bookType);
    }
    
    public static OpenStaticBookScreenPacket decode(RegistryFriendlyByteBuf buf) {
        return new OpenStaticBookScreenPacket(BookView.STREAM_CODEC.decode(buf), buf.readEnum(BookType.class));
    }
    
    public static void onMessage(PacketContext<OpenStaticBookScreenPacket> ctx) {
        OpenStaticBookScreenPacket message = ctx.message();
        if (Side.CLIENT.equals(ctx.side())) {
            ClientUtils.openStaticBookScreen(message.view, message.bookType);
        }
    }
}
