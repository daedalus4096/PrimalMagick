package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.ScanResearchTrigger;
import com.verdantartifice.primalmagic.common.research.ScanSourceUnlockTrigger;
import com.verdantartifice.primalmagic.common.research.ScanTagResearchTrigger;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.stats.StatsPM;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

import net.minecraft.util.ResourceLocation;

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
        ResearchManager.registerScanTrigger(new ScanResearchTrigger(ItemsPM.MARBLE_RAW.get(), SimpleResearchKey.parse("RAW_MARBLE")));
        ResearchManager.registerScanTrigger(new ScanTagResearchTrigger(ItemTagsPM.NUGGETS_PRIMALITE, SimpleResearchKey.parse("b_scan_primalite"), false));
        ResearchManager.registerScanTrigger(new ScanTagResearchTrigger(ItemTagsPM.INGOTS_PRIMALITE, SimpleResearchKey.parse("b_scan_primalite"), false));
        ResearchManager.registerScanTrigger(new ScanTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE, SimpleResearchKey.parse("b_scan_primalite"), false));
        ResearchManager.registerScanTrigger(new ScanTagResearchTrigger(ItemTagsPM.NUGGETS_HEXIUM, SimpleResearchKey.parse("b_scan_hexium"), false));
        ResearchManager.registerScanTrigger(new ScanTagResearchTrigger(ItemTagsPM.INGOTS_HEXIUM, SimpleResearchKey.parse("b_scan_hexium"), false));
        ResearchManager.registerScanTrigger(new ScanTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_HEXIUM, SimpleResearchKey.parse("b_scan_hexium"), false));
        ResearchManager.registerScanTrigger(new ScanTagResearchTrigger(ItemTagsPM.NUGGETS_HALLOWSTEEL, SimpleResearchKey.parse("b_scan_hallowsteel"), false));
        ResearchManager.registerScanTrigger(new ScanTagResearchTrigger(ItemTagsPM.INGOTS_HALLOWSTEEL, SimpleResearchKey.parse("b_scan_hallowsteel"), false));
        ResearchManager.registerScanTrigger(new ScanTagResearchTrigger(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL, SimpleResearchKey.parse("b_scan_hallowsteel"), false));
    }
}
