package com.verdantartifice.primalmagick.datagen.theorycrafting;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.QuorumResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.tags.ItemTagsForgeExt;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class ProjectProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final PackOutput packOutput;
    
    public ProjectProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        Map<ResourceLocation, IFinishedProject> map = new HashMap<>();
        this.registerProjects((project) -> {
            if (map.put(project.getId(), project) != null) {
                LOGGER.debug("Duplicate theorycrafting project in data generation: {}", project.getId().toString());
            }
        });
        map.entrySet().forEach(entry -> {
            futuresBuilder.add(DataProvider.saveStable(cache, entry.getValue().getProjectJson(), this.getPath(this.packOutput, entry.getKey())));
        });
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    private Path getPath(PackOutput output, ResourceLocation entryLoc) {
        return output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(entryLoc.getNamespace()).resolve("theorycrafting").resolve(entryLoc.getPath() + ".json");
    }
    
    protected void registerProjects(Consumer<IFinishedProject> consumer) {
        SimpleResearchKey shardSynthesis = ResearchNames.SHARD_SYNTHESIS.get().simpleKey();
        
        ProjectBuilder.project("runework").requiredResearch(QuorumResearchKey.builder(3).add("RUNE_EARTH", "RUNE_SEA", "RUNE_SKY", "RUNE_SUN", "RUNE_MOON", "RUNE_PROJECT", "RUNE_PROTECT", "RUNE_ITEM", "RUNE_SELF").build())
            .weightFunction(ProgressiveWeightFunctionBuilder.start(5).modifier("EXPERT_RUNEWORKING", -1).modifier("MASTER_RUNEWORKING", -1).modifier("SUPREME_RUNEWORKING", -2).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNECARVING_TABLE.get(), false).weight(5).build())
            .material(ItemMaterialBuilder.item(Items.STONE_SLAB, true).weight(3).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.GEMS_LAPIS, true).weight(3).build())
            .material(ItemMaterialBuilder.item(Items.DIAMOND_SWORD, false).weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNE_EARTH.get(), true).requiredResearch("RUNE_EARTH").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNE_SEA.get(), true).requiredResearch("RUNE_SEA").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNE_SKY.get(), true).requiredResearch("RUNE_SKY").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNE_SUN.get(), true).requiredResearch("RUNE_SUN").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNE_MOON.get(), true).requiredResearch("RUNE_MOON").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNE_PROJECT.get(), true).requiredResearch("RUNE_PROJECT").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNE_PROTECT.get(), true).requiredResearch("RUNE_PROTECT").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNE_ITEM.get(), true).requiredResearch("RUNE_ITEM").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RUNE_SELF.get(), true).requiredResearch("RUNE_SELF").weight(1).build())
            .build(consumer);
        ProjectBuilder.project("spellwork").requiredResearch("BASIC_SORCERY")
            .weightFunction(ProgressiveWeightFunctionBuilder.start(5).modifier("EXPERT_SORCERY", -1).modifier("MASTER_SORCERY", -1).modifier("SUPREME_SORCERY", -2).build())
            .material(ItemMaterialBuilder.item(ItemsPM.SPELLCRAFTING_ALTAR.get(), false).weight(5).build())
            .material(ItemMaterialBuilder.item(ItemsPM.WAND_INSCRIPTION_TABLE.get(), false).requiredResearch("WAND_INSCRIPTION").weight(2).build())
            .material(ItemMaterialBuilder.item(ItemsPM.WAND_CHARGER.get(), false).requiredResearch("WAND_CHARGER").weight(2).build())
            .material(ItemMaterialBuilder.item(ItemsPM.MUNDANE_WAND.get(), false).weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.SPELL_SCROLL_BLANK.get(), true).weight(5).build())
            .material(ItemMaterialBuilder.item(ItemsPM.ESSENCE_DUST_EARTH.get(), true).weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.ESSENCE_DUST_SEA.get(), true).weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.ESSENCE_DUST_SKY.get(), true).weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.ESSENCE_DUST_SUN.get(), true).weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.ESSENCE_DUST_MOON.get(), true).weight(1).build())
            .material(ObservationMaterialBuilder.observation(1, true).weight(5).build())
            .build(consumer);
        ProjectBuilder.project("wand_tinkering").requiredResearch("BASIC_MANAWEAVING")
            .weightFunction(ProgressiveWeightFunctionBuilder.start(5).modifier("MASTER_MANAWEAVING", -2).build())
            .material(ItemMaterialBuilder.item(ItemsPM.WAND_ASSEMBLY_TABLE.get(), false).requiredResearch("ADVANCED_WANDMAKING").weight(3).build())
            .material(ItemMaterialBuilder.item(ItemsPM.HEARTWOOD.get(), true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.OBSIDIAN, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTagsPM.CORAL_BLOCKS, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.BAMBOO, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTagsPM.SUNWOOD_LOGS, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTagsPM.MOONWOOD_LOGS, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.INGOTS_IRON, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.INGOTS_GOLD, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.GEMS_DIAMOND, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS, true).weight(1).build())
            .material(ObservationMaterialBuilder.observation(1, true).weight(5).build())
            .build(consumer);
    }

    @Override
    public String getName() {
        return "Primal Magick Theorycrafting Projects";
    }
}
