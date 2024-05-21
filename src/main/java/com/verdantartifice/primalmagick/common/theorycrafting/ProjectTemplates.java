package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.stream.Stream;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ExperienceProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ItemProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ItemTagProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ObservationProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.ExperienceReward;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.ItemReward;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.LootTableReward;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.ConstantWeight;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.ProgressiveWeight;

import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

/**
 * Datapack registry for the mod's theorycrafting project templates.
 * 
 * @author Daedalus4096
 */
public class ProjectTemplates {
    // TODO Define other project template keys
    public static final ResourceKey<ProjectTemplate> ADVANCED_ENCHANTING_STUDIES = create("advanced_enchanting_studies");
    public static final ResourceKey<ProjectTemplate> ADVANCED_ESSENCE_ANALYSIS = create("advanced_essence_analysis");
    public static final ResourceKey<ProjectTemplate> ADVANCED_RITUAL_PRACTICE = create("advanced_ritual_practice");
    public static final ResourceKey<ProjectTemplate> ADVANCED_RUNEWORK = create("advanced_runework");
    public static final ResourceKey<ProjectTemplate> ADVANCED_SPELLWORK = create("advanced_spellwork");
    public static final ResourceKey<ProjectTemplate> ADVANCED_WAND_TINKERING = create("advanced_wand_tinkering");
    public static final ResourceKey<ProjectTemplate> APIAMANCY = create("apiamancy");
    public static final ResourceKey<ProjectTemplate> BEACON_EMANATIONS = create("beacon_emanations");
    public static final ResourceKey<ProjectTemplate> BREWING_EXPERIMENTS = create("brewing_experiments");
    public static final ResourceKey<ProjectTemplate> CONDUIT_FORCES = create("conduit_forces");
    public static final ResourceKey<ProjectTemplate> DRACONIC_ENERGIES = create("draconic_energies");
    public static final ResourceKey<ProjectTemplate> DRACONIC_MEMORIES = create("draconic_memories");
    public static final ResourceKey<ProjectTemplate> ENCHANTING_STUDIES = create("enchanting_studies");
    public static final ResourceKey<ProjectTemplate> END_EXPEDITION = create("end_expedition");
    public static final ResourceKey<ProjectTemplate> ESSENCE_ANALYSIS = create("essence_analysis");
    public static final ResourceKey<ProjectTemplate> EXPEDITION = create("expedition");
    public static final ResourceKey<ProjectTemplate> HIT_THE_BOOKS = create("hit_the_books");
    public static final ResourceKey<ProjectTemplate> MAGITECH_TINKERING = create("magitech_tinkering");
    public static final ResourceKey<ProjectTemplate> MASTER_ENCHANTING_STUDIES = create("master_enchanting_studies");
    public static final ResourceKey<ProjectTemplate> MASTER_ESSENCE_ANALYSIS = create("master_essence_analysis");
    public static final ResourceKey<ProjectTemplate> MASTER_RITUAL_PRACTICE = create("master_ritual_practice");
    public static final ResourceKey<ProjectTemplate> MASTER_RUNEWORK = create("master_runework");
    public static final ResourceKey<ProjectTemplate> MASTER_SPELLWORK = create("master_spellwork");
    public static final ResourceKey<ProjectTemplate> TRADE = create("trade");
    
