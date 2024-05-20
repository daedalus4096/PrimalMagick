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
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.LootTableReward;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.ProgressiveWeight;

import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
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
