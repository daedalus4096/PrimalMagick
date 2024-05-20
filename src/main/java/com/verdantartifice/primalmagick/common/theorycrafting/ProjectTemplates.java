package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.stream.Stream;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ItemProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ItemTagProjectMaterial;
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
    public static final ResourceKey<ProjectTemplate> TRADE = create("trade");
    
    public static ResourceKey<ProjectTemplate> create(String name) {
        return ResourceKey.create(RegistryKeysPM.PROJECT_TEMPLATES, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<ProjectTemplate> context) {
        // TODO Initialize theorycrafting project templates
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
