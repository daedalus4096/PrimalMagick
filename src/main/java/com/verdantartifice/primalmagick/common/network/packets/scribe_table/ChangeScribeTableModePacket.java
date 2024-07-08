package com.verdantartifice.primalmagick.common.network.packets.scribe_table;

import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.menus.AbstractScribeTableMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to change the active mode of the scribe table GUI for the player.
 * 
 * @author Daedalus4096
 */
public class ChangeScribeTableModePacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeScribeTableModePacket> STREAM_CODEC = StreamCodec.ofMember(ChangeScribeTableModePacket::encode, ChangeScribeTableModePacket::decode);

    protected final int windowId;
    protected final ScribeTableMode newMode;
    
    public ChangeScribeTableModePacket(int windowId, ScribeTableMode newMode) {
        this.windowId = windowId;
        this.newMode = newMode;
    }

    public static void encode(ChangeScribeTableModePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeInt(message.windowId);
        buf.writeEnum(message.newMode);
    }
    
    public static ChangeScribeTableModePacket decode(RegistryFriendlyByteBuf buf) {
        return new ChangeScribeTableModePacket(buf.readInt(), buf.readEnum(ScribeTableMode.class));
    }
    
    public static void onMessage(ChangeScribeTableModePacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof AbstractScribeTableMenu scribeMenu) {
            // Store the new mode and open the corresponding menu
            LinguisticsManager.setScribeTableMode(player, message.newMode);
            player.openMenu(scribeMenu.getTile(), scribeMenu.getTilePos());
        }
    }
}
