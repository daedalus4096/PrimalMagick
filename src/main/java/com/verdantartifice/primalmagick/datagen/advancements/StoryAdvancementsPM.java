package com.verdantartifice.primalmagick.datagen.advancements;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.advancements.critereon.LinguisticsComprehensionTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.ResearchCompletedTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.RunescribingTrigger;
import com.verdantartifice.primalmagick.common.advancements.critereon.StatValueTrigger;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.init.InitAdvancements;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
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
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
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
}
