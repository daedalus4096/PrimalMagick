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
public class CycleActiveSpellPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, CycleActiveSpellPacket> STREAM_CODEC = StreamCodec.ofMember(CycleActiveSpellPacket::encode, CycleActiveSpellPacket::decode);

    protected final boolean reverse;
    
    public CycleActiveSpellPacket(boolean reverse) {
        this.reverse = reverse;
    }
    
    public static void encode(CycleActiveSpellPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(message.reverse);
    }
    
    public static CycleActiveSpellPacket decode(RegistryFriendlyByteBuf buf) {
        return new CycleActiveSpellPacket(buf.readBoolean());
    }
    
    public static void onMessage(CycleActiveSpellPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player != null) {
            // Mainhand takes priority over the offhand in case two wands are equipped
            if (player.getMainHandItem().getItem() instanceof IWand) {
                SpellManager.cycleActiveSpell(player, player.getMainHandItem(), message.reverse);
            } else if (player.getOffhandItem().getItem() instanceof IWand) {
                SpellManager.cycleActiveSpell(player, player.getOffhandItem(), message.reverse);
            }
        }
    }
}
