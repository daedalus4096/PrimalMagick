package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Global loot modifier that allows mobs in the conditioned tag to drop Mystical Relic Fragments when killed.
 * 
 * @author Daedalus4096
 */
public class RelicFragmentsModifier extends LootModifier {
    public static final Codec<RelicFragmentsModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).and(inst.group(
            Codec.INT.fieldOf("minCount").forGetter(m -> m.minCount),
            Codec.INT.fieldOf("maxCount").forGetter(m -> m.maxCount),
            Codec.INT.fieldOf("lootingBonus").forGetter(m -> m.lootingBonus)
        )).apply(inst, RelicFragmentsModifier::new));

    protected final int minCount;
    protected final int maxCount;
    protected final int lootingBonus;
    
    public RelicFragmentsModifier(LootItemCondition[] conditionsIn, int minCount, int maxCount, int lootingBonus) {
        super(conditionsIn);
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.lootingBonus = lootingBonus;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        int lootingLevel = context.getLootingModifier();
        int count = context.getRandom().nextInt((this.maxCount - this.minCount + 1) + (this.lootingBonus * lootingLevel)) + this.minCount;
        if (count > 0) {
            generatedLoot.add(new ItemStack(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), count));
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
