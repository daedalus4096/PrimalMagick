package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.research.ResearchEntries;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.ResearchStage;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncProgressPacket implements IMessageToServer {
    protected SimpleResearchKey key;
    protected boolean firstSync;
    protected boolean runChecks;
    protected boolean noFlags;
    
    public SyncProgressPacket() {
        this.key = null;
        this.firstSync = false;
        this.runChecks = false;
        this.noFlags = false;
    }
    
    public SyncProgressPacket(SimpleResearchKey key, boolean firstSync, boolean runChecks, boolean noFlags) {
        this.key = key;
        this.firstSync = firstSync;
        this.runChecks = runChecks;
        this.noFlags = noFlags;
    }
    
    public static void encode(SyncProgressPacket message, PacketBuffer buf) {
        buf.writeString(message.key.getRootKey());
        buf.writeBoolean(message.firstSync);
        buf.writeBoolean(message.runChecks);
        buf.writeBoolean(message.noFlags);
    }
    
    public static SyncProgressPacket decode(PacketBuffer buf) {
        SyncProgressPacket message = new SyncProgressPacket();
        message.key = SimpleResearchKey.parse(buf.readString());
        message.firstSync = buf.readBoolean();
        message.runChecks = buf.readBoolean();
        message.noFlags = buf.readBoolean();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncProgressPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if (message.key != null) {
                    PlayerEntity player = ctx.get().getSender();
                    if (message.firstSync != message.key.isKnownBy(player)) {
                        if (message.runChecks && !checkAndConsumePrerequisites(player, message.key)) {
                            return;
                        }
                        if (message.noFlags) {
                            ResearchManager.noFlags = true;
                        }
                        // Do the actual progression
                        PrimalMagic.LOGGER.info("Progressing research {} for player {}", message.key.getRootKey(), player.getName().getString());
                        ResearchManager.progressResearch(player, message.key);
                    }
                }
            });
        }
        
        protected static boolean checkAndConsumePrerequisites(PlayerEntity player, SimpleResearchKey key) {
            ResearchEntry entry = ResearchEntries.getEntry(key);
            if (entry == null || entry.getStages().isEmpty()) {
                return true;
            }

            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge == null) {
                return false;
            }

            // Get the current stage of the given key's entry
            int currentStageNum = knowledge.getResearchStage(key) - 1;  // Remember, it's one-based
            if (currentStageNum < 0) {
                return false;
            }
            if (currentStageNum >= entry.getStages().size()) {
                return true;
            }
            
            ResearchStage stage = entry.getStages().get(currentStageNum);
            boolean retVal = stage.arePrerequisitesMet(player);
            if (retVal) {
                // TODO Consume the met prerequisite items and knowledge
            }
            return retVal;
        }
    }
}
