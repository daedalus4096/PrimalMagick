package com.verdantartifice.primalmagick.common.loot.modifiers;

import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Global loot modifier that gives a chance for bonus nuggets when mining quartz or metal ores.
 * 
 * @author Daedalus4096
 */
public class BonusNuggetModifier extends LootModifier {
    public static final Codec<BonusNuggetModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).and(inst.group(
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
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        int enchantmentLevel = tool == null ? 0 : tool.getEnchantmentLevel(EnchantmentsPM.LUCKY_STRIKE.get());
        if (state != null && enchantmentLevel > 0) {
            this.nuggetMap.forEach((blockTag, itemTag) -> {
                if (state.is(blockTag) && ForgeRegistries.ITEMS.tags().isKnownTagName(itemTag)) {
                    Optional<Item> nuggetOpt = ForgeRegistries.ITEMS.tags().getTag(itemTag).stream().findFirst();
                    nuggetOpt.ifPresent(nugget -> {
                        int nuggetCount = IntStream.range(0, enchantmentLevel).map(i -> context.getRandom().nextFloat() < this.chance ? 1 : 0).sum();
                        generatedLoot.add(new ItemStack(nugget, nuggetCount));
                    });
                }
            });
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
