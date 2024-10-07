package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.loot.LootModifiers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

/**
 * Global loot modifier that controls essence drops from the Essence Thief enchantment.
 * 
 * @author Daedalus4096
 */
public class EssenceThiefModifier extends LootModifier {
    public static final MapCodec<EssenceThiefModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, EssenceThiefModifier::new));

    public EssenceThiefModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return LootModifiers.essenceThief(generatedLoot, context);
    }
    
    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
