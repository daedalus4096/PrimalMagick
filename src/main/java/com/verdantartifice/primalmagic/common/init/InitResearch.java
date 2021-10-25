package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.ScanEntityResearchTrigger;
import com.verdantartifice.primalmagic.common.research.ScanEntityTagResearchTrigger;
import com.verdantartifice.primalmagic.common.research.ScanItemResearchTrigger;
import com.verdantartifice.primalmagic.common.research.ScanItemTagResearchTrigger;
import com.verdantartifice.primalmagic.common.research.ScanSourceUnlockTrigger;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.stats.StatsPM;
import com.verdantartifice.primalmagic.common.tags.EntityTypeTagsPM;
import com.verdantartifice.primalmagic.common.tags.ItemTagsForgeExt;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

/**
 * Point of registration for mod research data.
 * 
 * @author Daedalus4096
 */
public class InitResearch {
    public static void initResearch() {
        initDisciplines();
        initScanResearch();
    }
    
    private static void initDisciplines() {
        ResearchDisciplines.registerDiscipline("BASICS", null, new ResourceLocation(PrimalMagic.MODID, "textures/item/grimoire.png"), null);
        ResearchDisciplines.registerDiscipline("ALCHEMY", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_ALCHEMY")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_alchemy.png"), StatsPM.CRAFTED_ALCHEMY);
        ResearchDisciplines.registerDiscipline("SORCERY", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_SORCERY")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_sorcery.png"), StatsPM.CRAFTED_SORCERY);
        ResearchDisciplines.registerDiscipline("MANAWEAVING", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_MANAWEAVING")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_manaweaving.png"), StatsPM.CRAFTED_MANAWEAVING);
        ResearchDisciplines.registerDiscipline("RUNEWORKING", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_RUNEWORKING")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_runeworking.png"), StatsPM.CRAFTED_RUNEWORKING);
        ResearchDisciplines.registerDiscipline("RITUAL", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_RITUAL")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_ritual.png"), StatsPM.CRAFTED_RITUAL);
        ResearchDisciplines.registerDiscipline("MAGITECH", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_MAGITECH")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_magitech.png"), StatsPM.CRAFTED_MAGITECH);
        ResearchDisciplines.registerDiscipline("SCANS", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_SCANS")), new ResourceLocation(PrimalMagic.MODID, "textures/item/magnifying_glass.png"), null);
    }
    
    private static void initScanResearch() {
        ResearchManager.registerScanTrigger(new ScanSourceUnlockTrigger(ItemsPM.HALLOWED_ORB.get(), Source.HALLOWED));
        SimpleResearchKey rawMarble = SimpleResearchKey.parse("RAW_MARBLE");
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
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWED_ORB.get(), SimpleResearchKey.parse("HALLOWED_ORB")));
        SimpleResearchKey hallowood = SimpleResearchKey.parse("HALLOWOOD_TREES");
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.HALLOWOOD_LOGS, hallowood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWOOD_LEAVES.get(), hallowood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.HALLOWOOD_SAPLING.get(), hallowood));
        SimpleResearchKey sunwood = SimpleResearchKey.parse("SUNWOOD_TREES");
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.SUNWOOD_LOGS, sunwood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.SUNWOOD_LEAVES.get(), sunwood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.SUNWOOD_SAPLING.get(), sunwood));
        SimpleResearchKey moonwood = SimpleResearchKey.parse("MOONWOOD_TREES");
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.MOONWOOD_LOGS, moonwood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MOONWOOD_LEAVES.get(), moonwood));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MOONWOOD_SAPLING.get(), moonwood));
        SimpleResearchKey rockSalt = SimpleResearchKey.parse("ROCK_SALT");
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.ROCK_SALT_ORE.get(), rockSalt));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.ROCK_SALT.get(), rockSalt));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.REFINED_SALT.get(), rockSalt));
        SimpleResearchKey primalite = SimpleResearchKey.parse("b_scan_primalite");
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.NUGGETS_PRIMALITE, primalite, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.INGOTS_PRIMALITE, primalite, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE, primalite, false));
        SimpleResearchKey hexium = SimpleResearchKey.parse("b_scan_hexium");
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.NUGGETS_HEXIUM, hexium, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.INGOTS_HEXIUM, hexium, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_HEXIUM, hexium, false));
        SimpleResearchKey hallowsteel = SimpleResearchKey.parse("b_scan_hallowsteel");
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.NUGGETS_HALLOWSTEEL, hallowsteel, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.INGOTS_HALLOWSTEEL, hallowsteel, false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL, hallowsteel, false));
        ResearchManager.registerScanTrigger(new ScanEntityTagResearchTrigger(EntityTypeTagsPM.FLYING_CREATURES, SimpleResearchKey.parse("t_flying_creature"), false));
        ResearchManager.registerScanTrigger(new ScanEntityTagResearchTrigger(EntityTypeTagsPM.GOLEMS, SimpleResearchKey.parse("t_golem"), false));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.NETHER_STARS, SimpleResearchKey.parse("b_scan_nether_star"), false));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.ALCHEMICAL_WASTE.get(), SimpleResearchKey.parse("ALCHEMICAL_WASTE")));
        SimpleResearchKey quartz = SimpleResearchKey.parse("QUARTZ");
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.GEMS_QUARTZ, quartz));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.ORES_QUARTZ, quartz));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.STORAGE_BLOCKS_QUARTZ, quartz));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(ItemTagsForgeExt.NUGGETS_QUARTZ, quartz));
        ResearchManager.registerScanTrigger(new ScanEntityResearchTrigger(EntityTypesPM.INNER_DEMON.get(), SimpleResearchKey.parse("INNER_DEMON")));
        ResearchManager.registerScanTrigger(new ScanItemTagResearchTrigger(Tags.Items.BOOKSHELVES, SimpleResearchKey.parse("BOOKSHELF")));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.BEEHIVE, SimpleResearchKey.parse("BEEHIVE")));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.BEACON, SimpleResearchKey.parse("BEACON")));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(Items.DRAGON_EGG, SimpleResearchKey.parse("DRAGON_EGG")));
        SimpleResearchKey relic = SimpleResearchKey.parse("MYSTICAL_RELIC");
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MYSTICAL_RELIC.get(), relic));
        ResearchManager.registerScanTrigger(new ScanItemResearchTrigger(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), relic));
    }
}