    public static ResourceKey<ProjectTemplate> create(String name) {
        return ResourceKey.create(RegistryKeysPM.PROJECT_TEMPLATES, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<ProjectTemplate> context) {
        // TODO Initialize theorycrafting project templates
        context.register(ADVANCED_ENCHANTING_STUDIES, ProjectTemplate.builder().rewardMultiplier(0.5D)
                .requiredResearch(ResearchEntries.EXPERT_MANAWEAVING)
                .requiredResearch(ResearchEntries.PRIMALITE)
                .weightFunction(ProgressiveWeight.builder(5).modifier(ResearchEntries.MASTER_MANAWEAVING, -1).modifier(ResearchEntries.SUPREME_MANAWEAVING, -1).build())
                .material(ItemTagProjectMaterial.builder(ItemTagsPM.ENCHANTING_TABLES).weight(5).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.GEMS_LAPIS).quantity(2).consumed().weight(5).build())
                .material(ExperienceProjectMaterial.builder(2).consumed().bonusReward(0.25D).weight(5).build())
                .material(ItemProjectMaterial.builder(Items.BOOK).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_SWORD.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_PICKAXE.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_SHOVEL.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_AXE.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_HOE.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_HEAD.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_CHEST.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_LEGS.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_FEET.get()).weight(1).build())
                .material(ObservationProjectMaterial.builder(1).consumed().weight(5).build())
                .build());
        context.register(ADVANCED_ESSENCE_ANALYSIS, ProjectTemplate.builder().requiredResearch(ResearchEntries.CRYSTAL_SYNTHESIS).rewardMultiplier(0.5D)
                .weightFunction(ProgressiveWeight.builder(5).modifier(ResearchEntries.CLUSTER_SYNTHESIS, -2).build())
                .material(ObservationProjectMaterial.builder(1).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_EARTH.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SEA.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SKY.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SUN.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_MOON.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_BLOOD.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_BLOOD).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_VOID.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_VOID).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_HALLOWED.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_EARTH.get()).consumed().afterCrafting(5).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_SEA.get()).consumed().afterCrafting(5).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_SKY.get()).consumed().afterCrafting(5).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_SUN.get()).consumed().afterCrafting(5).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_MOON.get()).consumed().afterCrafting(5).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_BLOOD.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_BLOOD).afterCrafting(5).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).afterCrafting(5).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_VOID.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_VOID).afterCrafting(5).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_HALLOWED.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).afterCrafting(5).bonusReward(0.25D).weight(1).build())
                .build());
        context.register(ADVANCED_RITUAL_PRACTICE, ProjectTemplate.builder().rewardMultiplier(0.5D)
                .quorumResearch(2, ResearchEntries.RITUAL_LECTERN, ResearchEntries.RITUAL_BELL, ResearchEntries.PRIMAL_SHOVEL, ResearchEntries.PRIMAL_FISHING_ROD, ResearchEntries.PRIMAL_AXE, ResearchEntries.PRIMAL_HOE, ResearchEntries.PRIMAL_PICKAXE)
                .weightFunction(ProgressiveWeight.builder(3).modifier(ResearchEntries.BASIC_RITUAL, 1).modifier(ResearchEntries.EXPERT_RITUAL, 1).modifier(ResearchEntries.MASTER_RITUAL, -1).modifier(ResearchEntries.SUPREME_RITUAL, -1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RITUAL_ALTAR.get()).weight(10).build())
                .material(ItemProjectMaterial.builder(ItemsPM.OFFERING_PEDESTAL.get()).weight(5).build())
                .material(ItemProjectMaterial.builder(ItemsPM.REFINED_SALT.get(), 2).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RITUAL_LECTERN.get()).requiredResearch(ResearchEntries.RITUAL_LECTERN).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.ENCHANTED_BOOK).requiredResearch(ResearchEntries.RITUAL_LECTERN).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RITUAL_BELL.get()).requiredResearch(ResearchEntries.RITUAL_BELL).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_SHOVEL.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_SHOVEL).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_EARTH.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_SHOVEL).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_FISHING_ROD.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_FISHING_ROD).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SEA.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_FISHING_ROD).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_AXE.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_AXE).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SKY.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_AXE).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_HOE.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_HOE).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SUN.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_HOE).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.PRIMALITE_PICKAXE.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_PICKAXE).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_MOON.get()).consumed().requiredResearch(ResearchEntries.PRIMAL_PICKAXE).bonusReward(0.25D).weight(1).build())
                .build());
        context.register(ADVANCED_RUNEWORK, ProjectTemplate.builder().rewardMultiplier(0.5D)
                .quorumResearch(3, ResearchEntries.RUNE_BLOOD, ResearchEntries.RUNE_INFERNAL, ResearchEntries.RUNE_VOID, ResearchEntries.RUNE_ABSORB, ResearchEntries.RUNE_DISPEL, ResearchEntries.RUNE_SUMMON, ResearchEntries.RUNE_AREA, ResearchEntries.RUNE_CREATURE, ResearchEntries.RUNE_INSIGHT)
                .weightFunction(ProgressiveWeight.builder(3).modifier(ResearchEntries.BASIC_RUNEWORKING, 1).modifier(ResearchEntries.EXPERT_RUNEWORKING, 1).modifier(ResearchEntries.MASTER_RUNEWORKING, -1).modifier(ResearchEntries.SUPREME_RUNEWORKING, -1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNECARVING_TABLE.get()).weight(5).build())
                .material(ItemProjectMaterial.builder(Items.STONE_SLAB, 2).consumed().weight(3).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.GEMS_LAPIS).quantity(2).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(Items.DIAMOND_SWORD).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_BLOOD.get()).consumed().requiredResearch(ResearchEntries.RUNE_BLOOD).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.RUNE_INFERNAL).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_VOID.get()).consumed().requiredResearch(ResearchEntries.RUNE_VOID).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_INSIGHT.get()).consumed().requiredResearch(ResearchEntries.RUNE_INSIGHT).bonusReward(0.25D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_ABSORB.get()).consumed().requiredResearch(ResearchEntries.RUNE_ABSORB).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_DISPEL.get()).consumed().requiredResearch(ResearchEntries.RUNE_DISPEL).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_SUMMON.get()).consumed().requiredResearch(ResearchEntries.RUNE_SUMMON).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_AREA.get()).consumed().requiredResearch(ResearchEntries.RUNE_AREA).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_CREATURE.get()).consumed().requiredResearch(ResearchEntries.RUNE_CREATURE).weight(1).build())
                .build());
        context.register(ADVANCED_SPELLWORK, ProjectTemplate.builder().rewardMultiplier(0.5D)
                .requiredResearch(ResearchEntries.EXPERT_SORCERY)
                .requiredResearch(ResearchEntries.SHARD_SYNTHESIS)
                .weightFunction(ProgressiveWeight.builder(5).modifier(ResearchEntries.MASTER_SORCERY, -1).modifier(ResearchEntries.SUPREME_SORCERY, -1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.SPELLCRAFTING_ALTAR.get()).weight(5).build())
                .material(ItemProjectMaterial.builder(ItemsPM.WAND_INSCRIPTION_TABLE.get()).requiredResearch(ResearchEntries.WAND_INSCRIPTION).weight(2).build())
                .material(ItemProjectMaterial.builder(ItemsPM.AUTO_CHARGER.get()).requiredResearch(ResearchEntries.AUTO_CHARGER).weight(2).build())
                .material(ItemProjectMaterial.builder(ItemsPM.MUNDANE_WAND.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.SPELL_SCROLL_BLANK.get(), 2).consumed().weight(5).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_EARTH.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SEA.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SKY.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SUN.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_MOON.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_BLOOD.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_BLOOD).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_VOID.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_VOID).weight(1).build())
                .material(ObservationProjectMaterial.builder(1).consumed().weight(5).build())
                .build());
        context.register(ADVANCED_WAND_TINKERING, ProjectTemplate.builder().rewardMultiplier(0.5D).requiredResearch(ResearchEntries.MASTER_MANAWEAVING)
                .weightFunction(new ConstantWeight(5))
                .material(ItemProjectMaterial.builder(ItemsPM.WAND_ASSEMBLY_TABLE.get()).requiredResearch(ResearchEntries.ADVANCED_WANDMAKING).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.WAND_GLAMOUR_TABLE.get()).requiredResearch(ResearchEntries.WAND_GLAMOUR_TABLE).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEARTWOOD.get(), 2).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.BONES).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.RODS_BLAZE).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.PURPUR_BLOCK).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(ItemTagsPM.INGOTS_PRIMALITE).consumed().requiredResearch(ResearchEntries.PRIMALITE).weight(1).build())
                .material(ItemTagProjectMaterial.builder(ItemTagsPM.INGOTS_HEXIUM).consumed().requiredResearch(ResearchEntries.HEXIUM).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_BLOOD.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_BLOOD).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_VOID.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_VOID).weight(1).build())
                .build());
        context.register(APIAMANCY, ProjectTemplate.builder().aid(Blocks.BEEHIVE).materialCountOverride(1).baseSuccessChanceOverride(0.5D).rewardMultiplier(0.5D)
                .weightFunction(new ConstantWeight(5))
                .material(ItemTagProjectMaterial.builder(ItemTags.SMALL_FLOWERS).consumed().weight(1).build())
                .otherReward(new ItemReward(Items.HONEYCOMB, 3))
                .build());
        context.register(BEACON_EMANATIONS, ProjectTemplate.builder().aid(Blocks.BEACON).materialCountOverride(1).baseSuccessChanceOverride(0.5D).rewardMultiplier(0.5D)
                .weightFunction(new ConstantWeight(5))
                .material(ItemTagProjectMaterial.builder(ItemTags.BEACON_PAYMENT_ITEMS).consumed().weight(1).build())
                .build());
        context.register(BREWING_EXPERIMENTS, ProjectTemplate.builder().rewardMultiplier(0.5D).requiredResearch(ResearchEntries.DISCOVER_INFERNAL)
                .weightFunction(new ConstantWeight(5))
                .material(ItemProjectMaterial.builder(Items.BREWING_STAND).weight(5).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.CROPS_NETHER_WART).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(Items.FERMENTED_SPIDER_EYE).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.DUSTS_GLOWSTONE).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.DUSTS_REDSTONE).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.SUGAR).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.RABBIT_FOOT).consumed().bonusReward(0.25D).weight(0.5D).build())
                .material(ItemProjectMaterial.builder(Items.BLAZE_POWDER).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GLISTERING_MELON_SLICE).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.SPIDER_EYE).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GHAST_TEAR).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.MAGMA_CREAM).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.PUFFERFISH).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_CARROT).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.TURTLE_HELMET).consumed().bonusReward(0.25D).weight(0.5D).build())
                .material(ItemProjectMaterial.builder(Items.PHANTOM_MEMBRANE).consumed().bonusReward(0.25D).weight(0.5D).build())
                .material(ObservationProjectMaterial.builder(1).consumed().weight(3).build())
                .build());
        context.register(CONDUIT_FORCES, ProjectTemplate.builder().aid(Blocks.CONDUIT).materialCountOverride(1).baseSuccessChanceOverride(0.5D).rewardMultiplier(0.5D)
                .weightFunction(new ConstantWeight(5))
                .material(ItemTagProjectMaterial.builder(Tags.Items.GEMS_PRISMARINE).consumed().weight(1).build())
                .build());
        context.register(DRACONIC_ENERGIES, ProjectTemplate.builder().aid(Blocks.DRAGON_EGG).materialCountOverride(1).baseSuccessChanceOverride(0.5D).rewardMultiplier(1.0D)
                .weightFunction(new ConstantWeight(5))
                .material(ItemTagProjectMaterial.builder(Tags.Items.ENDER_PEARLS).consumed().weight(1).build())
                .build());
        context.register(DRACONIC_MEMORIES, ProjectTemplate.builder().aid(Blocks.DRAGON_HEAD).aid(Blocks.DRAGON_WALL_HEAD).materialCountOverride(1).baseSuccessChanceOverride(0.5D).rewardMultiplier(0.5D)
                .weightFunction(new ConstantWeight(5))
                .material(ExperienceProjectMaterial.builder(3).consumed().weight(1).build())
                .build());
        context.register(ENCHANTING_STUDIES, ProjectTemplate.builder().requiredResearch(ResearchEntries.BASIC_MANAWEAVING)
                .weightFunction(ProgressiveWeight.builder(5).modifier(ResearchEntries.EXPERT_MANAWEAVING, -1).modifier(ResearchEntries.MASTER_MANAWEAVING, -1).modifier(ResearchEntries.SUPREME_MANAWEAVING, -2).build())
                .material(ItemTagProjectMaterial.builder(ItemTagsPM.ENCHANTING_TABLES).weight(5).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.GEMS_LAPIS).consumed().weight(5).build())
                .material(ExperienceProjectMaterial.builder(1).consumed().bonusReward(0.125D).weight(5).build())
                .material(ItemProjectMaterial.builder(Items.BOOK).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_SWORD).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_PICKAXE).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_SHOVEL).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_AXE).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_HOE).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_HELMET).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_CHESTPLATE).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_LEGGINGS).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.GOLDEN_BOOTS).weight(1).build())
                .material(ObservationProjectMaterial.builder(1).consumed().weight(5).build())
                .build());
        context.register(END_EXPEDITION, ProjectTemplate.builder().requiredResearch(ResearchEntries.DISCOVER_VOID).rewardMultiplier(1.0D)
                .weightFunction(new ConstantWeight(5))
                .material(ItemProjectMaterial.builder(Items.NETHERITE_SWORD).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.CROSSBOW).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.ARROW, 32).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.NETHERITE_CHESTPLATE).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.MAP).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.CARTOGRAPHY_TABLE).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.TORCH, 32).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.BREAD, 8).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SLOW_FALLING)).consumed().bonusReward(0.5D).weight(1).matchNbt().build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.ENDER_PEARLS).quantity(4).consumed().bonusReward(0.5D).weight(3).build())
                .material(ItemProjectMaterial.builder(Items.ENDER_EYE).consumed().weight(1).build())
                .build());
        context.register(ESSENCE_ANALYSIS, ProjectTemplate.builder().requiredResearch(ResearchEntries.BASIC_ALCHEMY)
                .weightFunction(ProgressiveWeight.builder(5).modifier(ResearchEntries.CRYSTAL_SYNTHESIS, -2).modifier(ResearchEntries.CLUSTER_SYNTHESIS, -2).build())
                .material(ObservationProjectMaterial.builder(1).consumed().weight(5).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_EARTH.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_SEA.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_SKY.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_SUN.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_MOON.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_BLOOD.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_BLOOD).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_VOID.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_VOID).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_DUST_HALLOWED.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_EARTH.get()).consumed().requiredResearch(ResearchEntries.SHARD_SYNTHESIS).afterCrafting(5).bonusReward(0.125D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SEA.get()).consumed().requiredResearch(ResearchEntries.SHARD_SYNTHESIS).afterCrafting(5).bonusReward(0.125D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SKY.get()).consumed().requiredResearch(ResearchEntries.SHARD_SYNTHESIS).afterCrafting(5).bonusReward(0.125D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_SUN.get()).consumed().requiredResearch(ResearchEntries.SHARD_SYNTHESIS).afterCrafting(5).bonusReward(0.125D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_MOON.get()).consumed().requiredResearch(ResearchEntries.SHARD_SYNTHESIS).afterCrafting(5).bonusReward(0.125D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_BLOOD.get()).consumed().requiredResearch(ResearchEntries.SHARD_SYNTHESIS).requiredResearch(ResearchEntries.DISCOVER_BLOOD).afterCrafting(5).bonusReward(0.125D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.SHARD_SYNTHESIS).requiredResearch(ResearchEntries.DISCOVER_INFERNAL).afterCrafting(5).bonusReward(0.125D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_VOID.get()).consumed().requiredResearch(ResearchEntries.SHARD_SYNTHESIS).requiredResearch(ResearchEntries.DISCOVER_VOID).afterCrafting(5).bonusReward(0.125D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_HALLOWED.get()).consumed().requiredResearch(ResearchEntries.SHARD_SYNTHESIS).requiredResearch(ResearchEntries.DISCOVER_HALLOWED).afterCrafting(5).bonusReward(0.125D).weight(1).build())
                .build());
        context.register(EXPEDITION, ProjectTemplate.builder()
                .weightFunction(ProgressiveWeight.builder(5).modifier(ResearchEntries.DISCOVER_INFERNAL, -2).modifier(ResearchEntries.DISCOVER_VOID, -2).build())
                .material(ItemProjectMaterial.builder(Items.IRON_SWORD).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.BOW).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.ARROW, 4).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.IRON_CHESTPLATE).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.MAP).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.COMPASS).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.CLOCK).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.CARTOGRAPHY_TABLE).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.CAMPFIRE).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.TORCH, 4).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.BREAD).consumed().weight(1).build())
                .build());
        context.register(HIT_THE_BOOKS, ProjectTemplate.builder().aid(Blocks.BOOKSHELF).materialCountOverride(1).baseSuccessChanceOverride(0.5D).rewardMultiplier(0.5D)
                .weightFunction(new ConstantWeight(5))
                .material(ItemProjectMaterial.builder(Items.BOOK).weight(1).build())
                .otherReward(new ExperienceReward(5))
                .build());
        context.register(MAGITECH_TINKERING, ProjectTemplate.builder().rewardMultiplier(0.5D)
                .quorumResearch(3, ResearchEntries.ARCANOMETER, ResearchEntries.CONCOCTING_TINCTURES, ResearchEntries.ENTROPY_SINK, ResearchEntries.AUTO_CHARGER, ResearchEntries.ESSENCE_TRANSMUTER, ResearchEntries.DISSOLUTION_CHAMBER, ResearchEntries.INFERNAL_FURNACE)
                .weightFunction(ProgressiveWeight.builder(3).modifier(ResearchEntries.MASTER_MAGITECH, 2).build())
                .material(ItemProjectMaterial.builder(ItemsPM.MAGITECH_PARTS_BASIC.get()).consumed().bonusReward(0.125D).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.MAGITECH_PARTS_ENCHANTED.get()).consumed().requiredResearch(ResearchEntries.EXPERT_MAGITECH).bonusReward(0.25D).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.MAGITECH_PARTS_FORBIDDEN.get()).consumed().requiredResearch(ResearchEntries.MASTER_MAGITECH).bonusReward(0.5D).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HONEY_EXTRACTOR.get()).requiredResearch(ResearchEntries.HONEY_EXTRACTOR).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ARCANOMETER.get()).requiredResearch(ResearchEntries.ARCANOMETER).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.CONCOCTER.get()).requiredResearch(ResearchEntries.CONCOCTING_TINCTURES).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ENTROPY_SINK.get()).requiredResearch(ResearchEntries.ENTROPY_SINK).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.AUTO_CHARGER.get()).requiredResearch(ResearchEntries.AUTO_CHARGER).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_TRANSMUTER.get()).requiredResearch(ResearchEntries.ESSENCE_TRANSMUTER).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.DISSOLUTION_CHAMBER.get()).requiredResearch(ResearchEntries.DISSOLUTION_CHAMBER).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.INFERNAL_FURNACE.get()).requiredResearch(ResearchEntries.INFERNAL_FURNACE).weight(1).build())
                .build());
        context.register(MASTER_ENCHANTING_STUDIES, ProjectTemplate.builder().rewardMultiplier(1D)
                .requiredResearch(ResearchEntries.MASTER_MANAWEAVING)
                .requiredResearch(ResearchEntries.HEXIUM)
                .weightFunction(new ConstantWeight(5))
                .material(ItemTagProjectMaterial.builder(ItemTagsPM.ENCHANTING_TABLES).weight(5).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.GEMS_LAPIS).quantity(3).consumed().weight(5).build())
                .material(ExperienceProjectMaterial.builder(3).consumed().bonusReward(0.5D).weight(5).build())
                .material(ItemProjectMaterial.builder(Items.BOOK).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_SWORD.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_PICKAXE.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_SHOVEL.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_AXE.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_HOE.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_HEAD.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_CHEST.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_LEGS.get()).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_FEET.get()).weight(1).build())
                .material(ObservationProjectMaterial.builder(1).consumed().weight(5).build())
                .build());
        context.register(MASTER_ESSENCE_ANALYSIS, ProjectTemplate.builder().requiredResearch(ResearchEntries.CLUSTER_SYNTHESIS).rewardMultiplier(1.0D)
                .weightFunction(new ConstantWeight(5))
                .material(ObservationProjectMaterial.builder(1).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_EARTH.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_SEA.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_SKY.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_SUN.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_MOON.get()).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_BLOOD.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_BLOOD).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_VOID.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_VOID).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_HALLOWED.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).afterCrafting(5).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CLUSTER_EARTH.get()).consumed().afterCrafting(5).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CLUSTER_SEA.get()).consumed().afterCrafting(5).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CLUSTER_SKY.get()).consumed().afterCrafting(5).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CLUSTER_SUN.get()).consumed().afterCrafting(5).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CLUSTER_MOON.get()).consumed().afterCrafting(5).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CLUSTER_BLOOD.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_BLOOD).afterCrafting(5).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CLUSTER_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).afterCrafting(5).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CLUSTER_VOID.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_VOID).afterCrafting(5).weight(1).bonusReward(0.5D).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CLUSTER_HALLOWED.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_HALLOWED).afterCrafting(5).bonusReward(0.5D).weight(1).build())
                .build());
        context.register(MASTER_RITUAL_PRACTICE, ProjectTemplate.builder().rewardMultiplier(1D)
                .quorumResearch(2, ResearchEntries.BLOODLETTER, ResearchEntries.SOUL_ANVIL, ResearchEntries.FORBIDDEN_TRIDENT, ResearchEntries.FORBIDDEN_SWORD, ResearchEntries.FORBIDDEN_BOW)
                .weightFunction(new ConstantWeight(5))
                .material(ItemProjectMaterial.builder(ItemsPM.RITUAL_ALTAR.get()).weight(10).build())
                .material(ItemProjectMaterial.builder(ItemsPM.OFFERING_PEDESTAL.get()).weight(5).build())
                .material(ItemProjectMaterial.builder(ItemsPM.REFINED_SALT.get(), 4).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.BLOODLETTER.get()).requiredResearch(ResearchEntries.BLOODLETTER).weight(1).build())
                .material(ItemProjectMaterial.builder(Items.WATER_BUCKET).consumed().requiredResearch(ResearchEntries.BLOODLETTER).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.SOUL_ANVIL.get()).requiredResearch(ResearchEntries.SOUL_ANVIL).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.SPELLCLOTH.get()).requiredResearch(ResearchEntries.SOUL_ANVIL).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_TRIDENT.get()).consumed().requiredResearch(ResearchEntries.FORBIDDEN_TRIDENT).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_BLOOD.get()).consumed().requiredResearch(ResearchEntries.FORBIDDEN_TRIDENT).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_BOW.get()).consumed().requiredResearch(ResearchEntries.FORBIDDEN_BOW).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.FORBIDDEN_BOW).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.HEXIUM_SWORD.get()).consumed().requiredResearch(ResearchEntries.FORBIDDEN_SWORD).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_SHARD_VOID.get()).consumed().requiredResearch(ResearchEntries.FORBIDDEN_SWORD).bonusReward(0.5D).weight(1).build())
                .build());
        context.register(MASTER_RUNEWORK, ProjectTemplate.builder().rewardMultiplier(1D).requiredResearch(ResearchEntries.RUNE_HALLOWED)
                .weightFunction(new ConstantWeight(5))
                .material(ItemProjectMaterial.builder(ItemsPM.RUNECARVING_TABLE.get()).weight(5).build())
                .material(ItemProjectMaterial.builder(Items.STONE_SLAB, 4).consumed().weight(3).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.GEMS_LAPIS).quantity(4).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(Items.DIAMOND_SWORD).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_HALLOWED.get()).consumed().requiredResearch(ResearchEntries.RUNE_HALLOWED).weight(3).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_POWER.get()).consumed().requiredResearch(ResearchEntries.RUNE_POWER).bonusReward(0.5D).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_PROJECT.get(), 2).consumed().requiredResearch(ResearchEntries.RUNE_PROJECT).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_PROTECT.get(), 2).consumed().requiredResearch(ResearchEntries.RUNE_PROTECT).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_ITEM.get(), 2).consumed().requiredResearch(ResearchEntries.RUNE_ITEM).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_SELF.get(), 2).consumed().requiredResearch(ResearchEntries.RUNE_SELF).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_ABSORB.get(), 2).consumed().requiredResearch(ResearchEntries.RUNE_ABSORB).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_DISPEL.get(), 2).consumed().requiredResearch(ResearchEntries.RUNE_DISPEL).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_SUMMON.get(), 2).consumed().requiredResearch(ResearchEntries.RUNE_SUMMON).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_AREA.get(), 2).consumed().requiredResearch(ResearchEntries.RUNE_AREA).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.RUNE_CREATURE.get(), 2).consumed().requiredResearch(ResearchEntries.RUNE_CREATURE).weight(1).build())
                .build());
        context.register(MASTER_SPELLWORK, ProjectTemplate.builder().rewardMultiplier(1D)
                .requiredResearch(ResearchEntries.MASTER_SORCERY)
                .requiredResearch(ResearchEntries.CRYSTAL_SYNTHESIS)
                .weightFunction(new ConstantWeight(5))
                .material(ItemProjectMaterial.builder(ItemsPM.SPELLCRAFTING_ALTAR.get()).weight(5).build())
                .material(ItemProjectMaterial.builder(ItemsPM.WAND_INSCRIPTION_TABLE.get()).requiredResearch(ResearchEntries.WAND_INSCRIPTION).weight(2).build())
                .material(ItemProjectMaterial.builder(ItemsPM.MANA_NEXUS.get()).requiredResearch(ResearchEntries.MANA_NEXUS).weight(2).build())
                .material(ItemProjectMaterial.builder(ItemsPM.SPELL_SCROLL_BLANK.get(), 4).consumed().weight(5).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_EARTH.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_SEA.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_SKY.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_SUN.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_MOON.get()).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_BLOOD.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_BLOOD).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_INFERNAL.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_INFERNAL).weight(1).build())
                .material(ItemProjectMaterial.builder(ItemsPM.ESSENCE_CRYSTAL_VOID.get()).consumed().requiredResearch(ResearchEntries.DISCOVER_VOID).weight(1).build())
                .material(ObservationProjectMaterial.builder(1).consumed().weight(5).build())
                .build());
        context.register(TRADE, ProjectTemplate.builder()
                .weightFunction(ProgressiveWeight.builder(5).modifier(ResearchEntries.DISCOVER_INFERNAL, -2).modifier(ResearchEntries.DISCOVER_VOID, -2).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.GEMS_EMERALD).consumed().bonusReward(0.25D).weight(2).build())
                .material(ItemProjectMaterial.builder(Items.COAL, 2).consumed().weight(3).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.INGOTS_IRON).consumed().weight(3).build())
                .material(ItemProjectMaterial.builder(Items.CHICKEN, 2).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.PORKCHOP).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.PAPER, 4).consumed().weight(2).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.GLASS_PANES).quantity(2).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.ROTTEN_FLESH, 4).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.CROPS_WHEAT).quantity(3).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.CROPS_POTATO).quantity(3).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.CROPS_CARROT).quantity(3).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.CROPS_BEETROOT).quantity(3).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.STRING).quantity(3).consumed().weight(2).build())
                .material(ItemProjectMaterial.builder(Items.COD, 2).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.RODS_WOODEN).quantity(4).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.LEATHER).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.BOOK).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.CLAY_BALL, 2).consumed().weight(1).build())
                .material(ItemProjectMaterial.builder(Items.STONE, 2).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(ItemTags.WOOL).quantity(2).consumed().weight(1).build())
                .material(ItemTagProjectMaterial.builder(Tags.Items.DYES).quantity(2).consumed().weight(1).build())
                .otherReward(LootTableReward.builder(PrimalMagick.resource("gameplay/theorycrafting/trade")).description("label.primalmagick.loot_table.trade.desc").build())
                .build());
    }
    
    public static Stream<ProjectTemplate> stream(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.PROJECT_TEMPLATES).stream();
    }
}
