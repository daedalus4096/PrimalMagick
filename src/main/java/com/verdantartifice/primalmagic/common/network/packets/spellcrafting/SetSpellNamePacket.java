package com.verdantartifice.primalmagic.common.network.packets.spellcrafting;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.containers.SpellcraftingAltarContainer;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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
    
    public static void encode(SetSpellNamePacket message, PacketBuffer buf) {
        buf.writeInt(message.windowId);
        buf.writeString(message.name);
    }
    
    public static SetSpellNamePacket decode(PacketBuffer buf) {
        SetSpellNamePacket message = new SetSpellNamePacket();
        message.windowId = buf.readInt();
        message.name = buf.readString();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SetSpellNamePacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player.openContainer != null && player.openContainer.windowId == message.windowId && player.openContainer instanceof SpellcraftingAltarContainer) {
                    // Update the spell name if the open container window matches the given one
                    ((SpellcraftingAltarContainer)player.openContainer).setSpellName(message.name);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
