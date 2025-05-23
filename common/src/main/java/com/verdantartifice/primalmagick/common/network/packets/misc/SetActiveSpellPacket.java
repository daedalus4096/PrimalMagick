package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.IWand;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet sent to trigger an update of an equipped wand's NBT for spell selection on the server.
 * 
 * @author Daedalus4096
 */
public class SetActiveSpellPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("set_active_spell");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetActiveSpellPacket> STREAM_CODEC = StreamCodec.ofMember(SetActiveSpellPacket::encode, SetActiveSpellPacket::decode);

    protected final int index;
    
    public SetActiveSpellPacket(int index) {
        this.index = index;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SetActiveSpellPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.index);
    }
    
    public static SetActiveSpellPacket decode(RegistryFriendlyByteBuf buf) {
        return new SetActiveSpellPacket(buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<SetActiveSpellPacket> ctx) {
        SetActiveSpellPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        if (player != null) {
            // Main hand takes priority over the offhand in case two wands are equipped
            SpellManager.setActiveSpellIndex(player, player.getMainHandItem(), player.getOffhandItem(), message.index);
        }
    }
}
