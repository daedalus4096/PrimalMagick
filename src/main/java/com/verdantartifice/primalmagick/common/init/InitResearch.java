package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ScanEntityResearchTrigger;
import com.verdantartifice.primalmagick.common.research.ScanEntityTagResearchTrigger;
import com.verdantartifice.primalmagick.common.research.ScanItemResearchTrigger;
import com.verdantartifice.primalmagick.common.research.ScanItemTagResearchTrigger;
import com.verdantartifice.primalmagick.common.research.ScanSourceUnlockTrigger;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsForgeExt;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

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
        ResearchManager.registerScanTrigger(new ScanSourceUnlockTrigger(ItemsPM.HALLOWED_ORB.get(), Source.HALLOWED));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWED_ORB.get(), SimpleResearchKey.find("b_sotu_scan_hallowed_orb").orElseThrow()));
        SimpleResearchKey rawMarble = SimpleResearchKey.find("RAW_MARBLE").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_RAW.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_SLAB.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_STAIRS.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_WALL.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_BRICKS.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_BRICK_SLAB.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_BRICK_STAIRS.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_BRICK_WALL.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_PILLAR.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_CHISELED.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_RUNED.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MARBLE_TILES.get(), rawMarble));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWED_ORB.get(), SimpleResearchKey.find("HALLOWED_ORB").orElseThrow()));
        SimpleResearchKey hallowood = SimpleResearchKey.find("HALLOWOOD_TREES").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.HALLOWOOD_LOGS, hallowood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWOOD_LEAVES.get(), hallowood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWOOD_SAPLING.get(), hallowood));
        SimpleResearchKey sunwood = SimpleResearchKey.find("SUNWOOD_TREES").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.SUNWOOD_LOGS, sunwood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.SUNWOOD_LEAVES.get(), sunwood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.SUNWOOD_SAPLING.get(), sunwood));
        SimpleResearchKey moonwood = SimpleResearchKey.find("MOONWOOD_TREES").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.MOONWOOD_LOGS, moonwood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MOONWOOD_LEAVES.get(), moonwood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MOONWOOD_SAPLING.get(), moonwood));
        SimpleResearchKey rockSalt = SimpleResearchKey.find("ROCK_SALT").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.ROCK_SALT_ORE.get(), rockSalt));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.ROCK_SALT.get(), rockSalt));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.REFINED_SALT.get(), rockSalt));
        SimpleResearchKey primalite = SimpleResearchKey.find("b_scan_primalite").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.NUGGETS_PRIMALITE, primalite, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.INGOTS_PRIMALITE, primalite, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE, primalite, false));
        SimpleResearchKey hexium = SimpleResearchKey.find("b_scan_hexium").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.NUGGETS_HEXIUM, hexium, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.INGOTS_HEXIUM, hexium, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_HEXIUM, hexium, false));
        SimpleResearchKey hallowsteel = SimpleResearchKey.find("b_scan_hallowsteel").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.NUGGETS_HALLOWSTEEL, hallowsteel, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.INGOTS_HALLOWSTEEL, hallowsteel, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL, hallowsteel, false));
        ResearchManager.registerScanTrigger(new ScanEntityTagResearchTrigger(EntityTypeTagsPM.FLYING_CREATURES, SimpleResearchKey.find("t_flying_creature").orElseThrow(), false));
        ResearchManager.registerScanTrigger(new ScanEntityTagResearchTrigger(EntityTypeTagsPM.GOLEMS, SimpleResearchKey.find("t_golem").orElseThrow(), false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.NETHER_STARS, SimpleResearchKey.find("b_scan_nether_star").orElseThrow(), false));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.ALCHEMICAL_WASTE.get(), SimpleResearchKey.find("ALCHEMICAL_WASTE").orElseThrow()));
        SimpleResearchKey quartz = SimpleResearchKey.find("QUARTZ").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.GEMS_QUARTZ, quartz));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.ORES_QUARTZ, quartz));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.STORAGE_BLOCKS_QUARTZ, quartz));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsForgeExt.NUGGETS_QUARTZ, quartz));
        ResearchManager.registerScanTrigger(new ScanEntityResearchTrigger(EntityTypesPM.INNER_DEMON.get(), SimpleResearchKey.find("INNER_DEMON").orElseThrow()));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.BOOKSHELVES, SimpleResearchKey.find("BOOKSHELF").orElseThrow()));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.BEEHIVE, SimpleResearchKey.find("BEEHIVE").orElseThrow()));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.BEACON, SimpleResearchKey.find("BEACON").orElseThrow()));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.DRAGON_EGG, SimpleResearchKey.find("DRAGON_EGG").orElseThrow()));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.DRAGON_HEAD, SimpleResearchKey.find("DRAGON_HEAD").orElseThrow()));
        SimpleResearchKey relic = SimpleResearchKey.find("MYSTICAL_RELIC").orElseThrow();
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MYSTICAL_RELIC.get(), relic));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), relic));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get(), SimpleResearchKey.find("HUMMING_ARTIFACT").orElseThrow()));
        ResearchManager.registerScanTrigger(new ScanEntityResearchTrigger(EntityTypesPM.TREEFOLK.get(), SimpleResearchKey.find("TREEFOLK").orElseThrow()));
    }
}
