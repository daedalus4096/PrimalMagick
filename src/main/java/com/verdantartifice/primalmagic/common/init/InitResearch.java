package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.util.ResourceLocation;

public class InitResearch {
    public static void initResearch() {
        initDisciplines();
        initResearchFiles();
        ResearchManager.parseAllResearch();
    }
    
    private static void initDisciplines() {
        ResearchDisciplines.registerDiscipline("BASICS", null);
        ResearchDisciplines.registerDiscipline("ALCHEMY", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_ALCHEMY")));
        ResearchDisciplines.registerDiscipline("SORCERY", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_SORCERY")));
        ResearchDisciplines.registerDiscipline("ENCHANTMENT", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_ENCHANTMENT")));
        ResearchDisciplines.registerDiscipline("RUNEWORKING", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_RUNEWORKING")));
        ResearchDisciplines.registerDiscipline("RITUAL", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_RITUAL")));
        ResearchDisciplines.registerDiscipline("MAGITECH", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_MAGITECH")));
    }
    
    private static void initResearchFiles() {
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/basics"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/alchemy"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/sorcery"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/enchantment"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/runeworking"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/ritual"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/magitech"));
    }
}
