package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Global loot modifier that replaces all contents of the loot drop with the given item if its loot
 * conditions are met.  Mainly used for cases where only a single item is allowed to drop, such as
 * archaeology.
 * 
 * @author Daedalus4096
 */
public class ReplaceItemModifier extends LootModifier {
    public static final Codec<ReplaceItemModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst)
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
            .apply(inst, ReplaceItemModifier::new));
    
    protected final Item item;

    public ReplaceItemModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Random chance is controlled by the LootItemRandomChance condition in the modifier JSON
        generatedLoot.clear();
        generatedLoot.add(new ItemStack(this.item));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
