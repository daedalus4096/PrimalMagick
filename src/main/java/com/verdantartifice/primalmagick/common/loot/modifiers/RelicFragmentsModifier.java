package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Global loot modifier that allows mobs in the conditioned tag to drop Mystical Relic Fragments when killed.
 * 
 * @author Daedalus4096
 */
public class RelicFragmentsModifier extends LootModifier {
    protected final int minCount;
    protected final int maxCount;
    protected final int lootingBonus;
    
    public RelicFragmentsModifier(LootItemCondition[] conditionsIn, int minCount, int maxCount, int lootingBonus) {
        super(conditionsIn);
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.lootingBonus = lootingBonus;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        int lootingLevel = context.getLootingModifier();
        int count = context.getRandom().nextInt((this.maxCount - this.minCount + 1) + (this.lootingBonus * lootingLevel)) + this.minCount;
        if (count > 0) {
            generatedLoot.add(new ItemStack(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), count));
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<RelicFragmentsModifier> {
        @Override
        public RelicFragmentsModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            int minCount = object.getAsJsonPrimitive("minCount").getAsInt();
            int maxCount = object.getAsJsonPrimitive("maxCount").getAsInt();
            int lootingBonus = object.getAsJsonPrimitive("lootingBonus").getAsInt();
            return new RelicFragmentsModifier(ailootcondition, minCount, maxCount, lootingBonus);
        }

        @Override
        public JsonObject write(RelicFragmentsModifier instance) {
            JsonObject obj = this.makeConditions(instance.conditions);
            obj.addProperty("minCount", instance.minCount);
            obj.addProperty("maxCount", instance.maxCount);
            obj.addProperty("lootingBonus", instance.lootingBonus);
            return obj;
        }
    }
}
