package com.verdantartifice.primalmagick.common.network.packets.spellcrafting;

import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.spells.SpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent to update the value of a spell component's property on the server in the spellcrafting altar GUI.
 * 
 * @author Daedalus4096
 */
public class SetSpellComponentPropertyPacket implements IMessageToServer {
    protected int windowId;
    protected SpellComponent attr;
    protected SpellProperty property;
    protected int value;

    public SetSpellComponentPropertyPacket(int windowId, SpellComponent attr, SpellProperty property, int value) {
        this.windowId = windowId;
        this.attr = attr;
        this.property = property;
        this.value = value;
    }

    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(SetSpellComponentPropertyPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
        buf.writeEnum(message.attr);
        buf.writeUtf(message.property);
        buf.writeVarInt(message.value);
    }
    
    public static SetSpellComponentPropertyPacket decode(FriendlyByteBuf buf) {
        return new SetSpellComponentPropertyPacket(buf.readVarInt(), buf.readEnum(SpellComponent.class), "", buf.readVarInt());
    }
    
    public static void onMessage(SetSpellComponentPropertyPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof SpellcraftingAltarMenu altarMenu) {
            // Update the property value if the open menu window matches the given one
            altarMenu.setSpellPropertyValue(message.attr, message.property, message.value);
        }
    }
}
