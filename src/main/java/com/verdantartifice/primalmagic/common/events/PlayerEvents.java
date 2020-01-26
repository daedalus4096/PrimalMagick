package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for player related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class PlayerEvents {
    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntity().world.isRemote && (event.getEntity() instanceof ServerPlayerEntity)) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            if (player.ticksExisted % 20 == 0) {
                // Check to see if any players need their research/knowledge synced to their clients
                if (ResearchManager.popSyncList(player.getName().getString()) != null) {
                    IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                    if (knowledge != null) {
                        knowledge.sync(player);
                    }
                }
            }
            if (player.ticksExisted % 200 == 0) {
                // Periodically check for environmentally-triggered research entries
                checkEnvironmentalResearch(player);
            }
        }
    }
    
    protected static void checkEnvironmentalResearch(ServerPlayerEntity player) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null || !knowledge.isResearchKnown(SimpleResearchKey.parse("FIRST_STEPS"))) {
            // Only check environmental research if the player has started progression
            return;
        }
        
        Biome biome = player.world.getBiome(player.getPosition());
        if (!knowledge.isResearchKnown(Source.INFERNAL.getDiscoverKey()) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
            // If the player is in a Nether-based biome, discover the Infernal source
            ResearchManager.completeResearch(player, Source.INFERNAL.getDiscoverKey());
            player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.discover_source.infernal").applyTextStyle(TextFormatting.GREEN), false);
        }
        if (!knowledge.isResearchKnown(Source.VOID.getDiscoverKey()) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.END)) {
            // If the player is in an End-based biome, discover the Void source
            ResearchManager.completeResearch(player, Source.VOID.getDiscoverKey());
            player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.discover_source.void").applyTextStyle(TextFormatting.GREEN), false);
        }
    }
    
    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && (event.getEntity() instanceof ServerPlayerEntity)) {
            // When a player first joins a world, sync that player's capabilities to their client
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge != null) {
                knowledge.sync(player);
            }
            IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
            if (cooldowns != null) {
                cooldowns.sync(player);
            }
            IPlayerStats stats = PrimalMagicCapabilities.getStats(player);
            if (stats != null) {
                stats.sync(player);
            }
        }
    }
    
    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        // Preserve player capability data between deaths
        if (event.isWasDeath()) {
            try {
                CompoundNBT nbtKnowledge = PrimalMagicCapabilities.getKnowledge(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getKnowledge(event.getPlayer()).deserializeNBT(nbtKnowledge);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} knowledge", event.getOriginal().getName().getString());
            }
            
            try {
                CompoundNBT nbtCooldowns = PrimalMagicCapabilities.getCooldowns(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getCooldowns(event.getPlayer()).deserializeNBT(nbtCooldowns);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} cooldowns", event.getOriginal().getName().getString());
            }
            
            try {
                CompoundNBT nbtStats = PrimalMagicCapabilities.getStats(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getStats(event.getPlayer()).deserializeNBT(nbtStats);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} stats", event.getOriginal().getName().getString());
            }
        }
    }
    
    @SubscribeEvent
    public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().world.isRemote) {
            ItemStack stack = event.getCrafting().copy();
            int stackHash = ItemUtils.getHashCode(stack);
            
            // If a research entry requires crafting the item that was just crafted, grant the appropriate research
            if (ResearchManager.getAllCraftingReferences().contains(Integer.valueOf(stackHash))) {
                ResearchManager.completeResearch(event.getPlayer(), SimpleResearchKey.parse("[#]" + stackHash));
            }
            
            // If a research entry requires crafting the a tag containing the item that was just crafted, grant the appropriate research
            for (ResourceLocation tag : stack.getItem().getTags()) {
                if (tag != null) {
                    int tagHash = ("tag:" + tag.toString()).hashCode();
                    if (ResearchManager.getAllCraftingReferences().contains(Integer.valueOf(tagHash))) {
                        ResearchManager.completeResearch(event.getPlayer(), SimpleResearchKey.parse("[#]" + tagHash));
                    }
                }
            }
        }
    }
}
