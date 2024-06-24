package com.verdantartifice.primalmagick.datagen.advancements;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.advancements.critereon.AttunementThresholdTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.EntityHurtPlayerTriggerExt;
import com.verdantartifice.primalmagick.common.advancements.critereon.LinguisticsComprehensionTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.RecallStoneTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.ResearchCompletedTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.RuneUseCountTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.RunescribingTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.StatValueTrigger;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.init.InitAdvancements;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tags.DamageTypeTagsPM;
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
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.StartRidingTrigger;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;

/**
 * Data sub-provider for generating mod advancements.
 * 
 * @author Daedalus4096
 */
public class StoryAdvancementsPM implements AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
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
        AdvancementHolder craftForbiddenManaFont = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_forbidden_mana_font").icon(ItemsPM.FORBIDDEN_FONT_BLOOD.get()).build())
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
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_heavenly_mana_font").icon(ItemsPM.HEAVENLY_FONT_HALLOWED.get()).build())
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
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("discover_all_shrines").icon(Items.FILLED_MAP).build())
                .parent(craftGrimoire)
                .addCriterion("earth_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.EARTH_SHRINE)))
                .addCriterion("sea_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SEA_SHRINE)))
                .addCriterion("sky_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SKY_SHRINE)))
                .addCriterion("sun_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SUN_SHRINE)))
                .addCriterion("moon_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.MOON_SHRINE)))
                .save(saver, PrimalMagick.resource("story/discover_all_shrines"));
        AdvancementHolder discoverLibrary = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("discover_library").icon(ItemsPM.STATIC_BOOK.get()).build())
                .parent(craftGrimoire)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("earth_library", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.EARTH_LIBRARY)))
                .addCriterion("sea_library", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SEA_LIBRARY)))
                .addCriterion("sky_library", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SKY_LIBRARY)))
                .addCriterion("sun_library", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SUN_LIBRARY)))
                .addCriterion("moon_library", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.MOON_LIBRARY)))
                .addCriterion("forbidden_library", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.FORBIDDEN_LIBRARY)))
                .save(saver, PrimalMagick.resource("story/discover_library"));
        AdvancementHolder gainSomeComprehension = makeComprehensionAdvancement("gain_some_comprehension", ItemsPM.SCRIBE_TABLE.get(), AdvancementType.TASK, discoverLibrary, false, 1, saver);
        AdvancementHolder fullyComprehendLanguage = makeComprehensionAdvancement("fully_comprehend_language", ItemsPM.STATIC_BOOK_UNCOMMON.get(), AdvancementType.GOAL, gainSomeComprehension, false, 60, saver);
        makeComprehensionAdvancement("fully_comprehend_all_languages", ItemsPM.STATIC_BOOK_RARE.get(), AdvancementType.CHALLENGE, fullyComprehendLanguage, true, 60, saver);
        AdvancementHolder craftSpellcraftingAltar = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_spellcrafting_altar").icon(ItemsPM.SPELLCRAFTING_ALTAR.get()).build())
                .parent(craftArcaneWorkbench)
                .addCriterion("has_altar", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.SPELLCRAFTING_ALTAR.get()))
                .save(saver, PrimalMagick.resource("story/craft_spellcrafting_altar"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_expensive_spell").icon(ItemsPM.ARCHMAGE_WAND_GEM_ITEM.get()).type(AdvancementType.GOAL).build())
                .parent(craftSpellcraftingAltar)
                .addCriterion("has_stat", StatValueTrigger.TriggerInstance.atLeast(StatsPM.SPELLS_CRAFTED_MAX_COST, 2000))
                .save(saver, PrimalMagick.resource("story/craft_expensive_spell"));
        AdvancementHolder killWithSorcery = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("kill_with_sorcery").icon(ItemsPM.SPELL_SCROLL_FILLED.get()).build())
                .parent(craftSpellcraftingAltar)
                .addCriterion("any_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY))))
                .save(saver, PrimalMagick.resource("story/kill_with_sorcery"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("kill_with_all_sorcery").icon(Items.SKELETON_SKULL).type(AdvancementType.GOAL).build())
                .parent(killWithSorcery)
                .addCriterion("earth_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY_EARTH))))
                .addCriterion("sea_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY_SEA))))
                .addCriterion("sky_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY_SKY))))
                .addCriterion("sun_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY_SUN))))
                .addCriterion("moon_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY_MOON))))
                .addCriterion("blood_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY_BLOOD))))
                .addCriterion("infernal_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY_INFERNAL))))
                .addCriterion("void_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY_VOID))))
                .addCriterion("hallowed_spell", KilledTrigger.TriggerInstance.playerKilledEntity(Optional.empty(), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsPM.IS_SORCERY_HALLOWED))))
                .save(saver, PrimalMagick.resource("story/kill_with_all_sorcery"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("polymorph_sheep").icon(Items.SHEARS).type(AdvancementType.CHALLENGE).build())
                .parent(craftSpellcraftingAltar)
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("discovered_forbidden", ResearchCompletedTrigger.TriggerInstance.researchEntry(ResearchEntries.SPELL_PAYLOAD_POLYMORPH_SHEEP))
                .save(saver, PrimalMagick.resource("story/polymorph_sheep"));
        AdvancementHolder craftRunecarvingTable = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_runecarving_table").icon(ItemsPM.RUNECARVING_TABLE.get()).build())
                .parent(craftArcaneWorkbench)
                .addCriterion("has_table", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.RUNECARVING_TABLE.get()))
                .save(saver, PrimalMagick.resource("story/craft_runecarving_table"));
        AdvancementHolder runescribeEnchantment = makeRunescribingAdvancement(registries, "runescribe_enchantment", ItemsPM.RUNESCRIBING_ALTAR_BASIC.get(), AdvancementType.TASK, craftRunecarvingTable, false, saver);
        makeRunescribingAdvancement(registries, "runescribe_all_enchantments", Items.ENCHANTING_TABLE, AdvancementType.CHALLENGE, runescribeEnchantment, true, saver);
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_power_rune").icon(ItemsPM.RUNE_POWER.get()).build())
                .parent(runescribeEnchantment)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("has_insight", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.RUNE_INSIGHT.get()))
                .addCriterion("has_power", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.RUNE_POWER.get()))
                .addCriterion("has_grace", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.RUNE_GRACE.get()))
                .save(saver, PrimalMagick.resource("story/craft_power_rune"));
        AdvancementHolder useRecallStone = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("use_recall_stone").icon(ItemsPM.RECALL_STONE.get()).build())
                .parent(runescribeEnchantment)
                .addCriterion("recall", RecallStoneTrigger.TriggerInstance.anywhere())
                .save(saver, PrimalMagick.resource("story/use_recall_stone"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("use_recall_stone_nether").icon(Items.RESPAWN_ANCHOR).build())
                .parent(useRecallStone)
                .addCriterion("recall_nether", RecallStoneTrigger.TriggerInstance.inDimension(Level.NETHER))
                .save(saver, PrimalMagick.resource("story/use_recall_stone_nether"));
        AdvancementHolder craftRitualAltar = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_ritual_altar").icon(ItemsPM.RITUAL_ALTAR.get()).build())
                .parent(craftArcaneWorkbench)
                .addCriterion("has_ritual_altar", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.RITUAL_ALTAR.get()))
                .save(saver, PrimalMagick.resource("story/craft_ritual_altar"));
        AdvancementHolder sufferRitualMishap = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("suffer_ritual_mishap").icon(ItemsPM.DOWSING_ROD.get()).build())
                .parent(craftRitualAltar)
                .addCriterion("has_mishap", StatValueTrigger.TriggerInstance.atLeast(StatsPM.RITUAL_MISHAPS, 1))
                .save(saver, PrimalMagick.resource("story/suffer_ritual_mishap"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("suffer_many_ritual_mishaps").icon(ItemsPM.ENTROPY_SINK.get()).type(AdvancementType.CHALLENGE).build())
                .parent(sufferRitualMishap)
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("has_many_mishaps", StatValueTrigger.TriggerInstance.atLeast(StatsPM.RITUAL_MISHAPS, 50))
                .save(saver, PrimalMagick.resource("story/suffer_many_ritual_mishaps"));
        AdvancementHolder rideFlyingCarpet = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("ride_flying_carpet").icon(ItemsPM.FLYING_CARPET.get()).build())
                .parent(craftRitualAltar)
                .addCriterion("ride_carpet", StartRidingTrigger.TriggerInstance.playerStartsRiding(EntityPredicate.Builder.entity().vehicle(EntityPredicate.Builder.entity().of(EntityTypesPM.FLYING_CARPET.get()))))
                .save(saver, PrimalMagick.resource("story/ride_flying_carpet"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("get_shot_off_flying_carpet").icon(Items.FIRE_CHARGE).type(AdvancementType.CHALLENGE).build())
                .parent(rideFlyingCarpet)
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("shot_off_carpet", EntityHurtPlayerTriggerExt.TriggerInstance.playerHurtEntity(
                        Optional.of(EntityPredicate.Builder.entity().of(EntityType.PLAYER).vehicle(EntityPredicate.Builder.entity().of(EntityTypesPM.FLYING_CARPET.get())).build()), 
                        DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(EntityType.GHAST).build()).type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of(EntityType.FIREBALL)))))
                .save(saver, PrimalMagick.resource("story/get_shot_off_flying_carpet"));
        AdvancementHolder craftAmbrosia = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_ambrosia").icon(ItemsPM.BASIC_EARTH_AMBROSIA.get()).build())
                .parent(craftRitualAltar)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("earth", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_EARTH_AMBROSIA.get()))
                .addCriterion("sea", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_SEA_AMBROSIA.get()))
                .addCriterion("sky", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_SKY_AMBROSIA.get()))
                .addCriterion("sun", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_SUN_AMBROSIA.get()))
                .addCriterion("moon", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_MOON_AMBROSIA.get()))
                .addCriterion("blood", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_BLOOD_AMBROSIA.get()))
                .addCriterion("infernal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_INFERNAL_AMBROSIA.get()))
                .addCriterion("void", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_VOID_AMBROSIA.get()))
                .addCriterion("hallowed", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_HALLOWED_AMBROSIA.get()))
                .save(saver, PrimalMagick.resource("story/craft_ambrosia"));
        AdvancementHolder allMinorAttunements = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("all_minor_attunements").icon(ItemsPM.GREATER_INFERNAL_AMBROSIA.get()).build())
                .parent(craftAmbrosia)
                .addCriterion("earth", AttunementThresholdTrigger.TriggerInstance.ephemeral(Sources.EARTH, AttunementThreshold.MINOR))
                .addCriterion("sea", AttunementThresholdTrigger.TriggerInstance.ephemeral(Sources.SEA, AttunementThreshold.MINOR))
                .addCriterion("sky", AttunementThresholdTrigger.TriggerInstance.ephemeral(Sources.SKY, AttunementThreshold.MINOR))
                .addCriterion("sun", AttunementThresholdTrigger.TriggerInstance.ephemeral(Sources.SUN, AttunementThreshold.MINOR))
                .addCriterion("moon", AttunementThresholdTrigger.TriggerInstance.ephemeral(Sources.MOON, AttunementThreshold.MINOR))
                .addCriterion("blood", AttunementThresholdTrigger.TriggerInstance.ephemeral(Sources.BLOOD, AttunementThreshold.MINOR))
                .addCriterion("infernal", AttunementThresholdTrigger.TriggerInstance.ephemeral(Sources.INFERNAL, AttunementThreshold.MINOR))
                .addCriterion("void", AttunementThresholdTrigger.TriggerInstance.ephemeral(Sources.VOID, AttunementThreshold.MINOR))
                .addCriterion("hallowed", AttunementThresholdTrigger.TriggerInstance.ephemeral(Sources.HALLOWED, AttunementThreshold.MINOR))
                .save(saver, PrimalMagick.resource("story/all_minor_attunements"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("greater_attunement").icon(ItemsPM.SUPREME_HALLOWED_AMBROSIA.get()).type(AdvancementType.GOAL).build())
                .parent(allMinorAttunements)
                .addCriterion("any_greater", AttunementThresholdTrigger.TriggerInstance.anyLasting(AttunementThreshold.GREATER))
                .save(saver, PrimalMagick.resource("story/greater_attunement"));
        AdvancementHolder craftBasicPixie = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_basic_pixie").icon(ItemsPM.BASIC_EARTH_PIXIE.get()).build())
                .parent(craftRitualAltar)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("earth_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_EARTH_PIXIE.get()))
                .addCriterion("sea_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_SEA_PIXIE.get()))
                .addCriterion("sky_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_SKY_PIXIE.get()))
                .addCriterion("sun_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_SUN_PIXIE.get()))
                .addCriterion("moon_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_MOON_PIXIE.get()))
                .addCriterion("blood_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_BLOOD_PIXIE.get()))
                .addCriterion("infernal_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_INFERNAL_PIXIE.get()))
                .addCriterion("void_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_VOID_PIXIE.get()))
                .addCriterion("hallowed_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_HALLOWED_PIXIE.get()))
                .save(saver, PrimalMagick.resource("story/craft_basic_pixie"));
        AdvancementHolder craftGrandPixie = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_grand_pixie").icon(ItemsPM.GRAND_INFERNAL_PIXIE.get()).build())
                .parent(craftBasicPixie)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("earth_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRAND_EARTH_PIXIE.get()))
                .addCriterion("sea_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRAND_SEA_PIXIE.get()))
                .addCriterion("sky_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRAND_SKY_PIXIE.get()))
                .addCriterion("sun_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRAND_SUN_PIXIE.get()))
                .addCriterion("moon_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRAND_MOON_PIXIE.get()))
                .addCriterion("blood_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRAND_BLOOD_PIXIE.get()))
                .addCriterion("infernal_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRAND_INFERNAL_PIXIE.get()))
                .addCriterion("void_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRAND_VOID_PIXIE.get()))
                .addCriterion("hallowed_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRAND_HALLOWED_PIXIE.get()))
                .save(saver, PrimalMagick.resource("story/craft_grand_pixie"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_majestic_pixie").icon(ItemsPM.MAJESTIC_HALLOWED_PIXIE.get()).type(AdvancementType.GOAL).build())
                .parent(craftGrandPixie)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("earth_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAJESTIC_EARTH_PIXIE.get()))
                .addCriterion("sea_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAJESTIC_SEA_PIXIE.get()))
                .addCriterion("sky_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAJESTIC_SKY_PIXIE.get()))
                .addCriterion("sun_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAJESTIC_SUN_PIXIE.get()))
                .addCriterion("moon_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAJESTIC_MOON_PIXIE.get()))
                .addCriterion("blood_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAJESTIC_BLOOD_PIXIE.get()))
                .addCriterion("infernal_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAJESTIC_INFERNAL_PIXIE.get()))
                .addCriterion("void_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAJESTIC_VOID_PIXIE.get()))
                .addCriterion("hallowed_pixie", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAJESTIC_HALLOWED_PIXIE.get()))
                .save(saver, PrimalMagick.resource("story/craft_majestic_pixie"));
        AdvancementHolder craftMagitechParts = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_magitech_parts").icon(ItemsPM.MAGITECH_PARTS_BASIC.get()).build())
                .parent(craftArcaneWorkbench)
                .addCriterion("has_parts", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MAGITECH_PARTS_BASIC.get()))
                .save(saver, PrimalMagick.resource("story/craft_magitech_parts"));
        AdvancementHolder craftArcanometer = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_arcanometer").icon(ItemsPM.ARCANOMETER.get()).build())
                .parent(craftMagitechParts)
                .addCriterion("has_arcanometer", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ARCANOMETER.get()))
                .save(saver, PrimalMagick.resource("story/craft_arcanometer"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("scan_chest").icon(Items.CHEST).build())
                .parent(craftArcanometer)
                .addCriterion("scan_chest", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(Tags.Blocks.CHESTS)), 
                        ItemPredicate.Builder.item().of(ItemsPM.ARCANOMETER.get())))
                .save(saver, PrimalMagick.resource("story/scan_chest"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_seascribe_pen").icon(ItemsPM.SEASCRIBE_PEN.get()).build())
                .parent(craftMagitechParts)
                .addCriterion("has_pen", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.SEASCRIBE_PEN.get()))
                .save(saver, PrimalMagick.resource("story/craft_seascribe_pen"));
        AdvancementHolder craftAlchemicalBomb = makeBombAdvancement(registries, "craft_alchemical_bomb", ConcoctionUtils.newBomb(Potions.HARMING), AdvancementType.TASK, craftArcanometer, false, saver);
        makeBombAdvancement(registries, "craft_all_alchemical_bombs", new ItemStack(Items.TNT), AdvancementType.CHALLENGE, craftAlchemicalBomb, true, saver);
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_zephyr_engine").icon(ItemsPM.ZEPHYR_ENGINE.get()).build())
                .parent(craftArcanometer)
                .addCriterion("has_engine", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.ZEPHYR_ENGINE.get()))
                .save(saver, PrimalMagick.resource("story/craft_zephyr_engine"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_warding_module").icon(ItemsPM.BASIC_WARDING_MODULE.get()).build())
                .parent(craftArcanometer)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("has_basic_module", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.BASIC_WARDING_MODULE.get()))
                .addCriterion("has_greater_module", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GREATER_WARDING_MODULE.get()))
                .addCriterion("has_supreme_module", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.SUPREME_WARDING_MODULE.get()))
                .save(saver, PrimalMagick.resource("story/craft_warding_module"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("find_humming_artifact").icon(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()).build())
                .parent(craftGrimoire)
                .addCriterion("has_artifact", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()))
                .save(saver, PrimalMagick.resource("story/find_humming_artifact"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("find_lore_tablet").icon(ItemsPM.STATIC_TABLET.get()).build())
                .parent(discoverLibrary)
                .addCriterion("has_tablet", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.STATIC_TABLET.get()))
                .save(saver, PrimalMagick.resource("story/find_lore_tablet"));
        AdvancementHolder reuseRuneOnce = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("reuse_rune_once").icon(ItemsPM.RUNESCRIBING_ALTAR_ENCHANTED.get()).build())
                .parent(runescribeEnchantment)
                .addCriterion("reuse_once", RuneUseCountTrigger.TriggerInstance.atLeast(2))
                .save(saver, PrimalMagick.resource("story/reuse_rune_once"));
        AdvancementHolder reuseRuneTwice = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("reuse_rune_twice").icon(ItemsPM.RUNESCRIBING_ALTAR_FORBIDDEN.get()).type(AdvancementType.GOAL).build())
                .parent(reuseRuneOnce)
                .addCriterion("reuse_twice", RuneUseCountTrigger.TriggerInstance.atLeast(3))
                .save(saver, PrimalMagick.resource("story/reuse_rune_twice"));
        Advancement.Builder.advancement().display(DisplayInfoBuilder.id("reuse_rune_thrice").icon(ItemsPM.RUNESCRIBING_ALTAR_HEAVENLY.get()).type(AdvancementType.CHALLENGE).build())
                .parent(reuseRuneTwice)
                .rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("reuse_thrice", RuneUseCountTrigger.TriggerInstance.atLeast(4))
                .save(saver, PrimalMagick.resource("story/reuse_rune_thrice"));
    }
    
    private static AdvancementHolder makeComprehensionAdvancement(String id, ItemLike icon, AdvancementType type, AdvancementHolder parent, boolean requireAll, int threshold, Consumer<AdvancementHolder> saver) {
        // TODO Don't hardcode languages, use the registry lookup and ancient tag (problem: custom datapack tags don't appear to be bound)
        Advancement.Builder builder = Advancement.Builder.advancement().display(DisplayInfoBuilder.id(id).icon(icon).type(type).build())
                .parent(parent)
                .requirements(requireAll ? AdvancementRequirements.Strategy.AND : AdvancementRequirements.Strategy.OR)
                .addCriterion("earth_language", LinguisticsComprehensionTrigger.TriggerInstance.atLeast(BookLanguagesPM.EARTH, threshold))
                .addCriterion("sea_language", LinguisticsComprehensionTrigger.TriggerInstance.atLeast(BookLanguagesPM.SEA, threshold))
                .addCriterion("sky_language", LinguisticsComprehensionTrigger.TriggerInstance.atLeast(BookLanguagesPM.SKY, threshold))
                .addCriterion("sun_language", LinguisticsComprehensionTrigger.TriggerInstance.atLeast(BookLanguagesPM.SUN, threshold))
                .addCriterion("moon_language", LinguisticsComprehensionTrigger.TriggerInstance.atLeast(BookLanguagesPM.MOON, threshold))
                .addCriterion("trade_language", LinguisticsComprehensionTrigger.TriggerInstance.atLeast(BookLanguagesPM.TRADE, threshold))
                .addCriterion("forbidden_language", LinguisticsComprehensionTrigger.TriggerInstance.atLeast(BookLanguagesPM.FORBIDDEN, threshold))
                .addCriterion("hallowed_language", LinguisticsComprehensionTrigger.TriggerInstance.atLeast(BookLanguagesPM.HALLOWED, threshold));
        if (type == AdvancementType.CHALLENGE) {
            builder.rewards(AdvancementRewards.Builder.experience(100));
        }
        return builder.save(saver, PrimalMagick.resource("story/" + id));
    }
    
    private static AdvancementHolder makeRunescribingAdvancement(HolderLookup.Provider registries, String id, ItemLike icon, AdvancementType type, AdvancementHolder parent, boolean requireAll, Consumer<AdvancementHolder> saver) {
        Advancement.Builder builder = Advancement.Builder.advancement().display(DisplayInfoBuilder.id(id).icon(icon).type(type).build())
                .parent(parent)
                .requirements(requireAll ? AdvancementRequirements.Strategy.AND : AdvancementRequirements.Strategy.OR);
        registries.lookupOrThrow(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS).listElements().sorted(Comparator.comparing(defHolder -> defHolder.key().location().toString())).forEach(defHolder -> {
            builder.addCriterion(defHolder.key().location().toString(), RunescribingTrigger.TriggerInstance.enchantment(defHolder.value().result()));
        });
        if (type == AdvancementType.CHALLENGE) {
            builder.rewards(AdvancementRewards.Builder.experience(100));
        }
        return builder.save(saver, PrimalMagick.resource("story/" + id));
    }
    
    private static AdvancementHolder makeBombAdvancement(HolderLookup.Provider registries, String id, ItemStack icon, AdvancementType type, AdvancementHolder parent, boolean requireAll, Consumer<AdvancementHolder> saver) {
        Advancement.Builder builder = Advancement.Builder.advancement().display(DisplayInfoBuilder.id(id).icon(icon).type(type).build())
                .parent(parent)
                .requirements(requireAll ? AdvancementRequirements.Strategy.AND : AdvancementRequirements.Strategy.OR);
        registries.lookupOrThrow(Registries.POTION).listElements().filter(potHolder -> !potHolder.value().getEffects().isEmpty()).sorted(Comparator.comparing(potHolder -> potHolder.key().location().toString())).forEach(potHolder -> {
            builder.addCriterion(potHolder.key().location().toString(), InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemsPM.ALCHEMICAL_BOMB.get()).isPotion(potHolder.value())));
        });
        if (type == AdvancementType.CHALLENGE) {
            builder.rewards(AdvancementRewards.Builder.experience(100));
        }
        return builder.save(saver, PrimalMagick.resource("story/" + id));
    }
}
