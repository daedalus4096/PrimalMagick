package com.verdantartifice.primalmagic.common.network.packets.theorycrafting;

import java.util.Random;
import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.containers.ResearchTableContainer;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.TheorycraftManager;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvents;
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

                // Determine if current project is a success
                AbstractProject project = knowledge.getActiveResearchProject();
                Random rand = player.getRNG();
                if (project != null && project.isSatisfied(player) && project.consumeSelectedMaterials(player)) {
                    if (rand.nextDouble() < project.getSuccessChance(player)) {
                        ResearchManager.addKnowledge(player, IPlayerKnowledge.KnowledgeType.THEORY, project.getTheoryPointReward());
                        StatsManager.incrementValue(player, StatsPM.RESEARCH_PROJECTS_COMPLETED);
                        PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundsPM.WRITING.get(), 1.0F, 1.0F + (float)rand.nextGaussian() * 0.05F), player);
                    } else {
                        PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F + (float)rand.nextGaussian() * 0.05F), player);
                    }
                }
                
                // Set new project
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
