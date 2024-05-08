package com.verdantartifice.primalmagick.common.research;

import java.util.function.Function;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * Datapack registry for the mod's research entries, the backbone of its progression system.
 * 
 * @author Daedalus4096
 */
public class ResearchEntries {
    // Fundamentals research entries
    public static final ResourceKey<ResearchEntry> FIRST_STEPS = create("first_steps");
    public static final ResourceKey<ResearchEntry> THEORYCRAFTING = create("theorycrafting");
    public static final ResourceKey<ResearchEntry> ATTUNEMENTS = create("attunements");
    public static final ResourceKey<ResearchEntry> LINGUISTICS = create("linguistics");
    public static final ResourceKey<ResearchEntry> UNLOCK_MANAWEAVING = create("unlock_manaweaving");
    public static final ResourceKey<ResearchEntry> UNLOCK_ALCHEMY = create("unlock_alchemy");
    public static final ResourceKey<ResearchEntry> UNLOCK_SORCERY = create("unlock_sorcery");
    public static final ResourceKey<ResearchEntry> UNLOCK_RUNEWORKING = create("unlock_runeworking");
    public static final ResourceKey<ResearchEntry> UNLOCK_RITUAL = create("unlock_ritual");
    public static final ResourceKey<ResearchEntry> UNLOCK_MAGITECH = create("unlock_magitech");
    public static final ResourceKey<ResearchEntry> UNLOCK_SCANS = create("unlock_scans");
    
    // Manaweaving research entries
    public static final ResourceKey<ResearchEntry> MANA_ARROWS = create("mana_arrows");
    public static final ResourceKey<ResearchEntry> WAND_CHARGER = create("wand_charger");
    public static final ResourceKey<ResearchEntry> MANA_SALTS = create("mana_salts");
    
    // Alchemy research entries
    public static final ResourceKey<ResearchEntry> CALCINATOR_BASIC = create("calcinator_basic");
    
    // Sorcery research entries
    public static final ResourceKey<ResearchEntry> WAND_INSCRIPTION = create("wand_inscription");
    
    // Runeworking research entries
    public static final ResourceKey<ResearchEntry> RUNE_PROJECT = create("rune_project");
    
    // Ritual Magick research entries
    public static final ResourceKey<ResearchEntry> MANAFRUIT = create("manafruit");
    
    // Magitech research entries
    
    public static ResourceKey<ResearchEntry> create(String name) {
        return ResourceKey.create(RegistryKeysPM.RESEARCH_ENTRIES, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<ResearchEntry> context) {
        bootstrapBasicsEntries(context);
    }
    
    private static void bootstrapBasicsEntries(BootstapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.BASICS;
        register(context, FIRST_STEPS, key -> ResearchEntry.builder(key).discipline(discipline).icon(ItemsPM.GRIMOIRE.get())
                .stage().requiredCraft(ItemsPM.ARCANE_WORKBENCH.get()).recipe(ItemsPM.MUNDANE_WAND.get()).end()
                .stage().requiredStat(StatsPM.MANA_SIPHONED, 10).recipe(ItemsPM.MUNDANE_WAND.get()).end()
                .stage().requiredStat(StatsPM.OBSERVATIONS_MADE, 1).recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.WOOD_TABLE.get()).recipe(ItemsPM.MAGNIFYING_GLASS.get())
                        .recipe(ItemsPM.ANALYSIS_TABLE.get()).end()
                .stage().recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.WOOD_TABLE.get()).recipe(ItemsPM.MAGNIFYING_GLASS.get()).recipe(ItemsPM.ANALYSIS_TABLE.get()).end()
                .build());
        register(context, THEORYCRAFTING, key -> ResearchEntry.builder(key).discipline(discipline).icon("textures/research/knowledge_theory.png").parent(FIRST_STEPS)
                .stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).end()
                .stage().requiredCraft(ItemsPM.RESEARCH_TABLE.get()).requiredCraft(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get())
                        .recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).end()
                .stage().requiredStat(StatsPM.THEORIES_FORMED, 1).recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get()).recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).end()
                .stage().recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get()).recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).end()
                .build());
        register(context, ATTUNEMENTS, key -> ResearchEntry.builder(key).discipline(discipline).parent(FIRST_STEPS)
                .stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).end()
                .stage().end()
                .build());
        // FIXME Re-add for 1.21 release
/*
        register(context, LINGUISTICS, key -> ResearchEntry.builder(key).discipline(discipline).icon(Items.WRITABLE_BOOK).parent(FIRST_STEPS)
                .stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).requiredStat(StatsPM.ANCIENT_BOOKS_READ, 1).end()
                .stage().recipe(ItemsPM.SCRIBE_TABLE.get()).end()
                .build());
*/
        register(context, UNLOCK_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).icon("textures/research/discipline_manaweaving.png").parent(FIRST_STEPS)
                .stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).icon("textures/research/discipline_alchemy.png").parent(MANA_ARROWS)
                .stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon("textures/research/discipline_sorcery.png").parent(WAND_CHARGER)
                .stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).icon("textures/research/discipline_runeworking.png").parent(CALCINATOR_BASIC)
                .stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).icon("textures/research/discipline_ritual.png").parent(WAND_INSCRIPTION).parent(RUNE_PROJECT)
                .stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).icon("textures/research/discipline_magitech.png").parent(MANAFRUIT).parent(MANA_SALTS)
                .stage().requiredKnowledge(KnowledgeType.OBSERVATION, 1).end()
                .stage().end()
                .build());
    }
    
    private static Holder.Reference<ResearchEntry> register(BootstapContext<ResearchEntry> context, ResourceKey<ResearchEntry> key, Function<ResourceKey<ResearchEntry>, ResearchEntry> supplier) {
        return context.register(key, supplier.apply(key));
    }
}
