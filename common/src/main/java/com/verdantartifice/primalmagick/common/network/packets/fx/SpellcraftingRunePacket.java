package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.tiles.crafting.SpellcraftingAltarTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Packet sent from the server to trigger a spellcrafting rune particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingRunePacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("spellcrafting_rune");
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellcraftingRunePacket> STREAM_CODEC = StreamCodec.ofMember(SpellcraftingRunePacket::encode, SpellcraftingRunePacket::decode);

    protected final SpellcraftingAltarTileEntity.Segment segment;
    protected final double x;
    protected final double y;
    protected final double z;
    protected final double dx;
    protected final double dy;
    protected final double dz;
    protected final int color;

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

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SpellcraftingRunePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeEnum(message.segment);
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeDouble(message.dx);
        buf.writeDouble(message.dy);
        buf.writeDouble(message.dz);
        buf.writeVarInt(message.color);
    }
    
    public static SpellcraftingRunePacket decode(RegistryFriendlyByteBuf buf) {
        return new SpellcraftingRunePacket(buf.readEnum(SpellcraftingAltarTileEntity.Segment.class), buf.readDouble(), buf.readDouble(), buf.readDouble(),
                buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<SpellcraftingRunePacket> ctx) {
        SpellcraftingRunePacket message = ctx.message();
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
