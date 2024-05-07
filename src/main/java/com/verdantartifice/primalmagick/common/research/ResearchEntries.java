package com.verdantartifice.primalmagick.common.research;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * Datapack registry for the mod's research entries, the backbone of its progression system.
 * 
 * @author Daedalus4096
 */
public class ResearchEntries {
    public static final ResourceKey<ResearchEntry> FIRST_STEPS = create("first_steps");
    public static final ResourceKey<ResearchEntry> UNLOCK_MANAWEAVING = create("unlock_manaweaving");
    public static final ResourceKey<ResearchEntry> UNLOCK_ALCHEMY = create("unlock_alchemy");
    public static final ResourceKey<ResearchEntry> UNLOCK_SORCERY = create("unlock_sorcery");
    public static final ResourceKey<ResearchEntry> UNLOCK_RUNEWORKING = create("unlock_runeworking");
    public static final ResourceKey<ResearchEntry> UNLOCK_RITUAL = create("unlock_ritual");
    public static final ResourceKey<ResearchEntry> UNLOCK_MAGITECH = create("unlock_magitech");
    public static final ResourceKey<ResearchEntry> UNLOCK_SCANS = create("unlock_scans");
    
    public static ResourceKey<ResearchEntry> create(String name) {
        return ResourceKey.create(RegistryKeysPM.RESEARCH_ENTRIES, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<ResearchEntry> context) {
        bootstrapBasicsEntries(context);
    }
    
    private static void bootstrapBasicsEntries(BootstapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.BASICS;
        context.register(FIRST_STEPS, ResearchEntry.builder(FIRST_STEPS).discipline(discipline).icon(ItemsPM.GRIMOIRE.get())
                .stage().requiredCraft(ItemsPM.ARCANE_WORKBENCH.get()).recipe(ItemsPM.MUNDANE_WAND.get()).end()
                .stage().requiredStat(StatsPM.MANA_SIPHONED, 10).recipe(ItemsPM.MUNDANE_WAND.get()).end()
                .stage().requiredStat(StatsPM.OBSERVATIONS_MADE, 1).recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.WOOD_TABLE.get()).recipe(ItemsPM.MAGNIFYING_GLASS.get())
                        .recipe(ItemsPM.ANALYSIS_TABLE.get()).end()
                .stage().recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.WOOD_TABLE.get()).recipe(ItemsPM.MAGNIFYING_GLASS.get()).recipe(ItemsPM.ANALYSIS_TABLE.get()).end()
                .build());
    }
}
