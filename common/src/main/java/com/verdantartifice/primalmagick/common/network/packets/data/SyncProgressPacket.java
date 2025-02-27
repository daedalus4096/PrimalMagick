package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchStage;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Packet to progress a research entry to its next stage on the server.
 * 
 * @author Daedalus4096
 */
public class SyncProgressPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("sync_progress");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncProgressPacket> STREAM_CODEC = StreamCodec.ofMember(SyncProgressPacket::encode, SyncProgressPacket::decode);

    private static final Logger LOGGER = LogManager.getLogger();

    protected final ResearchEntryKey key;
    protected final boolean firstSync;
    protected final boolean runChecks;
    protected final boolean noFlags;
    protected final boolean noPopups;
    
    public SyncProgressPacket(ResearchEntryKey key, boolean firstSync, boolean runChecks, boolean noFlags, boolean noPopups) {
        this.key = key;
        this.firstSync = firstSync;
        this.runChecks = runChecks;
        this.noFlags = noFlags;
        this.noPopups = noPopups;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SyncProgressPacket message, RegistryFriendlyByteBuf buf) {
        message.key.toNetwork(buf);
        buf.writeBoolean(message.firstSync);
        buf.writeBoolean(message.runChecks);
        buf.writeBoolean(message.noFlags);
        buf.writeBoolean(message.noPopups);
    }
    
    public static SyncProgressPacket decode(RegistryFriendlyByteBuf buf) {
        return new SyncProgressPacket(ResearchEntryKey.fromNetwork(buf), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }
    
    public static void onMessage(PacketContext<SyncProgressPacket> ctx) {
        SyncProgressPacket message = ctx.message();
        if (message.key != null) {
            Player player = ctx.sender();
            if (message.firstSync != ResearchManager.isResearchStarted(player, message.key)) {
                // If called for, ensure that prerequisites for the next stage are checked and consumed
                if (message.runChecks && !checkAndConsumePrerequisites(player, message.key)) {
                    LOGGER.debug("Requirements not met for research {} by player {}", message.key.getRootKey().location(), player.getName().getString());
                    return;
                }
                
                // Do the actual progression
                LOGGER.debug("Progressing research {} for player {}", message.key.getRootKey().location(), player.getName().getString());
                ResearchManager.progressResearch(player, message.key, true, !message.noFlags, !message.noPopups);
            }
        }
    }
    
    protected static boolean checkAndConsumePrerequisites(Player player, ResearchEntryKey key) {
        RegistryAccess registryAccess = player.level().registryAccess();
        ResearchEntry entry = ResearchEntries.getEntry(registryAccess, key);
        if (entry == null || entry.stages().isEmpty()) {
            return true;
        }

        IPlayerKnowledge knowledge = Services.CAPABILITIES.knowledge(player).orElse(null);
        if (knowledge == null) {
            return false;
        }

        // Get the current stage of the given key's entry
        int currentStageNum = knowledge.getResearchStage(key);
        if (currentStageNum < 0) {
            return false;
        }
        if (currentStageNum >= entry.stages().size()) {
            return true;
        }
        
        ResearchStage stage = entry.stages().get(currentStageNum);
        boolean retVal = stage.arePrerequisitesMet(player);
        if (retVal) {
            // Consume the met prerequisite items and knowledge
            stage.completionRequirementOpt().ifPresent(req -> req.consumeComponents(player));
        }
        return retVal;
    }
}
