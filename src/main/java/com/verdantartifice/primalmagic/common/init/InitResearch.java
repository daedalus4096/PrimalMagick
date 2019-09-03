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
    }
    
    private static void initResearchFiles() {
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/basics"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/alchemy"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/sorcery"));
    }
}
