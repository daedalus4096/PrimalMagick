package com.verdantartifice.primalmagic.common.loot.modifiers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Global loot modifier that allows mobs in the conditioned tag to drop Bloody Flesh when killed.
 * 
 * @author Daedalus4096
 */
public class BloodyFleshModifier extends LootModifier {
    public BloodyFleshModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        LootTable table = LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(ItemsPM.BLOODY_FLESH.get()).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))).build();
        List<ItemStack> lootList = new ArrayList<>();
        table.generate(context, lootList::add); // Use deprecated version to avoid calling modifyLoot and prevent infinite recursion
        return ItemUtils.mergeItemStackLists(generatedLoot, lootList);
    }

    public static class Serializer extends GlobalLootModifierSerializer<BloodyFleshModifier> {
        @Override
        public BloodyFleshModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new BloodyFleshModifier(ailootcondition);
        }

        @Override
        public JsonObject write(BloodyFleshModifier instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
