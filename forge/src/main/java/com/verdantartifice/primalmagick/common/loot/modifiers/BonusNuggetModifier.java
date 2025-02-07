package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.loot.LootModifiers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import java.util.Map;

/**
 * Global loot modifier that gives a chance for bonus nuggets when mining quartz or metal ores.
 * 
 * @author Daedalus4096
 */
public class BonusNuggetModifier extends LootModifier {
    public static final MapCodec<BonusNuggetModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(inst.group(
                Codec.unboundedMap(TagKey.codec(Registries.BLOCK), TagKey.codec(Registries.ITEM)).fieldOf("nuggetMap").forGetter(m -> m.nuggetMap),
                Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)
            )).apply(inst, BonusNuggetModifier::new));

    protected final float chance;
    protected final Map<TagKey<Block>, TagKey<Item>> nuggetMap;
    
    public BonusNuggetModifier(LootItemCondition[] conditions, Map<TagKey<Block>, TagKey<Item>> nuggetMap, float chance) {
        super(conditions);
        this.nuggetMap = nuggetMap;
        this.chance = chance;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return LootModifiers.bonusNugget(generatedLoot, context, this.chance, this.nuggetMap);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
