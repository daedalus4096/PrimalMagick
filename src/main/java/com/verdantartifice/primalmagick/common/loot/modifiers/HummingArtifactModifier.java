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
 * Global loot modifier that gives a chance for Humming Artifacts in dungeon loot chests.
 * 
 * @author Daedalus4096
 */
public class HummingArtifactModifier extends LootModifier {
    public static final Codec<HummingArtifactModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, HummingArtifactModifier::new));

    public HummingArtifactModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Random chance is controlled by the LootItemRandomChance condition in the modifier JSON
        generatedLoot.add(new ItemStack(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
