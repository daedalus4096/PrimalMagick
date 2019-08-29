package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;

public class InitResearch {
    public static void initResearch() {
        initDisciplines();
    }
    
    private static void initDisciplines() {
        ResearchDisciplines.registerDiscipline("BASICS", null);
    }
}
