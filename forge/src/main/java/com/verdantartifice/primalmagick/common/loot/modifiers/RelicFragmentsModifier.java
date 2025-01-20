package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.loot.LootModifiers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
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
    public static final MapCodec<RelicFragmentsModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(inst.group(
            TagKey.codec(Registries.ENTITY_TYPE).fieldOf("targetTag").forGetter(m -> m.targetTag),
            Codec.INT.fieldOf("minCount").forGetter(m -> m.minCount),
            Codec.INT.fieldOf("maxCount").forGetter(m -> m.maxCount)
        )).apply(inst, RelicFragmentsModifier::new));

    protected final TagKey<EntityType<?>> targetTag;
    protected final int minCount;
    protected final int maxCount;

    public RelicFragmentsModifier(LootItemCondition[] conditionsIn, TagKey<EntityType<?>> targetTag, int minCount, int maxCount) {
        super(conditionsIn);
        this.targetTag = targetTag;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return LootModifiers.relicFragments(generatedLoot, context, this.targetTag, this.minCount, this.maxCount);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
