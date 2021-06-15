package com.verdantartifice.primalmagic.common.loot.modifiers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Global loot modifier that gives a chance for bonus rolls on the fishing loot table.
 * 
 * @author Daedalus4096
 */
public class BountyFishingModifier extends LootModifier {
    protected final float chance;
    
    public BountyFishingModifier(ILootCondition[] conditionsIn, float chance) {
        super(conditionsIn);
        this.chance = chance;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        LootTable table = context.getWorld().getServer().getLootTableManager().getLootTableFromLocation(LootTables.GAMEPLAY_FISHING);
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentsPM.BOUNTY.get(), context.get(LootParameters.TOOL));
        for (int index = 0; index < enchantmentLevel; index++) {
            // TODO Only grant a roll if below the given chance on RNG
            List<ItemStack> bonusList = new ArrayList<>();
            table.generate(context, bonusList::add);    // Use deprecated method to avoid recursive modification of loot generated
            ItemUtils.mergeItemStackLists(generatedLoot, bonusList);
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<BountyFishingModifier> {
        @Override
        public BountyFishingModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            float chance = object.getAsJsonPrimitive("chance").getAsFloat();
            return new BountyFishingModifier(ailootcondition, chance);
        }

        @Override
        public JsonObject write(BountyFishingModifier instance) {
            JsonObject obj = this.makeConditions(instance.conditions);
            obj.addProperty("chance", instance.chance);
            return obj;
        }
    }
}
