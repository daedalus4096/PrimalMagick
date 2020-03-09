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
 * Packet sent to complete a research project on the server in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class CompleteProjectPacket implements IMessageToServer {
    protected int windowId;

    public CompleteProjectPacket() {
        this.windowId = -1;
    }
    
    public CompleteProjectPacket(int windowId) {
        this.windowId = windowId;
    }
    
    public static void encode(CompleteProjectPacket message, PacketBuffer buf) {
        buf.writeInt(message.windowId);
    }
    
    public static CompleteProjectPacket decode(PacketBuffer buf) {
        CompleteProjectPacket message = new CompleteProjectPacket();
        message.windowId = buf.readInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(CompleteProjectPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                
                // Consume paper and ink
                if (player.openContainer != null && player.openContainer.windowId == message.windowId && player.openContainer instanceof ResearchTableContainer) {
                    ((ResearchTableContainer)player.openContainer).consumeWritingImplements();
                }

                // TODO Determine if current project is a success
                
                // Set new project
                knowledge.setActiveResearchProject(TheorycraftManager.createRandomProject(player));
                knowledge.sync(player);
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
