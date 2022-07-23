package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
                Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance),
                ForgeRegistries.ITEMS.getCodec().fieldOf("nugget").forGetter(m -> m.nugget)
            )).apply(inst, BonusNuggetModifier::new));

    protected final float chance;
    protected final Item nugget;
    
    public BonusNuggetModifier(LootItemCondition[] conditions, float chance, Item nugget) {
        super(conditions);
        this.chance = chance;
        this.nugget = nugget;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        int count = 0;
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        int enchantmentLevel = tool == null ? 0 : tool.getEnchantmentLevel(EnchantmentsPM.LUCKY_STRIKE.get());
        for (int index = 0; index < enchantmentLevel; index++) {
            if (context.getRandom().nextFloat() < this.chance) {
                count++;
            }
        }
        if (count > 0) {
            generatedLoot.add(new ItemStack(this.nugget, count));
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
