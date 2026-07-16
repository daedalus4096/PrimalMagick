package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

/**
 * Packet sent from the server to trigger a spell bolt particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class SpellBoltPacket implements IMessageToClient {
    public static final Identifier CHANNEL = ResourceUtils.loc("spell_bolt");
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellBoltPacket> STREAM_CODEC = StreamCodec.composite(
            Vec3.STREAM_CODEC, p -> p.source,
            Vec3.STREAM_CODEC, p -> p.target,
            ByteBufCodecs.VAR_INT, p -> p.color,
            SpellBoltPacket::new);

    protected final Vec3 source;
    protected final Vec3 target;
    protected final int color;

    public SpellBoltPacket(Vec3 source, Vec3 target, int color) {
        this.source = source;
        this.target = target;
        this.color = color;
    }
    
    public SpellBoltPacket(BlockPos source, BlockPos target, int color) {
        this(Vec3.atCenterOf(source), Vec3.atCenterOf(target), color);
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<SpellBoltPacket> ctx) {
        SpellBoltPacket message = ctx.message();
        FxDispatcher.INSTANCE.spellBolt(message.source, message.target, message.color);
    }
}
