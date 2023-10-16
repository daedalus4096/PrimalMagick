package com.verdantartifice.primalmagick.common.loot.modifiers;

import org.jetbrains.annotations.NotNull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Global loot modifier that has a chance per Guillotine enchantment level to add the given item to
 * the generated loot set if its loot conditions are met and the item is not already present.
 * 
 * @author Daedalus4096
 */
public class GuillotineModifier extends LootModifier {
    public static final Codec<GuillotineModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst)
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
            .and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance))
            .apply(inst, GuillotineModifier::new));

    protected final Item item;
    protected final float chance;

    public GuillotineModifier(LootItemCondition[] conditionsIn, Item item, float chance) {
        super(conditionsIn);
        this.item = item;
        this.chance = chance;
    }
    
    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity victim && generatedLoot.stream().allMatch(stack -> !stack.is(this.item))) {
            int enchantLevel = getEnchantLevel(context);
            if (enchantLevel > 0 && context.getRandom().nextFloat() < (this.chance * enchantLevel)) {
                generatedLoot.add(new ItemStack(this.item));
            }
        }
        return generatedLoot;
    }

    private static int getEnchantLevel(LootContext context) {
        if (context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof LivingEntity killer) {
            // Get the highest enchantment level among held items that could hold the Essence Thief enchantment
            return EnchantmentsPM.GUILLOTINE.get().getSlotItems(killer).values().stream().mapToInt(GuillotineModifier::getEnchantLevel).max().orElse(0);
        } else {
            return 0;
        }
    }
    
    private static int getEnchantLevel(ItemStack stack) {
        return stack.getEnchantmentLevel(EnchantmentsPM.GUILLOTINE.get());
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
