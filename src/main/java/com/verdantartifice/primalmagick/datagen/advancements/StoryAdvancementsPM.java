package com.verdantartifice.primalmagick.datagen.advancements;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.advancements.critereon.ResearchCompletedTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.StatValueTrigger;
import com.verdantartifice.primalmagick.common.init.InitAdvancements;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructuresPM;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;

/**
 * Data sub-provider for generating mod advancements.
 * 
 * @author Daedalus4096
 */
public class StoryAdvancementsPM implements AdvancementGenerator {
    @Override
    public void generate(Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        InitAdvancements.initCriteria();
        AdvancementHolder root = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("root").icon(ItemsPM.GRIMOIRE.get()).background(PrimalMagick.resource("textures/block/marble_raw.png")).build())
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("earth_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.EARTH_SHRINE)))
                .addCriterion("sea_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SEA_SHRINE)))
                .addCriterion("sky_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SKY_SHRINE)))
                .addCriterion("sun_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SUN_SHRINE)))
                .addCriterion("moon_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.MOON_SHRINE)))
                .save(saver, PrimalMagick.resource("story/root"));
        AdvancementHolder craftMundaneWand = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_mundane_wand").icon(ItemsPM.MUNDANE_WAND.get()).build())
                .parent(root)
                .addCriterion("has_mundane_wand", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MUNDANE_WAND.get()))
                .save(saver, PrimalMagick.resource("story/craft_mundane_wand"));
        AdvancementHolder craftGrimoire = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_grimoire").icon(Items.BOOKSHELF).build())
                .parent(craftMundaneWand)
                .addCriterion("has_grimoire", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRIMOIRE.get()))
                .save(saver, PrimalMagick.resource("story/craft_grimoire"));
        AdvancementHolder craftArcaneWorkbench = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_arcane_workbench").icon(ItemsPM.ARCANE_WORKBENCH.get()).build())
                .parent(craftGrimoire)
                .addCriterion("made_workbench", ResearchCompletedTrigger.TriggerInstance.stackCrafted(ItemsPM.ARCANE_WORKBENCH.get()))
                .save(saver, PrimalMagick.resource("story/craft_arcane_workbench"));
        AdvancementHolder craftEssenceFurnace = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_essence_furnace").icon(ItemsPM.ESSENCE_FURNACE.get()).build())
                .parent(craftGrimoire)
                .addCriterion("made_furnace", ResearchCompletedTrigger.TriggerInstance.stackCrafted(ItemsPM.ESSENCE_FURNACE.get()))
                .save(saver, PrimalMagick.resource("story/craft_essence_furnace"));
        AdvancementHolder firstTheorycraft = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("first_theorycraft").icon(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).build())
                .parent(craftGrimoire)
                .addCriterion("completed_project", StatValueTrigger.TriggerInstance.atLeast(StatsPM.RESEARCH_PROJECTS_COMPLETED, 1))
                .save(saver, PrimalMagick.resource("story/first_theorycraft"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("many_theorycrafts").icon(ItemsPM.RESEARCH_TABLE.get()).type(AdvancementType.CHALLENGE).build())
                .parent(firstTheorycraft)
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("completed_many_projects", StatValueTrigger.TriggerInstance.atLeast(StatsPM.RESEARCH_PROJECTS_COMPLETED, 250))
                .save(saver, PrimalMagick.resource("story/many_theorycrafts"));
        // TODO More advancements
        AdvancementHolder discoverForbidden = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("discover_forbidden").icon(ItemsPM.BLOOD_NOTES.get()).build())
                .parent(craftGrimoire)
                .addCriterion("discovered_forbidden", ResearchCompletedTrigger.TriggerInstance.researchEntry(ResearchEntries.DISCOVER_FORBIDDEN))
                .save(saver, PrimalMagick.resource("story/discover_forbidden"));
    }
}
