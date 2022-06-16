package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Global loot modifier that gives a chance for bonus nuggets when mining quartz or metal ores.
 * 
 * @author Daedalus4096
 */
public class BonusNuggetModifier extends LootModifier {
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

    public static class Serializer extends GlobalLootModifierSerializer<BonusNuggetModifier> {
        @Override
        public BonusNuggetModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            float chance = object.getAsJsonPrimitive("chance").getAsFloat();
            Item nugget = GsonHelper.getAsItem(object, "nugget");
            return new BonusNuggetModifier(ailootcondition, chance, nugget);
        }

        @Override
        public JsonObject write(BonusNuggetModifier instance) {
            JsonObject obj = this.makeConditions(instance.conditions);
            obj.addProperty("chance", instance.chance);
            
            ResourceLocation nuggetLoc = ForgeRegistries.ITEMS.getKey(instance.nugget);
            if (nuggetLoc == null) {
                throw new IllegalArgumentException("Invalid nugget " + instance.nugget);
            } else {
                obj.addProperty("nugget", nuggetLoc.toString());
            }
            
            return obj;
        }
    }
}
