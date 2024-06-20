package com.verdantartifice.primalmagick.datagen.advancements;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.advancements.critereon.ResearchCompletedTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.StatValueTrigger;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.init.InitAdvancements;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructuresPM;

import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.ItemStack;
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
        // Custom advancement criteria are normally registered as part of FMLCommonSetup. However, that event never gets fired
        // in datagen runs, so it must be done manually as part of the data provider.
        InitAdvancements.initCriteria();
        
        // Define advancements
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
        ItemStack apprenticeWand = Util.make(new ItemStack(ItemsPM.MODULAR_WAND.get()), stack -> {
            ModularWandItem wandItem = (ModularWandItem)stack.getItem();
            wandItem.setWandCore(stack, WandCore.HEARTWOOD);
            wandItem.setWandCap(stack, WandCap.IRON);
            wandItem.setWandGem(stack, WandGem.APPRENTICE);
        });
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_modular_wand").icon(apprenticeWand).build())
                .parent(craftArcaneWorkbench)
                .addCriterion("has_wand", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MODULAR_WAND.get()))
                .save(saver, PrimalMagick.resource("story/craft_modular_wand"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_starter_robes").icon(ItemsPM.IMBUED_WOOL_HEAD.get()).build())
                .parent(craftArcaneWorkbench)
                .addCriterion("wool_head", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.IMBUED_WOOL_HEAD.get()))
                .addCriterion("wool_chest", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.IMBUED_WOOL_CHEST.get()))
                .addCriterion("wool_legs", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.IMBUED_WOOL_LEGS.get()))
                .addCriterion("wool_feet", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.IMBUED_WOOL_FEET.get()))
                .save(saver, PrimalMagick.resource("story/craft_starter_robes"));
        AdvancementHolder craftArtificialManaFont = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_artificial_mana_font").icon(ItemsPM.ARTIFICIAL_FONT_EARTH.get()).build())
                .parent(craftArcaneWorkbench)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("has_earth_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARTIFICIAL_FONT_EARTH.get()))
                .addCriterion("has_sea_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARTIFICIAL_FONT_SEA.get()))
                .addCriterion("has_sky_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARTIFICIAL_FONT_SKY.get()))
                .addCriterion("has_sun_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARTIFICIAL_FONT_SUN.get()))
                .addCriterion("has_moon_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARTIFICIAL_FONT_MOON.get()))
                .addCriterion("has_blood_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARTIFICIAL_FONT_BLOOD.get()))
                .addCriterion("has_infernal_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARTIFICIAL_FONT_INFERNAL.get()))
                .addCriterion("has_void_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARTIFICIAL_FONT_VOID.get()))
                .addCriterion("has_hallowed_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARTIFICIAL_FONT_HALLOWED.get()))
                .save(saver, PrimalMagick.resource("story/craft_artificial_mana_font"));
        AdvancementHolder craftForbiddenManaFont = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_forbidden_mana_font").icon(ItemsPM.ARTIFICIAL_FONT_EARTH.get()).build())
                .parent(craftArtificialManaFont)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("has_earth_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.FORBIDDEN_FONT_EARTH.get()))
                .addCriterion("has_sea_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.FORBIDDEN_FONT_SEA.get()))
                .addCriterion("has_sky_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.FORBIDDEN_FONT_SKY.get()))
                .addCriterion("has_sun_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.FORBIDDEN_FONT_SUN.get()))
                .addCriterion("has_moon_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.FORBIDDEN_FONT_MOON.get()))
                .addCriterion("has_blood_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.FORBIDDEN_FONT_BLOOD.get()))
                .addCriterion("has_infernal_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.FORBIDDEN_FONT_INFERNAL.get()))
                .addCriterion("has_void_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.FORBIDDEN_FONT_VOID.get()))
                .addCriterion("has_hallowed_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.FORBIDDEN_FONT_HALLOWED.get()))
                .save(saver, PrimalMagick.resource("story/craft_forbidden_mana_font"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_heavenly_mana_font").icon(ItemsPM.ARTIFICIAL_FONT_EARTH.get()).build())
                .parent(craftForbiddenManaFont)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("has_earth_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HEAVENLY_FONT_EARTH.get()))
                .addCriterion("has_sea_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HEAVENLY_FONT_SEA.get()))
                .addCriterion("has_sky_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HEAVENLY_FONT_SKY.get()))
                .addCriterion("has_sun_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HEAVENLY_FONT_SUN.get()))
                .addCriterion("has_moon_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HEAVENLY_FONT_MOON.get()))
                .addCriterion("has_blood_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HEAVENLY_FONT_BLOOD.get()))
                .addCriterion("has_infernal_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HEAVENLY_FONT_INFERNAL.get()))
                .addCriterion("has_void_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HEAVENLY_FONT_VOID.get()))
                .addCriterion("has_hallowed_font", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HEAVENLY_FONT_HALLOWED.get()))
                .save(saver, PrimalMagick.resource("story/craft_heavenly_mana_font"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_primalite").icon(ItemsPM.PRIMALITE_INGOT.get()).build())
                .parent(craftEssenceFurnace)
                .addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.PRIMALITE_INGOT.get()))
                .save(saver, PrimalMagick.resource("story/craft_primalite"));
        AdvancementHolder discoverForbidden = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("discover_forbidden").icon(ItemsPM.BLOOD_NOTES.get()).build())
                .parent(firstTheorycraft)
                .addCriterion("discovered_forbidden", ResearchCompletedTrigger.TriggerInstance.researchEntry(ResearchEntries.DISCOVER_FORBIDDEN))
                .save(saver, PrimalMagick.resource("story/discover_forbidden"));
        AdvancementHolder craftSoulGem = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_soul_gem").icon(ItemsPM.SOUL_GEM.get()).build())
                .parent(discoverForbidden)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("has_gem", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.SOUL_GEM.get()))
                .addCriterion("has_sliver", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.SOUL_GEM_SLIVER.get()))
                .save(saver, PrimalMagick.resource("story/craft_soul_gem"));
        AdvancementHolder craftSanguineCrucible = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_sanguine_crucible").icon(ItemsPM.SANGUINE_CRUCIBLE.get()).build())
                .parent(craftSoulGem)
                .addCriterion("has_crucible", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.SANGUINE_CRUCIBLE.get()))
                .save(saver, PrimalMagick.resource("story/craft_sanguine_crucible"));
        AdvancementHolder killInnerDemon = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("kill_inner_demon").icon(ItemsPM.SANGUINE_CORE_INNER_DEMON.get()).type(AdvancementType.GOAL).build())
                .parent(craftSanguineCrucible)
                .addCriterion("kill_inner_demon", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityTypesPM.INNER_DEMON.get())))
                .save(saver, PrimalMagick.resource("story/kill_inner_demon"));
        AdvancementHolder discoverHallowed = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("discover_hallowed").icon(ItemsPM.HALLOWED_ORB.get()).type(AdvancementType.GOAL).build())
                .parent(killInnerDemon)
                .addCriterion("discovered_forbidden", ResearchCompletedTrigger.TriggerInstance.researchEntry(ResearchEntries.DISCOVER_HALLOWED))
                .save(saver, PrimalMagick.resource("story/discover_hallowed"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("completionist").icon(ItemsPM.GRIMOIRE.get()).type(AdvancementType.GOAL).build())
                .parent(discoverHallowed)
                .addCriterion("discovered_forbidden", ResearchCompletedTrigger.TriggerInstance.researchEntry(ResearchEntries.THEORY_OF_EVERYTHING))
                .save(saver, PrimalMagick.resource("story/completionist"));
    }
}
