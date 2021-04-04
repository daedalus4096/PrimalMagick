package com.verdantartifice.primalmagic.common.network.packets.spellcrafting;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagic.common.containers.SpellcraftingAltarContainer;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.spells.SpellComponent;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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
    
    public static void encode(SetSpellComponentTypeIndexPacket message, PacketBuffer buf) {
        buf.writeInt(message.windowId);
        buf.writeString(message.attr.name());
        buf.writeInt(message.index);
    }
    
    public static SetSpellComponentTypeIndexPacket decode(PacketBuffer buf) {
        SetSpellComponentTypeIndexPacket message = new SetSpellComponentTypeIndexPacket();
        message.windowId = buf.readInt();
        String attrStr = buf.readString();
        try {
            message.attr = SpellComponent.valueOf(attrStr);
        } catch (Exception e) {
            LOGGER.warn("Received SetSpellAttributeTypeIndexPacket with unexpected attr value {}", attrStr);
            message.attr = null;
        }
        message.index = buf.readInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SetSpellComponentTypeIndexPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player.openContainer != null && player.openContainer.windowId == message.windowId && player.openContainer instanceof SpellcraftingAltarContainer) {
                    // Update the appropriate spell component type if the open container window matches the given one
                    SpellcraftingAltarContainer container = (SpellcraftingAltarContainer)player.openContainer;
                    switch (message.attr) {
                    case VEHICLE:
                        container.setSpellVehicleTypeIndex(message.index);
                        break;
                    case PAYLOAD:
                        container.setSpellPayloadTypeIndex(message.index);
                        break;
                    case PRIMARY_MOD:
                        container.setSpellPrimaryModTypeIndex(message.index);
                        break;
                    case SECONDARY_MOD:
                        container.setSpellSecondaryModTypeIndex(message.index);
                        break;
                    default:
                        // Do nothing
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
