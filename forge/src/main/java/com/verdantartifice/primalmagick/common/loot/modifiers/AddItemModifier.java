package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.loot.LootModifiers;
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
    public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
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
        return LootModifiers.addItem(generatedLoot, context, this.item, this.rolls);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
