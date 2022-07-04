package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet sent to trigger an update of an equipped wand's NBT for spell selection on the server.
 * 
 * @author Daedalus4096
 */
public class SetActiveSpellPacket implements IMessageToServer {
    protected int index = -1;
    
    public SetActiveSpellPacket() {}
    
    public SetActiveSpellPacket(int index) {
        this.index = index;
    }
    
    public static void encode(SetActiveSpellPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.index);
    }
    
    public static SetActiveSpellPacket decode(FriendlyByteBuf buf) {
        SetActiveSpellPacket message = new SetActiveSpellPacket();
        message.index = buf.readVarInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SetActiveSpellPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    // Mainhand takes priority over the offhand in case two wands are equipped
                    if (player.getMainHandItem().getItem() instanceof IWand) {
                        SpellManager.setActiveSpell(player, player.getMainHandItem(), message.index);
                    } else if (player.getOffhandItem().getItem() instanceof IWand) {
                        SpellManager.setActiveSpell(player, player.getOffhandItem(), message.index);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
