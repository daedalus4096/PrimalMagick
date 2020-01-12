package com.verdantartifice.primalmagic.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CycleActiveSpellPacket implements IMessageToServer {
    protected boolean reverse = false;
    
    public CycleActiveSpellPacket() {}
    
    public CycleActiveSpellPacket(boolean reverse) {
        this.reverse = reverse;
    }
    
    public static void encode(CycleActiveSpellPacket message, PacketBuffer buf) {
        buf.writeBoolean(message.reverse);
    }
    
    public static CycleActiveSpellPacket decode(PacketBuffer buf) {
        CycleActiveSpellPacket message = new CycleActiveSpellPacket();
        message.reverse = buf.readBoolean();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(CycleActiveSpellPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player != null) {
                    if (player.getHeldItemMainhand().getItem() instanceof IWand) {
                        SpellManager.cycleActiveSpell(player, player.getHeldItemMainhand(), message.reverse);
                    } else if (player.getHeldItemOffhand().getItem() instanceof IWand) {
                        SpellManager.cycleActiveSpell(player, player.getHeldItemOffhand(), message.reverse);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
