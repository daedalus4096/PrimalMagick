package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.ScanSourceUnlockTrigger;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.util.ResourceLocation;

/**
 * Point of registration for mod research data.
 * 
 * @author Daedalus4096
 */
public class InitResearch {
    public static void initResearch() {
        initDisciplines();
        initResearchFiles();
        ResearchManager.parseAllResearch();
        initScanResearch();
    }
    
    private static void initDisciplines() {
        ResearchDisciplines.registerDiscipline("BASICS", null, new ResourceLocation(PrimalMagic.MODID, "textures/item/grimoire.png"));
        ResearchDisciplines.registerDiscipline("ALCHEMY", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_ALCHEMY")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_alchemy.png"));
        ResearchDisciplines.registerDiscipline("SORCERY", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_SORCERY")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_sorcery.png"));
        ResearchDisciplines.registerDiscipline("MANAWEAVING", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_MANAWEAVING")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_manaweaving.png"));
        ResearchDisciplines.registerDiscipline("RUNEWORKING", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_RUNEWORKING")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_runeworking.png"));
        ResearchDisciplines.registerDiscipline("RITUAL", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_RITUAL")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_ritual.png"));
        ResearchDisciplines.registerDiscipline("MAGITECH", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_MAGITECH")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_magitech.png"));
    }
    
    private static void initResearchFiles() {
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/basics"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/alchemy"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/sorcery"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/manaweaving"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/runeworking"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/ritual"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/magitech"));
    }
    
    private static void initScanResearch() {
        ResearchManager.registerScanTrigger(new ScanSourceUnlockTrigger(ItemsPM.HALLOWED_ORB, Source.HALLOWED));
    }
}
