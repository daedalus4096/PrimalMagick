package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
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
                if (ResearchManager.checkSyncSet(player.getUniqueID())) {
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
        
        // If the player is working on the Earth Source research, check if they're far enough down
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_EARTH@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_EARTH@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_earth");
            if (player.getPositionVec().y < 10.0D && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_earth").applyTextStyle(TextFormatting.GREEN), false);
            }
        }
        
        // If the player is working on the Sea Source research, check if they're in the ocean
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SEA@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SEA@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_sea");
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_sea").applyTextStyle(TextFormatting.GREEN), false);
            }
        }
        
        // If the player is working on the Sky Source research, check if they're high up enough
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SKY@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SKY@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_sky");
            if (player.getPositionVec().y > 100.0D && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_sky").applyTextStyle(TextFormatting.GREEN), false);
            }
        }
        
        // If the player is working on the Sun Source research, check if they're in the desert during the daytime
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SUN@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SUN@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_sun");
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && TimePhase.getSunPhase(player.world) == TimePhase.FULL && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_sun").applyTextStyle(TextFormatting.GREEN), false);
            }
        }
        
        // If the player is working on the Moon Source research, check if they're in the forest during the night-time
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_MOON@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_MOON@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_moon");
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) && TimePhase.getMoonPhase(player.world) == TimePhase.FULL && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_moon").applyTextStyle(TextFormatting.GREEN), false);
            }
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
            IPlayerAttunements attunements = PrimalMagicCapabilities.getAttunements(player);
            if (attunements != null) {
                attunements.sync(player);
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
            
            try {
                CompoundNBT nbtAttunements = PrimalMagicCapabilities.getAttunements(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getAttunements(event.getPlayer()).deserializeNBT(nbtAttunements);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} attunements", event.getOriginal().getName().getString());
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
    
    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player != null && !player.world.isRemote) {
            if ( ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("m_found_shrine")) &&
                 !ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("t_got_dream")) ) {
                // If the player is at the appropriate point of the FTUX, grant them the dream journal and research
                grantDreamJournal(player);
            }
        }
    }
    
    protected static void grantDreamJournal(PlayerEntity player) {
        // First grant the appropriate research entry to continue FTUX
        ResearchManager.completeResearch(player, SimpleResearchKey.parse("t_got_dream"));
        
        // Construct the dream journal item
        ItemStack journal = new ItemStack(Items.WRITTEN_BOOK);
        CompoundNBT contents = new CompoundNBT();
        contents.putInt("generation", 3);
        contents.putString("title", new TranslationTextComponent("primalmagic.dream_journal.title").getFormattedText());
        contents.putString("author", player.getName().getFormattedText());
        ListNBT pages = new ListNBT();
        pages.add(StringNBT.valueOf(new TranslationTextComponent("primalmagic.dream_journal.text.1").getFormattedText()));
        pages.add(StringNBT.valueOf(new TranslationTextComponent("primalmagic.dream_journal.text.2").getFormattedText()));
        pages.add(StringNBT.valueOf(new TranslationTextComponent("primalmagic.dream_journal.text.3").getFormattedText()));
        contents.put("pages", pages);
        journal.setTag(contents);
        
        // Give the dream journal to the player and announce it
        if (!player.addItemStackToInventory(journal)) {
            player.dropItem(journal, false);
        }
        player.sendMessage(new TranslationTextComponent("event.primalmagic.got_dream").applyTextStyle(TextFormatting.GREEN));
    }
}
