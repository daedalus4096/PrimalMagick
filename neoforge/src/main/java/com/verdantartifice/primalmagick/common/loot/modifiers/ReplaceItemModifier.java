package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.loot.LootModifiers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

/**
 * Global loot modifier that replaces all contents of the loot drop with the given item if its loot
 * conditions are met.  Mainly used for cases where only a single item is allowed to drop, such as
 * archaeology.
 * 
 * @author Daedalus4096
 */
public class ReplaceItemModifier extends LootModifier {
    public static final MapCodec<ReplaceItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
            .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(m -> m.item))
            .apply(inst, ReplaceItemModifier::new));
    
    protected final Item item;

    public ReplaceItemModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    @NotNull
    protected ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        return LootModifiers.replaceItem(generatedLoot, context, this.item);
    }

    @Override
    @NotNull
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
