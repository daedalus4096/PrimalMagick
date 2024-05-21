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
        
        ProjectBuilder.project("mundane_tinkering")
            .weightFunction(ProgressiveWeightFunctionBuilder.start(5).modifier("BASIC_MAGITECH", -1).modifier("EXPERT_MAGITECH", -1).modifier("MASTER_MAGITECH", -1).modifier("SUPREME_MAGITECH", -1).build())
            .material(ItemMaterialBuilder.item(Items.CRAFTING_TABLE, false).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTags.ANVIL, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.FURNACE, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.BLAST_FURNACE, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.LOOM, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.SMOKER, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.SMITHING_TABLE, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.STONECUTTER, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GRINDSTONE, false).weight(1).build())
            .material(ObservationMaterialBuilder.observation(1, true).weight(5).build())
            .build(consumer);
        ProjectBuilder.project("nether_expedition").requiredResearch(ResearchEntries.DISCOVER_INFERNAL).rewardMultiplier(0.5D)
            .weightFunction(ProgressiveWeightFunctionBuilder.start(5).modifier(ResearchEntries.DISCOVER_VOID, -2).build())
            .material(ItemMaterialBuilder.item(Items.DIAMOND_SWORD, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.CROSSBOW, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.ARROW, 16, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DIAMOND_CHESTPLATE, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.MAP, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.CARTOGRAPHY_TABLE, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.TORCH, 16, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.BREAD, 4, true).weight(1).build())
            .material(ItemMaterialBuilder.item(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE), true).bonusReward(0.25D).weight(4).matchNbt().build())
            .material(ItemTagMaterialBuilder.tag(ItemTagsForgeExt.MILK, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.OBSIDIAN, 10, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.FLINT_AND_STEEL, false).weight(1).build())
            .build(consumer);
        ProjectBuilder.project("observation_analysis").aid(BlocksPM.ANALYSIS_TABLE.get()).materialCountOverride(1).baseSuccessChanceOverride(0.5D).rewardMultiplier(0.5D)
            .weightFunction(ConstantWeightFunctionBuilder.weight(5).build())
            .material(ObservationMaterialBuilder.observation(1, true).weight(1).build())
            .build(consumer);
        ProjectBuilder.project("piglin_barter").requiredResearch(ResearchEntries.DISCOVER_INFERNAL).rewardMultiplier(1D)
            .weightFunction(ConstantWeightFunctionBuilder.weight(5).build())
            .material(ItemMaterialBuilder.item(Items.BELL, true).weight(0.5D).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.STORAGE_BLOCKS_GOLD, true).bonusReward(0.5D).weight(0.5D).build())
            .material(ItemMaterialBuilder.item(Items.RAW_GOLD_BLOCK, true).bonusReward(0.5D).weight(0.5D).build())
            .material(ItemMaterialBuilder.item(Items.CLOCK, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GILDED_BLACKSTONE, true).weight(2).build())
            .material(ItemMaterialBuilder.item(Items.GLISTERING_MELON_SLICE, true).weight(2).build())
            .material(ItemMaterialBuilder.item(Items.RAW_GOLD, true).weight(2).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.INGOTS_GOLD, true).weight(5).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_APPLE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_AXE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_BOOTS, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_CARROT, true).weight(2).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_CHESTPLATE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_HELMET, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_HOE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_HORSE_ARMOR, true).bonusReward(0.5D).weight(0.5D).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_LEGGINGS, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_PICKAXE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_SHOVEL, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_SWORD, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.LIGHT_WEIGHTED_PRESSURE_PLATE, true).weight(1).build())
            .otherReward(LootTableRewardBuilder.table(new ResourceLocation("gameplay/piglin_bartering"), "label.primalmagick.loot_table.piglin_bartering.desc").build())
            .build(consumer);
        ProjectBuilder.project("portal_detritus").aid(Blocks.NETHER_PORTAL).materialCountOverride(1).baseSuccessChanceOverride(0.5D).rewardMultiplier(0.5D)
            .weightFunction(ConstantWeightFunctionBuilder.weight(5).build())
            .material(ItemMaterialBuilder.item(ItemsPM.MAGNIFYING_GLASS.get(), false).weight(1).build())
            .build(consumer);
        ProjectBuilder.project("prosperous_trade").rewardMultiplier(0.5D).requiredResearch(ResearchEntries.DISCOVER_INFERNAL)
            .weightFunction(ProgressiveWeightFunctionBuilder.start(5).modifier(ResearchEntries.DISCOVER_VOID, -2).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.GEMS_EMERALD, 2, true).bonusReward(0.5D).weight(2).build())
            .material(ItemMaterialBuilder.item(Items.LAVA_BUCKET, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.GEMS_DIAMOND, true).bonusReward(0.25D).weight(3).build())
            .material(ItemMaterialBuilder.item(Items.COAL, 2, true).weight(2).build())
            .material(ItemMaterialBuilder.item(Items.MUTTON, 2, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.BEEF, 2, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.COMPASS, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.INGOTS_GOLD, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GLOWSTONE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.MELON, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.CAKE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.SALMON, 2, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.TROPICAL_FISH, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.FLINT, 3, true).weight(3).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.FEATHERS, 3, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.RABBIT_HIDE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.INK_SAC, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.WRITABLE_BOOK, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GRANITE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.ANDESITE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DIORITE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.SHEARS, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTags.BEDS, true).weight(1).build())
            .otherReward(LootTableRewardBuilder.table(PrimalMagick.resource("gameplay/theorycrafting/prosperous_trade"), "label.primalmagick.loot_table.prosperous_trade.desc").build())
            .build(consumer);
        ProjectBuilder.project("raiding_the_raiders").rewardMultiplier(0.5D)
            .weightFunction(ConstantWeightFunctionBuilder.weight(5).build())
            .material(ItemMaterialBuilder.item(Raid.getLeaderBannerInstance(), true).matchNbt().bonusReward(0.25D).weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.BLOODY_FLESH.get(), true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.CROSSBOW, true).weight(3).build())
            .material(ItemMaterialBuilder.item(Items.IRON_SWORD, true).weight(3).build())
            .material(ItemMaterialBuilder.item(Items.IRON_AXE, true).weight(3).build())
            .material(ItemMaterialBuilder.item(Items.ARROW, 4, true).weight(3).build())
            .material(ItemMaterialBuilder.item(Items.GLASS_BOTTLE, 3, true).weight(3).build())
            .material(ItemMaterialBuilder.item(Items.DARK_OAK_LOG, 16, true).weight(3).build())
            .material(ItemMaterialBuilder.item(Items.CARVED_PUMPKIN, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.GEMS_EMERALD, true).bonusReward(0.25D).weight(1).build())
            .build(consumer);
        ProjectBuilder.project("recuperation")
            .weightFunction(ProgressiveWeightFunctionBuilder.start(5).modifier(ResearchEntries.DISCOVER_INFERNAL, -2).modifier(ResearchEntries.DISCOVER_VOID, -2).build())
            .material(ItemTagMaterialBuilder.tag(ItemTags.BEDS, false).weight(2).build())
            .material(ItemMaterialBuilder.item(Items.JUKEBOX, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.BOOK, false).weight(2).build())
            .material(ItemTagMaterialBuilder.tag(ItemTagsPM.FOOD_COOKED_BEEF, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTagsPM.FOOD_BAKED_POTATO, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTagsForgeExt.MILK, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.CAKE, true).bonusReward(0.125D).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.ROSE_BUSH, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.TNT, true).bonusReward(0.125D).weight(0.5D).build())
            .build(consumer);
        ProjectBuilder.project("redstone_tinkering").requiredResearch("BASIC_MAGITECH")
            .weightFunction(ProgressiveWeightFunctionBuilder.start(5).modifier("BASIC_MAGITECH", -1).modifier("EXPERT_MAGITECH", -1).modifier("MASTER_MAGITECH", -1).modifier("SUPREME_MAGITECH", -1).build())
            .material(ItemMaterialBuilder.item(Items.DETECTOR_RAIL, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.ACTIVATOR_RAIL, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DISPENSER, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DROPPER, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DAYLIGHT_DETECTOR, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.PISTON, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.HOPPER, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.REDSTONE_LAMP, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.STICKY_PISTON, false).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.COMPARATOR, false).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.DUSTS_REDSTONE, 4, true).weight(3).build())
            .material(ObservationMaterialBuilder.observation(1, true).weight(5).build())
            .build(consumer);
        ProjectBuilder.project("rich_trade").rewardMultiplier(1D).requiredResearch(ResearchEntries.DISCOVER_VOID)
            .weightFunction(ConstantWeightFunctionBuilder.weight(5).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.GEMS_EMERALD, 4, true).bonusReward(1D).weight(2).build())
            .material(ItemMaterialBuilder.item(Items.SHIELD, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DIAMOND_CHESTPLATE, true).bonusReward(0.5D).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DRIED_KELP_BLOCK, 2, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.SWEET_BERRIES, 4, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.ITEM_FRAME, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTags.BANNERS, true).weight(2).build())
            .material(ItemMaterialBuilder.item(Items.GLASS_BOTTLE, 3, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.CROPS_NETHER_WART, 4, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GOLDEN_CARROT, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.GLISTERING_MELON_SLICE, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.PUFFERFISH, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTags.BOATS, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.TRIPWIRE_HOOK, 2, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.CROSSBOW, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.LEATHER_HORSE_ARMOR, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.SADDLE, true).bonusReward(0.5D).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.CLOCK, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.NAME_TAG, true).bonusReward(0.5D).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(Tags.Items.GEMS_QUARTZ, 2, true).weight(1).build())
            .material(ItemTagMaterialBuilder.tag(ItemTags.TERRACOTTA, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.PAINTING, true).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DIAMOND_AXE, true).bonusReward(0.25D).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DIAMOND_PICKAXE, true).bonusReward(0.25D).weight(1).build())
            .material(ItemMaterialBuilder.item(Items.DIAMOND_SWORD, true).bonusReward(0.25D).weight(1).build())
            .otherReward(LootTableRewardBuilder.table(PrimalMagick.resource("gameplay/theorycrafting/rich_trade"), "label.primalmagick.loot_table.rich_trade.desc").build())
            .build(consumer);
        ProjectBuilder.project("ritual_practice").requiredResearch(QuorumResearchKey.builder(2).add("MANAFRUIT", "RITUAL_CANDLES", "INCENSE_BRAZIER", "DOWSING_ROD").build())
            .weightFunction(ProgressiveWeightFunctionBuilder.start(5).modifier("EXPERT_RITUAL", -1).modifier("MASTER_RITUAL", -1).modifier("SUPREME_RITUAL", -2).build())
            .material(ItemMaterialBuilder.item(ItemsPM.RITUAL_ALTAR.get(), false).weight(10).build())
            .material(ItemMaterialBuilder.item(ItemsPM.OFFERING_PEDESTAL.get(), false).weight(5).build())
            .material(ItemMaterialBuilder.item(ItemsPM.REFINED_SALT.get(), true).weight(3).build())
            .material(ItemMaterialBuilder.item(Items.APPLE, true).requiredResearch("MANAFRUIT").weight(1).bonusReward(0.125D).build())
            .material(ItemMaterialBuilder.item(Items.HONEY_BOTTLE, true).requiredResearch("MANAFRUIT").weight(1).bonusReward(0.125D).build())
            .material(ItemMaterialBuilder.item(ItemsPM.MANA_SALTS.get(), true).requiredResearch("MANAFRUIT").weight(1).bonusReward(0.125D).build())
            .material(ItemTagMaterialBuilder.tag(ItemTagsPM.RITUAL_CANDLES, false).requiredResearch("RITUAL_CANDLES").weight(1).build())
            .material(ItemMaterialBuilder.item(Items.FLINT_AND_STEEL, false).requiredResearch("RITUAL_CANDLES").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.INCENSE_BRAZIER.get(), false).requiredResearch("INCENSE_BRAZIER").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.INCENSE_STICK.get(), true).requiredResearch("INCENSE_BRAZIER").weight(1).build())
            .material(ItemMaterialBuilder.item(ItemsPM.DOWSING_ROD.get(), false).requiredResearch("DOWSING_ROD").weight(1).build())
            .build(consumer);
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
