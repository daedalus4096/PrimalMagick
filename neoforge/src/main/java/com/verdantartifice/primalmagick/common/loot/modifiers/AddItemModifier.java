package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.stream.IntStream;

/**
 * Global loot modifier that always adds an item to a generated loot set if its loot conditions are met.  An
 * optional number of rolls may be specified, defaulting to a single roll if not present.
 * 
 * @author Daedalus4096
 */
public class AddItemModifier extends LootModifier {
    protected static final UniformInt DEFAULT_ROLLS = UniformInt.of(1, 1);
    public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
            .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(m -> m.item))
            .and(UniformInt.CODEC.codec().optionalFieldOf("rolls", DEFAULT_ROLLS).forGetter(m -> m.rolls))
            .apply(inst, AddItemModifier::new));
    
    protected final Item item;
    protected final UniformInt rolls;
    
    public AddItemModifier(LootItemCondition[] conditionsIn, Item item) {
        this(conditionsIn, item, DEFAULT_ROLLS);
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, UniformInt rolls) {
        super(conditionsIn);
        this.item = item;
        this.rolls = rolls;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Random chance is controlled by the LootItemRandomChance condition in the modifier JSON
        IntStream.range(0, this.rolls.sample(context.getRandom())).forEach($ -> generatedLoot.add(new ItemStack(this.item)));
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
