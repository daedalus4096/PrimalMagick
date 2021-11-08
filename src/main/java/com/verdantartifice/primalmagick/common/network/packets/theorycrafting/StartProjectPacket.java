package com.verdantartifice.primalmagick.common.network.packets.theorycrafting;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagick.common.containers.ResearchTableContainer;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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
    
    public static void encode(StartProjectPacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.windowId);
    }
    
    public static StartProjectPacket decode(FriendlyByteBuf buf) {
        StartProjectPacket message = new StartProjectPacket();
        message.windowId = buf.readInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(StartProjectPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                PrimalMagicCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                    if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof ResearchTableContainer) {
                        ((ResearchTableContainer)player.containerMenu).getWorldPosCallable().execute((world, blockPos) -> {
                            knowledge.setActiveResearchProject(TheorycraftManager.createRandomProject(player, blockPos));
                        });
                        knowledge.sync(player);
                    }
                });
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
