package com.verdantartifice.primalmagick.common.loot;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ItemUtils;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;
import com.verdantartifice.primalmagick.platform.Services;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class LootModifiers {
    public static ObjectArrayList<ItemStack> addItem(ObjectArrayList<ItemStack> generatedLoot, LootContext context, Item item, UniformInt rolls) {
        // Random chance is controlled by the LootItemRandomChance condition in the modifier JSON
        IntStream.range(0, rolls.sample(context.getRandom())).forEach($ -> generatedLoot.add(new ItemStack(item)));
        return generatedLoot;
    }

    public static ObjectArrayList<ItemStack> bloodNotes(ObjectArrayList<ItemStack> generatedLoot, LootContext context, TagKey<EntityType<?>> targetTag) {
        // Random chance is controlled by the RandomChanceWithLooting condition in the modifier JSON
        Entity targetEntity = context.getParam(LootContextParams.THIS_ENTITY);
        if (targetEntity.getType().is(targetTag)) {
            generatedLoot.add(new ItemStack(ItemsPM.BLOOD_NOTES.get()));
        }
        return generatedLoot;
    }

    public static ObjectArrayList<ItemStack> bloodyFlesh(ObjectArrayList<ItemStack> generatedLoot, LootContext context, TagKey<EntityType<?>> targetTag) {
        // Random chance is controlled by the RandomChanceWithLooting condition in the modifier JSON
        Entity targetEntity = context.getParam(LootContextParams.THIS_ENTITY);
        if (targetEntity.getType().is(targetTag)) {
            generatedLoot.add(new ItemStack(ItemsPM.BLOODY_FLESH.get()));
        }
        return generatedLoot;
    }

    public static ObjectArrayList<ItemStack> bonusNugget(ObjectArrayList<ItemStack> generatedLoot, LootContext context, float chance,
                                                         Map<TagKey<Block>, TagKey<Item>> nuggetMap) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        int enchantmentLevel = tool == null ? 0 : tool.getEnchantments().getLevel(context.getResolver().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentsPM.LUCKY_STRIKE));
        if (state != null && enchantmentLevel > 0) {
            nuggetMap.forEach((blockTag, itemTag) -> {
                if (state.is(blockTag) && Services.ITEMS_REGISTRY.tagExists(itemTag)) {
                    Optional<Item> nuggetOpt = Services.ITEMS_REGISTRY.getTag(itemTag).stream().findFirst();
                    nuggetOpt.ifPresent(nugget -> {
                        int nuggetCount = IntStream.range(0, enchantmentLevel).map(i -> context.getRandom().nextFloat() < chance ? 1 : 0).sum();
                        generatedLoot.add(new ItemStack(nugget, nuggetCount));
                    });
                }
            });
        }
        return generatedLoot;
    }

    public static ObjectArrayList<ItemStack> bountyFarming(ObjectArrayList<ItemStack> generatedLoot, LootContext context, float chance) {
        LootTable table = context.getLevel().getServer().reloadableRegistries().getLootTable(context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock().getLootTable());
        return bountyInner(generatedLoot, context, chance, table);
    }

    public static ObjectArrayList<ItemStack> bountyFishing(ObjectArrayList<ItemStack> generatedLoot, LootContext context, float chance) {
        LootTable table = context.getLevel().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);
        return bountyInner(generatedLoot, context, chance, table);
    }

    private static ObjectArrayList<ItemStack> bountyInner(ObjectArrayList<ItemStack> generatedLoot, LootContext context, float chance, LootTable table) {
        ItemStack tool = context.hasParam(LootContextParams.TOOL) ? context.getParam(LootContextParams.TOOL) : ItemStack.EMPTY;
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(context.getResolver().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentsPM.BOUNTY), tool);
        for (int index = 0; index < enchantmentLevel; index++) {
            if (context.getRandom().nextFloat() < chance) {
                List<ItemStack> bonusList = new ArrayList<>();
                table.getRandomItemsRaw(context, bonusList::add);   // Use deprecated method to avoid recursive modification of loot generated
                generatedLoot = ItemUtils.mergeItemStackLists(generatedLoot, bonusList).stream().collect(ObjectArrayList.toList());
            }
        }
        return generatedLoot;
    }

    public static ObjectArrayList<ItemStack> essenceThief(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity victim) {
            int enchantLevel = getEnchantLevel(context, EnchantmentsPM.ESSENCE_THIEF);
            if (enchantLevel > 0) {
                // The affinity data is needed at the time of loot modification, so if it's not ready then we have no choice but to wait
                SourceList affinities = AffinityManager.getInstance().getAffinityValuesAsync(victim.getType(), victim.level().registryAccess()).join();
                if (affinities != null && !affinities.isEmpty()) {
                    WeightedRandomBag<Source> bag = new WeightedRandomBag<>();
                    for (Source source : affinities.getSources()) {
                        int amount = affinities.getAmount(source);
                        if (amount > 0) {
                            bag.add(source, amount);
                        }
                    }
                    EssenceType type = switch (enchantLevel) {
                        case 1 -> EssenceType.DUST;
                        case 2 -> EssenceType.SHARD;
                        case 3 -> EssenceType.CRYSTAL;
                        default -> EssenceType.CLUSTER;
                    };
                    generatedLoot.add(EssenceItem.getEssence(type, bag.getRandom(context.getRandom())));
                }
            }
        }
        return generatedLoot;
    }

    private static int getEnchantLevel(LootContext context, ResourceKey<Enchantment> key) {
        if (context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity killer) {
            // Get the highest enchantment level among held items that could hold the Essence Thief enchantment
            Holder<Enchantment> ench = context.getResolver().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
            return ench.value().getSlotItems(killer).values().stream().mapToInt(stack -> stack.getEnchantments().getLevel(ench)).max().orElse(0);
        } else {
            return 0;
        }
    }

    public static ObjectArrayList<ItemStack> guillotine(ObjectArrayList<ItemStack> generatedLoot, LootContext context, TagKey<EntityType<?>> targetTag,
                                                        Item item, float chance) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity livingTarget && generatedLoot.stream().noneMatch(stack -> stack.is(item))) {
            int enchantLevel = getEnchantLevel(context, EnchantmentsPM.GUILLOTINE);
            if (livingTarget.getType().is(targetTag) && enchantLevel > 0 && context.getRandom().nextFloat() < (chance * enchantLevel)) {
                generatedLoot.add(new ItemStack(item));
            }
        }
        return generatedLoot;
    }

    public static ObjectArrayList<ItemStack> relicFragments(ObjectArrayList<ItemStack> generatedLoot, LootContext context, TagKey<EntityType<?>> targetTag,
                                                            int minCount, int maxCount) {
        Entity targetEntity = context.getParam(LootContextParams.THIS_ENTITY);
        int count = context.getRandom().nextInt(maxCount - minCount + 1) + minCount;
        if (targetEntity.getType().is(targetTag) && count > 0) {
            generatedLoot.add(new ItemStack(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), count));
        }
        return generatedLoot;
    }

    public static ObjectArrayList<ItemStack> replaceItem(ObjectArrayList<ItemStack> generatedLoot, LootContext context, Item item) {
        // Random chance is controlled by the LootItemRandomChance condition in the modifier JSON
        generatedLoot.clear();
        generatedLoot.add(new ItemStack(item));
        return generatedLoot;
    }
}
