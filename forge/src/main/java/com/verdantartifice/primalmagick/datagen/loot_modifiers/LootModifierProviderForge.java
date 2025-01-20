package com.verdantartifice.primalmagick.datagen.loot_modifiers;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.loot.conditions.MatchBlockTag;
import com.verdantartifice.primalmagick.common.loot.modifiers.AddItemModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BloodNotesModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BloodyFleshModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BonusNuggetModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BountyFarmingModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BountyFishingModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.EssenceThiefModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.FourLeafCloverModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.GuillotineModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.RelicFragmentsModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.ReplaceItemModifier;
import com.verdantartifice.primalmagick.common.tags.BlockTagsForgeExt;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;
import com.verdantartifice.primalmagick.common.tags.CommonTags;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsForgeExt;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingHookPredicate;
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all of the mod's loot modifiers.
 * 
 * @author Daedalus4096
 */
public class LootModifierProviderForge extends GlobalLootModifierProvider {
    public LootModifierProviderForge(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, Constants.MOD_ID, registries);
    }

    @Override
    protected void start(HolderLookup.Provider registries) {
        HolderLookup.RegistryLookup<Enchantment> enchLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
        this.add("bloody_flesh", new BloodyFleshModifier(
                new LootItemCondition[] {
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.5F, 0.1F).build()
                }, EntityTypeTagsPM.DROPS_BLOODY_FLESH));
        this.add("bounty_farming", new BountyFarmingModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(BlockTagsPM.BOUNTY_CROPS).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().withSubPredicate(ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(enchLookup.getOrThrow(EnchantmentsPM.BOUNTY), MinMaxBounds.Ints.atLeast(1)))))).build()
                }, 0.25F));
        this.add("bounty_fishing", new BountyFishingModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(FishingHookPredicate.inOpenWater(true))).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().withSubPredicate(ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(enchLookup.getOrThrow(EnchantmentsPM.BOUNTY), MinMaxBounds.Ints.atLeast(1)))))).build()
                }, 0.25F));
        this.add("lucky_strike", new BonusNuggetModifier(
                new LootItemCondition[] {
                        MatchTool.toolMatches(ItemPredicate.Builder.item().withSubPredicate(ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(enchLookup.getOrThrow(EnchantmentsPM.LUCKY_STRIKE), MinMaxBounds.Ints.atLeast(1)))))).build()
                }, ImmutableMap.<TagKey<Block>, TagKey<Item>>builder()
                    .put(CommonTags.Blocks.ORES_IRON, CommonTags.Items.NUGGETS_IRON)
                    .put(CommonTags.Blocks.ORES_GOLD, CommonTags.Items.NUGGETS_GOLD)
                    .put(CommonTags.Blocks.ORES_QUARTZ, ItemTagsForgeExt.NUGGETS_QUARTZ)
                    .put(CommonTags.Blocks.ORES_COPPER, ItemTagsForgeExt.NUGGETS_COPPER)
                    .put(BlockTagsForgeExt.ORES_TIN, ItemTagsForgeExt.NUGGETS_TIN)
                    .put(BlockTagsForgeExt.ORES_LEAD, ItemTagsForgeExt.NUGGETS_LEAD)
                    .put(BlockTagsForgeExt.ORES_SILVER, ItemTagsForgeExt.NUGGETS_SILVER)
                    .put(BlockTagsForgeExt.ORES_URANIUM, ItemTagsForgeExt.NUGGETS_URANIUM)
                    .build()
                , 0.5F));
        this.add("blood_notes_high", new BloodNotesModifier(
                new LootItemCondition[] {
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, EntityTypeTagsPM.DROPS_BLOOD_NOTES_HIGH));
        this.add("blood_notes_low", new BloodNotesModifier(
                new LootItemCondition[] {
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.25F, 0.25F).build()
                }, EntityTypeTagsPM.DROPS_BLOOD_NOTES_LOW));
        this.add("relic_fragments_high", new RelicFragmentsModifier(
                new LootItemCondition[] {
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_HIGH, 3, 5));
        this.add("relic_fragments_low", new RelicFragmentsModifier(
                new LootItemCondition[] {
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.1F, 0.05F).build()
                }, EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_LOW, 1, 1));
        this.add("four_leaf_clover_short_grass", new FourLeafCloverModifier(
                new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SHORT_GRASS).build(),
                        LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.004F, 0.003F).build()
                }));
        this.add("four_leaf_clover_tall_grass", new FourLeafCloverModifier(
                new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS).build(),
                        LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.004F, 0.003F).build()
                }));
        this.add("humming_artifact_abandoned_mineshaft", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_ancient_city", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY.location()).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_bastion_treasure", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.BASTION_TREASURE.location()).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_buried_treasure", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_desert_pyramid", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.DESERT_PYRAMID.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_end_city_treasure", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.END_CITY_TREASURE.location()).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_igloo_chest", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.IGLOO_CHEST.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_jungle_temple", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.JUNGLE_TEMPLE.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_nether_fortress", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.NETHER_BRIDGE.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_pillager_outpost", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.PILLAGER_OUTPOST.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_shipwreck_treasure", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_simple_dungeon", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.4F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_stronghold_library", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_LIBRARY.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_underwater_ruin_big", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_woodland_mansion", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.WOODLAND_MANSION.location()).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("essence_thief", new EssenceThiefModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }));
        this.add("guillotine_zombie_head", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, EntityTypeTagsPM.GUILLOTINE_ZOMBIE_HEAD, Items.ZOMBIE_HEAD, 0.1F));
        this.add("guillotine_skeleton_skull", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, EntityTypeTagsPM.GUILLOTINE_SKELETON_SKULL, Items.SKELETON_SKULL, 0.1F));
        this.add("guillotine_creeper_head", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, EntityTypeTagsPM.GUILLOTINE_CREEPER_HEAD, Items.CREEPER_HEAD, 0.1F));
        this.add("guillotine_dragon_head", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, EntityTypeTagsPM.GUILLOTINE_DRAGON_HEAD, Items.DRAGON_HEAD, 0.1F));
        this.add("guillotine_wither_skeleton_skull", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, EntityTypeTagsPM.GUILLOTINE_WITHER_SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, 0.1F));
        this.add("guillotine_piglin_head", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, EntityTypeTagsPM.GUILLOTINE_PIGLIN_HEAD, Items.PIGLIN_HEAD, 0.1F));
        this.add("guillotine_player_head", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        // TODO Match target entity tag in loot condition if/when vanilla entity type tags are resolved before loot conditions
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, EntityTypeTagsPM.GUILLOTINE_PLAYER_HEAD, Items.PLAYER_HEAD, 0.1F));

        this.add("lore_fragment_desert_well_archaeology", new ReplaceItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.1F).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get()));
        this.add("lore_fragment_desert_pyramid_archaeology", new ReplaceItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.1F).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get()));
        this.add("lore_fragment_trail_ruins_archaeology_common", new ReplaceItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.02F).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get()));
        this.add("lore_fragment_trail_ruins_archaeology_rare", new ReplaceItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.1F).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get()));
        this.add("lore_fragment_ocean_ruin_warm_archaeology", new ReplaceItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.1F).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get()));
        this.add("lore_fragment_ocean_ruin_cold_archaeology", new ReplaceItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.1F).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get()));
        this.add("lore_fragment_underwater_ruin_small_chest", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_SMALL.location()).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get(), UniformInt.of(1, 3)));
        this.add("lore_fragment_underwater_ruin_big_chest", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG.location()).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get(), UniformInt.of(2, 6)));
        this.add("lore_fragment_desert_pyramid_chest", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.DESERT_PYRAMID.location()).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get(), UniformInt.of(2, 6)));
        this.add("lore_fragment_jungle_temple_chest", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.JUNGLE_TEMPLE.location()).build()
                }, ItemsPM.LORE_TABLET_FRAGMENT.get(), UniformInt.of(2, 6)));
    }
}
