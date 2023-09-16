package com.verdantartifice.primalmagick.common.loot.modifiers;

import org.jetbrains.annotations.NotNull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Global loot modifier that controls essence drops from the Essence Thief enchantment.
 * 
 * @author Daedalus4096
 */
public class EssenceThiefModifier extends LootModifier {
    public static final Codec<EssenceThiefModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, EssenceThiefModifier::new));

    public EssenceThiefModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity victim) {
            int enchantLevel = getEnchantLevel(context);
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
                    for (int index = 0; index < 2 * enchantLevel; index++) {
                        generatedLoot.add(EssenceItem.getEssence(EssenceType.DUST, bag.getRandom(context.getRandom())));
                    }
                }
            }
        }
        return generatedLoot;
    }
    
    private static int getEnchantLevel(LootContext context) {
        if (context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof LivingEntity killer) {
            // Get the highest enchantment level among held items that could hold the Essence Thief enchantment
            return EnchantmentsPM.ESSENCE_THIEF.get().getSlotItems(killer).values().stream().mapToInt(EssenceThiefModifier::getEnchantLevel).max().orElse(0);
        } else {
            return 0;
        }
    }
    
    private static int getEnchantLevel(ItemStack stack) {
        return stack.getEnchantmentLevel(EnchantmentsPM.ESSENCE_THIEF.get());
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
