package com.verdantartifice.primalmagick.common.network.packets.spellcrafting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.spells.SpellComponent;

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
    private static final Logger LOGGER = LogManager.getLogger();

    protected int windowId;
    protected SpellComponent attr;
    protected String name;
    protected int value;

    public SetSpellComponentPropertyPacket() {
        this.windowId = -1;
        this.attr = null;
        this.name = "";
        this.value = -1;
    }
    
    public SetSpellComponentPropertyPacket(int windowId, SpellComponent attr, String name, int value) {
        this.windowId = windowId;
        this.attr = attr;
        this.name = name;
        this.value = value;
    }

    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(SetSpellComponentPropertyPacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.windowId);
        buf.writeUtf(message.attr.name());
        buf.writeUtf(message.name);
        buf.writeInt(message.value);
    }
    
    public static SetSpellComponentPropertyPacket decode(FriendlyByteBuf buf) {
        SetSpellComponentPropertyPacket message = new SetSpellComponentPropertyPacket();
        message.windowId = buf.readInt();
        String attrStr = buf.readUtf();
        try {
            message.attr = SpellComponent.valueOf(attrStr);
        } catch (Exception e) {
            LOGGER.warn("Received SetSpellComponentPropertyPacket with unexpected attr value {}", attrStr);
            message.attr = null;
        }
        message.name = buf.readUtf();
        message.value = buf.readInt();
        return message;
    }
    
    public static void onMessage(SetSpellComponentPropertyPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof SpellcraftingAltarMenu altarMenu) {
            // Update the property value if the open menu window matches the given one
            altarMenu.setSpellPropertyValue(message.attr, message.name, message.value);
        }
    }
}
