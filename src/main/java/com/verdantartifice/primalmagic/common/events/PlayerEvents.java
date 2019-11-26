package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
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

@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class PlayerEvents {
    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntity().world.isRemote && (event.getEntity() instanceof ServerPlayerEntity)) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            if (player.ticksExisted % 20 == 0) {
                if (ResearchManager.popSyncList(player.getName().getString()) != null) {
                    IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                    if (knowledge != null) {
                        knowledge.sync(player);
                    }
                }
            }
            if (player.ticksExisted % 200 == 0) {
                checkEnvironmentalResearch(player);
            }
        }
    }
    
    protected static void checkEnvironmentalResearch(ServerPlayerEntity player) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null || !knowledge.isResearchKnown(SimpleResearchKey.parse("FIRST_STEPS"))) {
            return;
        }
        
        Biome biome = player.world.getBiome(player.getPosition());
        if (!knowledge.isResearchKnown(Source.INFERNAL.getDiscoverKey()) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
            knowledge.addResearch(Source.INFERNAL.getDiscoverKey());
            knowledge.sync(player);
            player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.discover_source.infernal").applyTextStyle(TextFormatting.GREEN), true);
        }
        if (!knowledge.isResearchKnown(Source.VOID.getDiscoverKey()) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.END)) {
            knowledge.addResearch(Source.VOID.getDiscoverKey());
            knowledge.sync(player);
            player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.discover_source.void").applyTextStyle(TextFormatting.GREEN), true);
        }
    }
    
    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && (event.getEntity() instanceof ServerPlayerEntity)) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge != null) {
                knowledge.sync(player);
            }
        }
    }
    
    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            try {
                CompoundNBT nbtKnowledge = PrimalMagicCapabilities.getKnowledge(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getKnowledge(event.getPlayer()).deserializeNBT(nbtKnowledge);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} knowledge", event.getOriginal().getName().getString());
            }
        }
    }
    
    @SubscribeEvent
    public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().world.isRemote) {
            ItemStack stack = event.getCrafting().copy();
            int stackHash = ItemUtils.getHashCode(stack);
            if (ResearchManager.getAllCraftingReferences().contains(Integer.valueOf(stackHash))) {
                ResearchManager.completeResearch(event.getPlayer(), SimpleResearchKey.parse("[#]" + stackHash));
            }
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
