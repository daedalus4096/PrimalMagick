package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.util.ItemUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Global loot modifier that gives a chance for bonus rolls on crops.  Unlike Fortune, this
 * gives bonus crops in addition to seeds.
 * 
 * @author Daedalus4096
 */
public class BountyFarmingModifier extends LootModifier {
    public static final MapCodec<BountyFarmingModifier> CODEC = RecordCodecBuilder.mapCodec(
            inst -> codecStart(inst).and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)).apply(inst, BountyFarmingModifier::new));

    protected final float chance;

    public BountyFarmingModifier(LootItemCondition[] conditionsIn, float chance) {
        super(conditionsIn);
        this.chance = chance;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootTable table = context.getLevel().getServer().reloadableRegistries().getLootTable(context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock().getLootTable());
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(context.getResolver().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentsPM.BOUNTY), context.getParamOrNull(LootContextParams.TOOL));
        for (int index = 0; index < enchantmentLevel; index++) {
            if (context.getRandom().nextFloat() < this.chance) {
                List<ItemStack> bonusList = new ArrayList<>();
                table.getRandomItemsRaw(context, bonusList::add);   // Use deprecated method to avoid recursive modification of loot generated
                generatedLoot = ItemUtils.mergeItemStackLists(generatedLoot, bonusList).stream().collect(ObjectArrayList.toList());
            }
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
