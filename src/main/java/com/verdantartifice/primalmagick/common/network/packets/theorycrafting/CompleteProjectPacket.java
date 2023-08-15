package com.verdantartifice.primalmagick.common.network.packets.theorycrafting;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraftforge.network.NetworkEvent;

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
    
    public static void encode(CompleteProjectPacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.windowId);
    }
    
    public static CompleteProjectPacket decode(FriendlyByteBuf buf) {
        CompleteProjectPacket message = new CompleteProjectPacket();
        message.windowId = buf.readInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(CompleteProjectPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                    // Consume paper and ink
                    if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof ResearchTableMenu researchMenu) {
                        researchMenu.consumeWritingImplements();
                        researchMenu.getWorldPosCallable().execute((world, blockPos) -> {
                            // Determine if current project is a success
                            Project project = knowledge.getActiveResearchProject();
                            RandomSource rand = player.getRandom();
                            if (project != null && project.isSatisfied(player, TheorycraftManager.getSurroundings(world, blockPos)) && project.consumeSelectedMaterials(player)) {
                                if (rand.nextDouble() < project.getSuccessChance()) {
                                    ResearchManager.addKnowledge(player, IPlayerKnowledge.KnowledgeType.THEORY, project.getTheoryPointReward());
                                    StatsManager.incrementValue(player, StatsPM.RESEARCH_PROJECTS_COMPLETED);
                                    PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundsPM.WRITING.get(), 1.0F, 1.0F + (float)rand.nextGaussian() * 0.05F), player);
                                } else {
                                    PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundEvents.GLASS_BREAK, 1.0F, 1.0F + (float)rand.nextGaussian() * 0.05F), player);
                                }
                            }
                        
                            // Set new project
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
