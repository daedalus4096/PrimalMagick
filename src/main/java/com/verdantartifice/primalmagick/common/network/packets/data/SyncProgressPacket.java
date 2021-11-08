package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.Knowledge;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchStage;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet to progress a research entry to its next stage on the server.
 * 
 * @author Daedalus4096
 */
public class SyncProgressPacket implements IMessageToServer {
    private static final Logger LOGGER = LogManager.getLogger();

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
    
    public static void encode(SyncProgressPacket message, FriendlyByteBuf buf) {
        buf.writeUtf(message.key.getRootKey());
        buf.writeBoolean(message.firstSync);
        buf.writeBoolean(message.runChecks);
        buf.writeBoolean(message.noFlags);
    }
    
    public static SyncProgressPacket decode(FriendlyByteBuf buf) {
        SyncProgressPacket message = new SyncProgressPacket();
        message.key = SimpleResearchKey.parse(buf.readUtf());
        message.firstSync = buf.readBoolean();
        message.runChecks = buf.readBoolean();
        message.noFlags = buf.readBoolean();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncProgressPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (message.key != null) {
                    Player player = ctx.get().getSender();
                    if (message.firstSync != message.key.isKnownBy(player)) {
                        // If called for, ensure that prerequisites for the next stage are checked and consumed
                        if (message.runChecks && !checkAndConsumePrerequisites(player, message.key)) {
                            return;
                        }
                        // Do the actual progression
                        LOGGER.debug("Progressing research {} for player {}", message.key.getRootKey(), player.getName().getString());
                        ResearchManager.progressResearch(player, message.key, true, !message.noFlags);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
        
        protected static boolean checkAndConsumePrerequisites(Player player, SimpleResearchKey key) {
            ResearchEntry entry = ResearchEntries.getEntry(key);
            if (entry == null || entry.getStages().isEmpty()) {
                return true;
            }

            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player).orElse(null);
            if (knowledge == null) {
                return false;
            }

            // Get the current stage of the given key's entry
            int currentStageNum = knowledge.getResearchStage(key);
            if (currentStageNum < 0) {
                return false;
            }
            if (currentStageNum >= entry.getStages().size()) {
                return true;
            }
            
            ResearchStage stage = entry.getStages().get(currentStageNum);
            boolean retVal = stage.arePrerequisitesMet(player);
            if (retVal) {
                // Consume the met prerequisite items and knowledge
                for (Object obj : stage.getMustObtain()) {
                    if (obj instanceof ItemStack) {
                        InventoryUtils.consumeItem(player, (ItemStack)obj);
                    } else if (obj instanceof ResourceLocation) {
                        InventoryUtils.consumeItem(player, (ResourceLocation)obj, 1);
                    }
                }
                for (Knowledge know : stage.getRequiredKnowledge()) {
                    ResearchManager.addKnowledge(player, know.getType(), -(know.getAmount() * know.getType().getProgression()));
                }
            }
            return retVal;
        }
    }
}
