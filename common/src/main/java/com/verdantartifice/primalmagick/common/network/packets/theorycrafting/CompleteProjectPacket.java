package com.verdantartifice.primalmagick.common.network.packets.theorycrafting;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;

/**
 * Packet sent to complete a research project on the server in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class CompleteProjectPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("complete_project");
    public static final StreamCodec<RegistryFriendlyByteBuf, CompleteProjectPacket> STREAM_CODEC = StreamCodec.ofMember(CompleteProjectPacket::encode, CompleteProjectPacket::decode);

    protected final int windowId;

    public CompleteProjectPacket(int windowId) {
        this.windowId = windowId;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(CompleteProjectPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
    }
    
    public static CompleteProjectPacket decode(RegistryFriendlyByteBuf buf) {
        return new CompleteProjectPacket(buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<CompleteProjectPacket> ctx) {
        CompleteProjectPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            // Consume paper and ink
            if (player.containerMenu instanceof ResearchTableMenu researchMenu && researchMenu.containerId == message.windowId) {
                researchMenu.consumeWritingImplements();
                researchMenu.getContainerLevelAccess().execute((world, blockPos) -> {
                    // Determine if current project is a success
                    Project project = knowledge.getActiveResearchProject();
                    RandomSource rand = player.getRandom();
                    if (project != null && project.isSatisfied(player, TheorycraftManager.getSurroundings(world, blockPos)) && project.consumeSelectedMaterials(player)) {
                        if (rand.nextDouble() < project.getSuccessChance()) {
                            ResearchManager.addKnowledge(player, KnowledgeType.THEORY, project.getTheoryPointReward());
                            project.otherRewards().forEach(reward -> reward.grant(player));
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
    }
}
