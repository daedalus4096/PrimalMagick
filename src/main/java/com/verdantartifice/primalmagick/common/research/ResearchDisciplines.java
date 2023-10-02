package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.resources.ResourceLocation;

/**
 * Collection of all defined research disciplines and their defining JSON data files.
 * 
 * @author Daedalus4096
 */
public class ResearchDisciplines {
    protected static final Map<String, ResearchDiscipline> DISCIPLINES = new HashMap<>();
    protected static final List<ResearchDiscipline> DISCIPLINES_SORTED = new ArrayList<>();
    
    public static final ResearchDiscipline BASICS = registerDiscipline("BASICS", null, PrimalMagick.resource("textures/item/grimoire.png"), null);
    public static final ResearchDiscipline MANAWEAVING = registerDiscipline("MANAWEAVING", ResearchNames.UNLOCK_MANAWEAVING.get().compoundKey(), PrimalMagick.resource("textures/research/discipline_manaweaving.png"), StatsPM.CRAFTED_MANAWEAVING);
    public static final ResearchDiscipline ALCHEMY = registerDiscipline("ALCHEMY", ResearchNames.UNLOCK_ALCHEMY.get().compoundKey(), PrimalMagick.resource("textures/research/discipline_alchemy.png"), StatsPM.CRAFTED_ALCHEMY);
    public static final ResearchDiscipline SORCERY = registerDiscipline("SORCERY", ResearchNames.UNLOCK_SORCERY.get().compoundKey(), PrimalMagick.resource("textures/research/discipline_sorcery.png"), StatsPM.CRAFTED_SORCERY);
    public static final ResearchDiscipline RUNEWORKING = registerDiscipline("RUNEWORKING", ResearchNames.UNLOCK_RUNEWORKING.get().compoundKey(), PrimalMagick.resource("textures/research/discipline_runeworking.png"), StatsPM.CRAFTED_RUNEWORKING);
    public static final ResearchDiscipline RITUAL = registerDiscipline("RITUAL", ResearchNames.UNLOCK_RITUAL.get().compoundKey(), PrimalMagick.resource("textures/research/discipline_ritual.png"), StatsPM.CRAFTED_RITUAL);
    public static final ResearchDiscipline MAGITECH = registerDiscipline("MAGITECH", ResearchNames.UNLOCK_MAGITECH.get().compoundKey(), PrimalMagick.resource("textures/research/discipline_magitech.png"), StatsPM.CRAFTED_MAGITECH);
    public static final ResearchDiscipline SCANS = registerDiscipline("SCANS", ResearchNames.UNLOCK_SCANS.get().compoundKey(), PrimalMagick.resource("textures/item/magnifying_glass.png"), null);
    
    @Nullable
    public static ResearchDiscipline getDiscipline(String key) {
        return DISCIPLINES.get(key);
    }
    
    @Nonnull
    public static Collection<ResearchDiscipline> getAllDisciplines() {
        return Collections.unmodifiableCollection(DISCIPLINES.values());
    }
    
    @Nonnull
    public static List<ResearchDiscipline> getAllDisciplinesSorted() {
        return Collections.unmodifiableList(DISCIPLINES_SORTED);
    }
    
    @Nullable
    public static ResearchDiscipline registerDiscipline(@Nullable String key, @Nullable CompoundResearchKey unlockResearchKey, @Nullable ResourceLocation icon, @Nullable Stat craftingStat) {
        if (key == null || DISCIPLINES.containsKey(key)) {
            // Don't allow null or duplicate disciplines in the collection
            return null;
        } else {
            ResearchDiscipline discipline = ResearchDiscipline.create(key, unlockResearchKey, icon, craftingStat);
            if (discipline != null) {
                DISCIPLINES.put(key, discipline);
                DISCIPLINES_SORTED.add(discipline);
            }
            return discipline;
        }
    }
    
    static void clearAllResearch() {
        for (ResearchDiscipline discipline : DISCIPLINES.values()) {
            discipline.clearEntries();
        }
    }
}
