package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.tiles.crafting.SpellcraftingAltarTileEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent from the server to trigger a spellcrafting rune particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingRunePacket implements IMessageToClient {
    protected SpellcraftingAltarTileEntity.Segment segment;
    protected double x;
    protected double y;
    protected double z;
    protected double dx;
    protected double dy;
    protected double dz;
    protected int color;

    public SpellcraftingRunePacket() {}
    
    public SpellcraftingRunePacket(SpellcraftingAltarTileEntity.Segment segment, double x, double y, double z, double dx, double dy, double dz, int color) {
        this.segment = segment;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.color = color;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SpellcraftingRunePacket message, FriendlyByteBuf buf) {
        buf.writeEnum(message.segment);
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeDouble(message.dx);
        buf.writeDouble(message.dy);
        buf.writeDouble(message.dz);
        buf.writeVarInt(message.color);
    }
    
    public static SpellcraftingRunePacket decode(FriendlyByteBuf buf) {
        SpellcraftingRunePacket message = new SpellcraftingRunePacket();
        message.segment = buf.readEnum(SpellcraftingAltarTileEntity.Segment.class);
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        message.dx = buf.readDouble();
        message.dy = buf.readDouble();
        message.dz = buf.readDouble();
        message.color = buf.readVarInt();
        return message;
    }
    
    public static void onMessage(SpellcraftingRunePacket message, CustomPayloadEvent.Context ctx) {
        switch (message.segment) {
        case U1:
        case U2:
            FxDispatcher.INSTANCE.spellcraftingRuneU(message.x, message.y, message.z, message.dx, message.dy, message.dz, message.color);
            break;
        case V1:
        case V2:
            FxDispatcher.INSTANCE.spellcraftingRuneV(message.x, message.y, message.z, message.dx, message.dy, message.dz, message.color);
            break;
        case T1:
        case T2:
            FxDispatcher.INSTANCE.spellcraftingRuneT(message.x, message.y, message.z, message.dx, message.dy, message.dz, message.color);
            break;
        case D1:
        case D2:
            FxDispatcher.INSTANCE.spellcraftingRuneD(message.x, message.y, message.z, message.dx, message.dy, message.dz, message.color);
            break;
        default:
            throw new IllegalArgumentException("Unrecognized spellcrafting altar segment type");
        }
    }
}
