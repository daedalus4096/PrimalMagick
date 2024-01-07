package com.verdantartifice.primalmagick.common.network.packets.theorycrafting;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent to start a research project on the server in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class StartProjectPacket implements IMessageToServer {
    protected int windowId;

    public StartProjectPacket() {
        this.windowId = -1;
    }
    
    public StartProjectPacket(int windowId) {
        this.windowId = windowId;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(StartProjectPacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.windowId);
    }
    
    public static StartProjectPacket decode(FriendlyByteBuf buf) {
        StartProjectPacket message = new StartProjectPacket();
        message.windowId = buf.readInt();
        return message;
    }
    
    public static void onMessage(StartProjectPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof ResearchTableMenu menu) {
                menu.getContainerLevelAccess().execute((world, blockPos) -> {
                    knowledge.setActiveResearchProject(TheorycraftManager.createRandomProject(player, blockPos));
                });
                knowledge.sync(player);
            }
        });
    }
}
