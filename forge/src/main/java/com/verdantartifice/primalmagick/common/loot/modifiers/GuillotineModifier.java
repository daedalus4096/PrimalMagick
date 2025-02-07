package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.loot.LootModifiers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

/**
 * Global loot modifier that has a chance per Guillotine enchantment level to add the given item to
 * the generated loot set if its loot conditions are met and the item is not already present.
 * 
 * @author Daedalus4096
 */
public class GuillotineModifier extends LootModifier {
    public static final MapCodec<GuillotineModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
            .and(TagKey.codec(Registries.ENTITY_TYPE).fieldOf("targetTag").forGetter(m -> m.targetTag))
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
            .and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance))
            .apply(inst, GuillotineModifier::new));

    protected final TagKey<EntityType<?>> targetTag;
    protected final Item item;
    protected final float chance;

    public GuillotineModifier(LootItemCondition[] conditionsIn, TagKey<EntityType<?>> targetTag, Item item, float chance) {
        super(conditionsIn);
        this.targetTag = targetTag;
        this.item = item;
        this.chance = chance;
    }
    
    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return LootModifiers.guillotine(generatedLoot, context, this.targetTag, this.item, this.chance);
    }
    
    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
