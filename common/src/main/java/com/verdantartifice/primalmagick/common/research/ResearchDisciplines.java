package com.verdantartifice.primalmagick.common.research;

import java.util.List;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

/**
 * Collection of all defined research disciplines and their defining JSON data files.
 * 
 * @author Daedalus4096
 */
public class ResearchDisciplines {
    public static final ResourceKey<ResearchDiscipline> BASICS = create("basics");
    public static final ResourceKey<ResearchDiscipline> MANAWEAVING = create("manaweaving");
    public static final ResourceKey<ResearchDiscipline> ALCHEMY = create("alchemy");
    public static final ResourceKey<ResearchDiscipline> SORCERY = create("sorcery");
    public static final ResourceKey<ResearchDiscipline> RUNEWORKING = create("runeworking");
    public static final ResourceKey<ResearchDiscipline> RITUAL = create("ritual");
    public static final ResourceKey<ResearchDiscipline> MAGITECH = create("magitech");
    public static final ResourceKey<ResearchDiscipline> SCANS = create("scans");
    
    public static ResourceKey<ResearchDiscipline> create(String name) {
        return ResourceKey.create(RegistryKeysPM.RESEARCH_DISCIPLINES, ResourceUtils.loc(name));
    }
    
    public static void bootstrap(BootstrapContext<ResearchDiscipline> context) {
        context.register(BASICS, ResearchDiscipline.builder(BASICS).icon(ResourceUtils.loc("textures/item/grimoire.png")).indexSortOrder(100).build());
        context.register(MANAWEAVING, ResearchDiscipline.builder(MANAWEAVING).unlock(ResearchEntries.UNLOCK_MANAWEAVING).icon(ResourceUtils.loc("textures/research/discipline_manaweaving.png"))
                .craftingStat(StatsPM.CRAFTED_MANAWEAVING).expertiseStat(StatsPM.EXPERTISE_MANAWEAVING).indexSortOrder(200).build());
        context.register(ALCHEMY, ResearchDiscipline.builder(ALCHEMY).unlock(ResearchEntries.UNLOCK_ALCHEMY).icon(ResourceUtils.loc("textures/research/discipline_alchemy.png"))
                .craftingStat(StatsPM.CRAFTED_ALCHEMY).expertiseStat(StatsPM.EXPERTISE_ALCHEMY).indexSortOrder(300).build());
        context.register(SORCERY, ResearchDiscipline.builder(SORCERY).unlock(ResearchEntries.UNLOCK_SORCERY).icon(ResourceUtils.loc("textures/research/discipline_sorcery.png"))
                .craftingStat(StatsPM.CRAFTED_SORCERY).expertiseStat(StatsPM.EXPERTISE_SORCERY).indexSortOrder(400).build());
        context.register(RUNEWORKING, ResearchDiscipline.builder(RUNEWORKING).unlock(ResearchEntries.UNLOCK_RUNEWORKING).icon(ResourceUtils.loc("textures/research/discipline_runeworking.png"))
                .craftingStat(StatsPM.CRAFTED_RUNEWORKING).expertiseStat(StatsPM.EXPERTISE_RUNEWORKING).indexSortOrder(500).build());
        context.register(RITUAL, ResearchDiscipline.builder(RITUAL).unlock(ResearchEntries.UNLOCK_RITUAL).icon(ResourceUtils.loc("textures/research/discipline_ritual.png"))
                .craftingStat(StatsPM.CRAFTED_RITUAL).expertiseStat(StatsPM.EXPERTISE_RITUAL).indexSortOrder(600).build());
        context.register(MAGITECH, ResearchDiscipline.builder(MAGITECH).unlock(ResearchEntries.UNLOCK_MAGITECH).icon(ResourceUtils.loc("textures/research/discipline_magitech.png"))
                .craftingStat(StatsPM.CRAFTED_MAGITECH).expertiseStat(StatsPM.EXPERTISE_MAGITECH).indexSortOrder(700).build());
        context.register(SCANS, ResearchDiscipline.builder(SCANS).unlock(ResearchEntries.UNLOCK_SCANS).icon(ResourceUtils.loc("textures/item/magnifying_glass.png")).build());
    }
    
    @Nullable
    public static ResearchDiscipline getDiscipline(RegistryAccess registryAccess, ResearchDisciplineKey key) {
        return getDiscipline(registryAccess, key.getRootKey());
    }
    
    @Nullable
    public static ResearchDiscipline getDiscipline(RegistryAccess registryAccess, ResourceKey<ResearchDiscipline> rawKey) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_DISCIPLINES).get(rawKey);
    }
    
    /**
     * Retrieves a list of the research disciplines that should be shown in the main index of the
     * grimoire, in sorted order.  Disciplines without an index sort order are excluded.
     * 
     * @param registryAccess a registry access object
     * @return a list of the research disciplines that should be shown in the grimoire index
     */
    public static List<ResearchDiscipline> getIndexDisciplines(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_DISCIPLINES).stream().filter(d -> d.indexSortOrder().isPresent()).sorted((a, b) -> Integer.compare(a.indexSortOrder().getAsInt(), b.indexSortOrder().getAsInt())).toList();
    }
}
