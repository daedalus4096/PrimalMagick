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
 * Packet sent to update the value of a spell component's type on the server in the spellcrafting altar GUI.
 * 
 * @author Daedalus4096
 */
public class SetSpellComponentTypeIndexPacket implements IMessageToServer {
    private static final Logger LOGGER = LogManager.getLogger();

    protected int windowId;
    protected SpellComponent attr;
    protected int index;

    public SetSpellComponentTypeIndexPacket() {
        this.windowId = -1;
        this.attr = null;
        this.index = -1;
    }
    
    public SetSpellComponentTypeIndexPacket(int windowId, SpellComponent attr, int index) {
        this.windowId = windowId;
        this.attr = attr;
        this.index = index;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(SetSpellComponentTypeIndexPacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.windowId);
        buf.writeUtf(message.attr.name());
        buf.writeInt(message.index);
    }
    
    public static SetSpellComponentTypeIndexPacket decode(FriendlyByteBuf buf) {
        SetSpellComponentTypeIndexPacket message = new SetSpellComponentTypeIndexPacket();
        message.windowId = buf.readInt();
        String attrStr = buf.readUtf();
        try {
            message.attr = SpellComponent.valueOf(attrStr);
        } catch (Exception e) {
            LOGGER.warn("Received SetSpellAttributeTypeIndexPacket with unexpected attr value {}", attrStr);
            message.attr = null;
        }
        message.index = buf.readInt();
        return message;
    }
    
    public static void onMessage(SetSpellComponentTypeIndexPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof SpellcraftingAltarMenu altarMenu) {
            // Update the appropriate spell component type if the open menu window matches the given one
            switch (message.attr) {
            case VEHICLE:
                altarMenu.setSpellVehicleTypeIndex(message.index);
                break;
            case PAYLOAD:
                altarMenu.setSpellPayloadTypeIndex(message.index);
                break;
            case PRIMARY_MOD:
                altarMenu.setSpellPrimaryModTypeIndex(message.index);
                break;
            case SECONDARY_MOD:
                altarMenu.setSpellSecondaryModTypeIndex(message.index);
                break;
            default:
                // Do nothing
            }
        }
    }
}
