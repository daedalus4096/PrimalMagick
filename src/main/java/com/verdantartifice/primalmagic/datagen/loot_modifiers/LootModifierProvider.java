package com.verdantartifice.primalmagic.datagen.loot_modifiers;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.loot.conditions.MatchBlockTag;
import com.verdantartifice.primalmagic.common.loot.modifiers.BloodyFleshModifier;
import com.verdantartifice.primalmagic.common.loot.modifiers.BonusNuggetModifier;
import com.verdantartifice.primalmagic.common.loot.modifiers.BountyFarmingModifier;
import com.verdantartifice.primalmagic.common.loot.modifiers.BountyFishingModifier;
import com.verdantartifice.primalmagic.common.loot.modifiers.LootModifierSerializersPM;
import com.verdantartifice.primalmagic.common.tags.BlockTagsPM;
import com.verdantartifice.primalmagic.common.tags.EntityTypeTagsPM;

import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.FishingPredicate;
import net.minecraft.loot.LootContext.EntityTarget;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

/**
 * Data provider for all of the mod's loot modifiers.
 * 
 * @author Daedalus4096
 */
public class LootModifierProvider extends GlobalLootModifierProvider {
    public LootModifierProvider(DataGenerator gen) {
        super(gen, PrimalMagic.MODID);
    }

    @Override
    protected void start() {
        this.add("bloody_flesh", LootModifierSerializersPM.BLOODY_FLESH.get(), new BloodyFleshModifier(
                new ILootCondition[] {
                        EntityHasProperty.builder(EntityTarget.THIS, EntityPredicate.Builder.create().type(EntityTypeTagsPM.DROPS_BLOODY_FLESH)).build(),
                        KilledByPlayer.builder().build(),
                        RandomChanceWithLooting.builder(0.5F, 0.1F).build()
                }));
        this.add("bounty_farming", LootModifierSerializersPM.BOUNTY_FARMING.get(), new BountyFarmingModifier(
                new ILootCondition[] {
                        MatchBlockTag.builder(BlockTagsPM.BOUNTY_CROPS).build(),
                        MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(EnchantmentsPM.BOUNTY.get(), MinMaxBounds.IntBound.atLeast(1)))).build()
                }, 0.25F));
        this.add("bounty_fishing", LootModifierSerializersPM.BOUNTY_FISHING.get(), new BountyFishingModifier(
                new ILootCondition[] {
                        EntityHasProperty.builder(EntityTarget.THIS, EntityPredicate.Builder.create().fishing(FishingPredicate.func_234640_a_(false))).build(),
                        MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(EnchantmentsPM.BOUNTY.get(), MinMaxBounds.IntBound.atLeast(1)))).build()
                }, 0.25F));
        this.add("bonus_nugget_iron", LootModifierSerializersPM.BONUS_NUGGET.get(), new BonusNuggetModifier(
                new ILootCondition[] {
                        MatchBlockTag.builder(Tags.Blocks.ORES_IRON).build(),
                        MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.IntBound.atLeast(1)))).build()
                }, 0.5F, Items.IRON_NUGGET));
        this.add("bonus_nugget_gold", LootModifierSerializersPM.BONUS_NUGGET.get(), new BonusNuggetModifier(
                new ILootCondition[] {
                        MatchBlockTag.builder(Tags.Blocks.ORES_GOLD).build(),
                        MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.IntBound.atLeast(1)))).build()
                }, 0.5F, Items.GOLD_NUGGET));
        this.add("bonus_nugget_quartz", LootModifierSerializersPM.BONUS_NUGGET.get(), new BonusNuggetModifier(
                new ILootCondition[] {
                        MatchBlockTag.builder(Tags.Blocks.ORES_QUARTZ).build(),
                        MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.IntBound.atLeast(1)))).build()
                }, 0.5F, ItemsPM.QUARTZ_NUGGET.get()));
    }
}
