package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.loot.LootModifiers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

/**
 * Global loot modifier that gives a chance for bonus rolls on the fishing loot table.
 * 
 * @author Daedalus4096
 */
public class BountyFishingModifier extends LootModifier {
    public static final MapCodec<BountyFishingModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)).apply(inst, BountyFishingModifier::new));

    protected final float chance;
    
    public BountyFishingModifier(LootItemCondition[] conditionsIn, float chance) {
        super(conditionsIn);
        this.chance = chance;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return LootModifiers.bountyFishing(generatedLoot, context, this.chance);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
