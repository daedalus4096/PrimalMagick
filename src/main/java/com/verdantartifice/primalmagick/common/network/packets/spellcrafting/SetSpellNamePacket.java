package com.verdantartifice.primalmagick.common.network.packets.spellcrafting;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet sent to update a spell package's name on the server in the spellcrafting altar GUI.
 * 
 * @author Daedalus4096
 */
public class SetSpellNamePacket implements IMessageToServer {
    protected int windowId;
    protected String name;
    
    public SetSpellNamePacket() {
        this.windowId = -1;
        this.name = "";
    }
    
    public SetSpellNamePacket(int windowId, String name) {
        this.windowId = windowId;
        this.name = name;
    }
    
    public static void encode(SetSpellNamePacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.windowId);
        buf.writeUtf(message.name);
    }
    
    public static SetSpellNamePacket decode(FriendlyByteBuf buf) {
        SetSpellNamePacket message = new SetSpellNamePacket();
        message.windowId = buf.readInt();
        message.name = buf.readUtf();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SetSpellNamePacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof SpellcraftingAltarMenu) {
                    // Update the spell name if the open container window matches the given one
                    ((SpellcraftingAltarMenu)player.containerMenu).setSpellName(message.name);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
