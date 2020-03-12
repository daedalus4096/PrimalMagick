package com.verdantartifice.primalmagic.common.network.packets.theorycrafting;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.containers.ResearchTableContainer;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.theorycrafting.TheorycraftManager;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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
    
    public static void encode(StartProjectPacket message, PacketBuffer buf) {
        buf.writeInt(message.windowId);
    }
    
    public static StartProjectPacket decode(PacketBuffer buf) {
        StartProjectPacket message = new StartProjectPacket();
        message.windowId = buf.readInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(StartProjectPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                if (player.openContainer != null && player.openContainer.windowId == message.windowId && player.openContainer instanceof ResearchTableContainer) {
                    ((ResearchTableContainer)player.openContainer).getWorldPosCallable().consume((world, blockPos) -> {
                        knowledge.setActiveResearchProject(TheorycraftManager.createRandomProject(player, blockPos));
                    });
                    knowledge.sync(player);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
