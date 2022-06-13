package com.verdantartifice.primalmagick.common.loot.modifiers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Global loot modifier that gives a chance for bonus rolls on the fishing loot table.
 * 
 * @author Daedalus4096
 */
public class BountyFishingModifier extends LootModifier {
    protected final float chance;
    
    public BountyFishingModifier(LootItemCondition[] conditionsIn, float chance) {
        super(conditionsIn);
        this.chance = chance;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootTable table = context.getLevel().getServer().getLootTables().get(BuiltInLootTables.FISHING);
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentsPM.BOUNTY.get(), context.getParamOrNull(LootContextParams.TOOL));
        for (int index = 0; index < enchantmentLevel; index++) {
            if (context.getRandom().nextFloat() < this.chance) {
                List<ItemStack> bonusList = new ArrayList<>();
                table.getRandomItems(context, bonusList::add);    // Use deprecated method to avoid recursive modification of loot generated
                generatedLoot = ItemUtils.mergeItemStackLists(generatedLoot, bonusList);
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<BountyFishingModifier> {
        @Override
        public BountyFishingModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
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
