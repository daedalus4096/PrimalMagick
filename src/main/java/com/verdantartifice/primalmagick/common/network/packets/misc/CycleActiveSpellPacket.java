package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet sent to trigger an update of an equipped wand's NBT for spell selection on the server.
 * 
 * @author Daedalus4096
 */
public class CycleActiveSpellPacket implements IMessageToServer {
    protected boolean reverse = false;
    
    public CycleActiveSpellPacket() {}
    
    public CycleActiveSpellPacket(boolean reverse) {
        this.reverse = reverse;
    }
    
    public static void encode(CycleActiveSpellPacket message, FriendlyByteBuf buf) {
        buf.writeBoolean(message.reverse);
    }
    
    public static CycleActiveSpellPacket decode(FriendlyByteBuf buf) {
        CycleActiveSpellPacket message = new CycleActiveSpellPacket();
        message.reverse = buf.readBoolean();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(CycleActiveSpellPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    // Mainhand takes priority over the offhand in case two wands are equipped
                    if (player.getMainHandItem().getItem() instanceof IWand) {
                        SpellManager.cycleActiveSpell(player, player.getMainHandItem(), message.reverse);
                    } else if (player.getOffhandItem().getItem() instanceof IWand) {
                        SpellManager.cycleActiveSpell(player, player.getOffhandItem(), message.reverse);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
