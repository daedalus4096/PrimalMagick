package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to trigger an update of an equipped wand's NBT for spell selection on the server.
 * 
 * @author Daedalus4096
 */
public class SetActiveSpellPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, SetActiveSpellPacket> STREAM_CODEC = StreamCodec.ofMember(SetActiveSpellPacket::encode, SetActiveSpellPacket::decode);

    protected final int index;
    
    public SetActiveSpellPacket(int index) {
        this.index = index;
    }
    
    public static void encode(SetActiveSpellPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.index);
    }
    
    public static SetActiveSpellPacket decode(RegistryFriendlyByteBuf buf) {
        return new SetActiveSpellPacket(buf.readVarInt());
    }
    
    public static void onMessage(SetActiveSpellPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player != null) {
            // Mainhand takes priority over the offhand in case two wands are equipped
            if (player.getMainHandItem().getItem() instanceof IWand) {
                SpellManager.setActiveSpell(player, player.getMainHandItem(), message.index);
            } else if (player.getOffhandItem().getItem() instanceof IWand) {
                SpellManager.setActiveSpell(player, player.getOffhandItem(), message.index);
            }
        }
    }
}
