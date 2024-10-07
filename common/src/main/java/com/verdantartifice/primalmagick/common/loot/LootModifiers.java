package com.verdantartifice.primalmagick.common.loot;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

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
}