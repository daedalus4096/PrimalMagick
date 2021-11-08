package com.verdantartifice.primalmagick.datagen.loot_modifiers;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.loot.conditions.MatchBlockTag;
import com.verdantartifice.primalmagick.common.loot.modifiers.BloodNotesModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BloodyFleshModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BonusNuggetModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BountyFarmingModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BountyFishingModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.LootModifierSerializersPM;
import com.verdantartifice.primalmagick.common.loot.modifiers.RelicFragmentsModifier;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingHookPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

/**
 * Data provider for all of the mod's loot modifiers.
 * 
 * @author Daedalus4096
 */
public class LootModifierProvider extends GlobalLootModifierProvider {
    public LootModifierProvider(DataGenerator gen) {
        super(gen, PrimalMagick.MODID);
    }

    @Override
    protected void start() {
        this.add("bloody_flesh", LootModifierSerializersPM.BLOODY_FLESH.get(), new BloodyFleshModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_BLOODY_FLESH)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.1F).build()
                }));
        this.add("bounty_farming", LootModifierSerializersPM.BOUNTY_FARMING.get(), new BountyFarmingModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(BlockTagsPM.BOUNTY_CROPS).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.BOUNTY.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.25F));
        this.add("bounty_fishing", LootModifierSerializersPM.BOUNTY_FISHING.get(), new BountyFishingModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().fishingHook(FishingHookPredicate.inOpenWater(false))).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.BOUNTY.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.25F));
        this.add("bonus_nugget_iron", LootModifierSerializersPM.BONUS_NUGGET.get(), new BonusNuggetModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(Tags.Blocks.ORES_IRON).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.5F, Items.IRON_NUGGET));
        this.add("bonus_nugget_gold", LootModifierSerializersPM.BONUS_NUGGET.get(), new BonusNuggetModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(Tags.Blocks.ORES_GOLD).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.5F, Items.GOLD_NUGGET));
        this.add("bonus_nugget_quartz", LootModifierSerializersPM.BONUS_NUGGET.get(), new BonusNuggetModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(Tags.Blocks.ORES_QUARTZ).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.5F, ItemsPM.QUARTZ_NUGGET.get()));
        this.add("blood_notes_high", LootModifierSerializersPM.BLOOD_NOTES.get(), new BloodNotesModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_BLOOD_NOTES_HIGH)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }));
        this.add("blood_notes_low", LootModifierSerializersPM.BLOOD_NOTES.get(), new BloodNotesModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_BLOOD_NOTES_LOW)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.25F, 0.25F).build()
                }));
        this.add("relic_fragments_high", LootModifierSerializersPM.RELIC_FRAGMENTS.get(), new RelicFragmentsModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_HIGH)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, 3, 5, 1));
        this.add("relic_fragments_low", LootModifierSerializersPM.RELIC_FRAGMENTS.get(), new RelicFragmentsModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_LOW)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.05F).build()
                }, 1, 1, 0));
    }
}
