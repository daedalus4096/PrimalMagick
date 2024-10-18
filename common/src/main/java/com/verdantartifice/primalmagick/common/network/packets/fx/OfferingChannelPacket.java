package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Packet sent from the server to trigger an offering channel particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class OfferingChannelPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("offering_channel");
    public static final StreamCodec<RegistryFriendlyByteBuf, OfferingChannelPacket> STREAM_CODEC = StreamCodec.ofMember(OfferingChannelPacket::encode, OfferingChannelPacket::decode);

    protected final double x1;
    protected final double y1;
    protected final double z1;
    protected final double x2;
    protected final double y2;
    protected final double z2;
    protected final ItemStack stack;
    
    public OfferingChannelPacket(double x1, double y1, double z1, double x2, double y2, double z2, ItemStack stack) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.stack = stack;
    }
    
    public OfferingChannelPacket(double x1, double y1, double z1, @Nonnull BlockPos target, ItemStack stack) {
        this(x1, y1, z1, target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, stack);
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(OfferingChannelPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeDouble(message.x1);
        buf.writeDouble(message.y1);
        buf.writeDouble(message.z1);
        buf.writeDouble(message.x2);
        buf.writeDouble(message.y2);
        buf.writeDouble(message.z2);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, message.stack);
    }
    
    public static OfferingChannelPacket decode(RegistryFriendlyByteBuf buf) {
        return new OfferingChannelPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(),
                ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
    }
    
    public static void onMessage(PacketContext<OfferingChannelPacket> ctx) {
        OfferingChannelPacket message = ctx.message();
        FxDispatcher.INSTANCE.offeringChannel(message.x1, message.y1, message.z1, message.x2, message.y2, message.z2, message.stack);
    }
}
