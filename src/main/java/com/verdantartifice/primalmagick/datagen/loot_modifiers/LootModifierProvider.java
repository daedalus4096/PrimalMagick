package com.verdantartifice.primalmagick.datagen.loot_modifiers;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.PrimalMagick;
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
import com.verdantartifice.primalmagick.common.tags.BlockTagsForgeExt;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsForgeExt;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingHookPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

/**
 * Data provider for all of the mod's loot modifiers.
 * 
 * @author Daedalus4096
 */
public class LootModifierProvider extends GlobalLootModifierProvider {
    public LootModifierProvider(PackOutput packOutput) {
        super(packOutput, PrimalMagick.MODID);
    }

    @Override
    protected void start() {
        this.add("bloody_flesh", new BloodyFleshModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_BLOODY_FLESH)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.1F).build()
                }));
        this.add("bounty_farming", new BountyFarmingModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(BlockTagsPM.BOUNTY_CROPS).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.BOUNTY.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.25F));
        this.add("bounty_fishing", new BountyFishingModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(FishingHookPredicate.inOpenWater(true))).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.BOUNTY.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.25F));
        this.add("lucky_strike", new BonusNuggetModifier(
                new LootItemCondition[] {
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, ImmutableMap.<TagKey<Block>, TagKey<Item>>builder()
                    .put(Tags.Blocks.ORES_IRON, Tags.Items.NUGGETS_IRON)
                    .put(Tags.Blocks.ORES_GOLD, Tags.Items.NUGGETS_GOLD)
                    .put(Tags.Blocks.ORES_QUARTZ, ItemTagsForgeExt.NUGGETS_QUARTZ)
                    .put(Tags.Blocks.ORES_COPPER, ItemTagsForgeExt.NUGGETS_COPPER)
                    .put(BlockTagsForgeExt.ORES_TIN, ItemTagsForgeExt.NUGGETS_TIN)
                    .put(BlockTagsForgeExt.ORES_LEAD, ItemTagsForgeExt.NUGGETS_LEAD)
                    .put(BlockTagsForgeExt.ORES_SILVER, ItemTagsForgeExt.NUGGETS_SILVER)
                    .put(BlockTagsForgeExt.ORES_URANIUM, ItemTagsForgeExt.NUGGETS_URANIUM)
                    .build()
                , 0.5F));
        this.add("blood_notes_high", new BloodNotesModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_BLOOD_NOTES_HIGH)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }));
        this.add("blood_notes_low", new BloodNotesModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_BLOOD_NOTES_LOW)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.25F, 0.25F).build()
                }));
        this.add("relic_fragments_high", new RelicFragmentsModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_HIGH)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, 3, 5, 1));
        this.add("relic_fragments_low", new RelicFragmentsModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_LOW)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.05F).build()
                }, 1, 1, 0));
        this.add("four_leaf_clover_short_grass", new FourLeafCloverModifier(
                new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.004F, 0.003F).build()
                }));
        this.add("four_leaf_clover_tall_grass", new FourLeafCloverModifier(
                new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS).build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.004F, 0.003F).build()
                }));
        this.add("humming_artifact_abandoned_mineshaft", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_ancient_city", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_bastion_treasure", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.BASTION_TREASURE).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_buried_treasure", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_desert_pyramid", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.DESERT_PYRAMID).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_end_city_treasure", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.END_CITY_TREASURE).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_igloo_chest", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.IGLOO_CHEST).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_jungle_temple", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.JUNGLE_TEMPLE).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_nether_fortress", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.NETHER_BRIDGE).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_pillager_outpost", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.PILLAGER_OUTPOST).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_shipwreck_treasure", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_simple_dungeon", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON).build(),
                        LootItemRandomChanceCondition.randomChance(0.4F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_stronghold_library", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_LIBRARY).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_underwater_ruin_big", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }, ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        this.add("humming_artifact_woodland_mansion", new AddItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.WOODLAND_MANSION).build(),
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
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.GUILLOTINE_ZOMBIE_HEAD)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, Items.ZOMBIE_HEAD, 0.1F));
        this.add("guillotine_skeleton_skull", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.GUILLOTINE_SKELETON_SKULL)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, Items.SKELETON_SKULL, 0.1F));
        this.add("guillotine_creeper_head", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.GUILLOTINE_CREEPER_HEAD)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, Items.CREEPER_HEAD, 0.1F));
        this.add("guillotine_dragon_head", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.GUILLOTINE_DRAGON_HEAD)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, Items.DRAGON_HEAD, 0.1F));
        this.add("guillotine_wither_skeleton_skull", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.GUILLOTINE_WITHER_SKELETON_SKULL)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, Items.WITHER_SKELETON_SKULL, 0.1F));
        this.add("guillotine_piglin_head", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.GUILLOTINE_PIGLIN_HEAD)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, Items.PIGLIN_HEAD, 0.1F));
        this.add("guillotine_player_head", new GuillotineModifier(
                new LootItemCondition[] {
                        // TODO Use the MatchTool loot predicate if the tool parameter is ever included for entity kills
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.GUILLOTINE_PLAYER_HEAD)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, Items.PLAYER_HEAD, 0.1F));
        // TODO Re-add archaeology and structure chest loot modifiers once the book project is ready to deploy
    }
}
