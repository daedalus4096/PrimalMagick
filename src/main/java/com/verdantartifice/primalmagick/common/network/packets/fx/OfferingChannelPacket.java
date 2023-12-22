package com.verdantartifice.primalmagick.common.network.packets.fx;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent from the server to trigger an offering channel particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class OfferingChannelPacket implements IMessageToClient {
    protected double x1;
    protected double y1;
    protected double z1;
    protected double x2;
    protected double y2;
    protected double z2;
    protected ItemStack stack;
    
    public OfferingChannelPacket() {}
    
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
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(OfferingChannelPacket message, FriendlyByteBuf buf) {
        buf.writeDouble(message.x1);
        buf.writeDouble(message.y1);
        buf.writeDouble(message.z1);
        buf.writeDouble(message.x2);
        buf.writeDouble(message.y2);
        buf.writeDouble(message.z2);
        buf.writeItem(message.stack);
    }
    
    public static OfferingChannelPacket decode(FriendlyByteBuf buf) {
        OfferingChannelPacket message = new OfferingChannelPacket();
        message.x1 = buf.readDouble();
        message.y1 = buf.readDouble();
        message.z1 = buf.readDouble();
        message.x2 = buf.readDouble();
        message.y2 = buf.readDouble();
        message.z2 = buf.readDouble();
        message.stack = buf.readItem();
        return message;
    }
    
    public static void onMessage(OfferingChannelPacket message, CustomPayloadEvent.Context ctx) {
        FxDispatcher.INSTANCE.offeringChannel(message.x1, message.y1, message.z1, message.x2, message.y2, message.z2, message.stack);
    }
}
