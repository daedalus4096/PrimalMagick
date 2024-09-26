package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Global loot modifier that allows mobs in the conditioned tag to drop Mystical Relic Fragments when killed.
 * 
 * @author Daedalus4096
 */
public class RelicFragmentsModifier extends LootModifier {
    public static final MapCodec<RelicFragmentsModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier.codecStart(inst).and(inst.group(
            TagKey.codec(Registries.ENTITY_TYPE).fieldOf("targetTag").forGetter(m -> m.targetTag),
            Codec.INT.fieldOf("minCount").forGetter(m -> m.minCount),
            Codec.INT.fieldOf("maxCount").forGetter(m -> m.maxCount),
            Codec.INT.fieldOf("lootingBonus").forGetter(m -> m.lootingBonus)
        )).apply(inst, RelicFragmentsModifier::new));

    protected final TagKey<EntityType<?>> targetTag;
    protected final int minCount;
    protected final int maxCount;
    protected final int lootingBonus;
    
    public RelicFragmentsModifier(LootItemCondition[] conditionsIn, TagKey<EntityType<?>> targetTag, int minCount, int maxCount, int lootingBonus) {
        super(conditionsIn);
        this.targetTag = targetTag;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.lootingBonus = lootingBonus;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity targetEntity = context.getParam(LootContextParams.THIS_ENTITY);
        int lootingLevel = context.getLootingModifier();
        int count = context.getRandom().nextInt((this.maxCount - this.minCount + 1) + (this.lootingBonus * lootingLevel)) + this.minCount;
        if (targetEntity.getType().is(this.targetTag) && count > 0) {
            generatedLoot.add(new ItemStack(ItemRegistration.MYSTICAL_RELIC_FRAGMENT.get(), count));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
