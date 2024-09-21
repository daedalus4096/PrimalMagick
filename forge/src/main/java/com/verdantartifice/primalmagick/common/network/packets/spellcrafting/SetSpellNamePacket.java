package com.verdantartifice.primalmagick.common.network.packets.spellcrafting;

import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to update a spell package's name on the server in the spellcrafting altar GUI.
 * 
 * @author Daedalus4096
 */
public class SetSpellNamePacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, SetSpellNamePacket> STREAM_CODEC = StreamCodec.ofMember(SetSpellNamePacket::encode, SetSpellNamePacket::decode);

    protected final int windowId;
    protected final String name;
    
    public SetSpellNamePacket(int windowId, String name) {
        this.windowId = windowId;
        this.name = name;
    }
    
    public static void encode(SetSpellNamePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
        buf.writeUtf(message.name);
    }
    
    public static SetSpellNamePacket decode(RegistryFriendlyByteBuf buf) {
        return new SetSpellNamePacket(buf.readVarInt(), buf.readUtf());
    }
    
    public static void onMessage(SetSpellNamePacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof SpellcraftingAltarMenu menu) {
            // Update the spell name if the open menu window matches the given one
            menu.setSpellName(message.name);
        }
    }
}
