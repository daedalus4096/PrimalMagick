package com.verdantartifice.primalmagick.common.loot.modifiers;

import java.util.stream.IntStream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Global loot modifier that always adds an item to a generated loot set if its loot conditions are met.  An
 * optional number of rolls may be specified, defaulting to a single roll if not present.
 * 
 * @author Daedalus4096
 */
public class AddItemModifier extends LootModifier {
    protected static final UniformInt DEFAULT_ROLLS = UniformInt.of(1, 1);
    public static final Codec<AddItemModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst)
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
            .and(UniformInt.CODEC.optionalFieldOf("rolls", DEFAULT_ROLLS).forGetter(m -> m.rolls))
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
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
