package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ScanEntityResearchTrigger;
import com.verdantartifice.primalmagick.common.research.ScanEntityTagResearchTrigger;
import com.verdantartifice.primalmagick.common.research.ScanItemResearchTrigger;
import com.verdantartifice.primalmagick.common.research.ScanItemTagResearchTrigger;
import com.verdantartifice.primalmagick.common.research.ScanSourceUnlockTrigger;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tags.CommonTags;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;
import com.verdantartifice.primalmagick.common.tags.ItemExtensionTags;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import net.minecraft.world.item.Items;

/**
 * Point of registration for mod research data.
 * 
 * @author Daedalus4096
 */
public class InitResearch {
    public static void initResearch() {
        initScanResearch();
    }
    
    private static void initScanResearch() {
        ResearchManager.registerScanTrigger(new ScanSourceUnlockTrigger(ItemsPM.HALLOWED_ORB.get(), Sources.HALLOWED));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWED_ORB.get(), ResearchEntries.SOTU_SCAN_HALLOWED_ORB));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_RAW.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_SLAB.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_STAIRS.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_WALL.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_BRICKS.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_BRICK_SLAB.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_BRICK_STAIRS.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_BRICK_WALL.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_PILLAR.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_CHISELED.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_RUNED.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_TILES.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_BOOKSHELF.get(), ResearchEntries.RAW_MARBLE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWED_ORB.get(), ResearchEntries.HALLOWED_ORB));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.HALLOWOOD_LOGS, ResearchEntries.HALLOWOOD_TREES));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWOOD_LEAVES.get(), ResearchEntries.HALLOWOOD_TREES));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWOOD_SAPLING.get(), ResearchEntries.HALLOWOOD_TREES));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.SUNWOOD_LOGS, ResearchEntries.SUNWOOD_TREES));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.SUNWOOD_LEAVES.get(), ResearchEntries.SUNWOOD_TREES));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.SUNWOOD_SAPLING.get(), ResearchEntries.SUNWOOD_TREES));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.MOONWOOD_LOGS, ResearchEntries.MOONWOOD_TREES));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MOONWOOD_LEAVES.get(), ResearchEntries.MOONWOOD_TREES));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MOONWOOD_SAPLING.get(), ResearchEntries.MOONWOOD_TREES));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.ROCK_SALT_ORE.get(), ResearchEntries.ROCK_SALT));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.ROCK_SALT.get(), ResearchEntries.ROCK_SALT));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.REFINED_SALT.get(), ResearchEntries.ROCK_SALT));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.NUGGETS_PRIMALITE, ResearchEntries.SCAN_PRIMALITE, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.INGOTS_PRIMALITE, ResearchEntries.SCAN_PRIMALITE, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE, ResearchEntries.SCAN_PRIMALITE, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.NUGGETS_HEXIUM, ResearchEntries.SCAN_HEXIUM, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.INGOTS_HEXIUM, ResearchEntries.SCAN_HEXIUM, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_HEXIUM, ResearchEntries.SCAN_HEXIUM, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.NUGGETS_HALLOWSTEEL, ResearchEntries.SCAN_HALLOWSTEEL, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.INGOTS_HALLOWSTEEL, ResearchEntries.SCAN_HALLOWSTEEL, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL, ResearchEntries.SCAN_HALLOWSTEEL, false));
        ResearchManager.registerScanTrigger(new ScanEntityTagResearchTrigger(EntityTypeTagsPM.FLYING_CREATURES, ResearchEntries.SCAN_FLYING_CREATURE, false));
        ResearchManager.registerScanTrigger(new ScanEntityTagResearchTrigger(EntityTypeTagsPM.GOLEMS, ResearchEntries.SCAN_GOLEM, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(CommonTags.Items.NETHER_STARS, ResearchEntries.SCAN_NETHER_STAR, false));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.ALCHEMICAL_WASTE.get(), ResearchEntries.ALCHEMICAL_WASTE));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(CommonTags.Items.GEMS_QUARTZ, ResearchEntries.QUARTZ));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(CommonTags.Items.ORES_QUARTZ, ResearchEntries.QUARTZ));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.QUARTZ_BLOCK, ResearchEntries.QUARTZ));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemExtensionTags.NUGGETS_QUARTZ, ResearchEntries.QUARTZ));
        ResearchManager.registerScanTrigger(new ScanEntityResearchTrigger(EntityTypesPM.INNER_DEMON.get(), ResearchEntries.INNER_DEMON));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(CommonTags.Items.BOOKSHELVES, ResearchEntries.BOOKSHELF));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.BEEHIVE, ResearchEntries.BEEHIVE));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.BEACON, ResearchEntries.BEACON));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.DRAGON_EGG, ResearchEntries.DRAGON_EGG));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.DRAGON_HEAD, ResearchEntries.DRAGON_HEAD));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MYSTICAL_RELIC.get(), ResearchEntries.MYSTICAL_RELIC));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), ResearchEntries.MYSTICAL_RELIC));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get(), ResearchEntries.HUMMING_ARTIFACT));
        ResearchManager.registerScanTrigger(new ScanEntityResearchTrigger(EntityTypesPM.TREEFOLK.get(), ResearchEntries.TREEFOLK));
    }
}
