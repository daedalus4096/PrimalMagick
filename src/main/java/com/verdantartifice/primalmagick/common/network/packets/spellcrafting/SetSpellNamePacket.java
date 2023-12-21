package com.verdantartifice.primalmagick.common.network.packets.spellcrafting;

import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

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
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
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
    
    public static void onMessage(SetSpellNamePacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof SpellcraftingAltarMenu menu) {
            // Update the spell name if the open menu window matches the given one
            menu.setSpellName(message.name);
        }
    }
}
