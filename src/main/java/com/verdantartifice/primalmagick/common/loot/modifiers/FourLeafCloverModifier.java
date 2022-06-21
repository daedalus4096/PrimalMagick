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
 * Global loot modifier that gives a chance for four-leaf clovers when breaking tall grass.
 * 
 * @author Daedalus4096
 */
public class FourLeafCloverModifier extends LootModifier {
    public FourLeafCloverModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Random chance is controlled by the RandomChanceWithLooting condition in the modifier JSON
        generatedLoot.add(new ItemStack(ItemsPM.FOUR_LEAF_CLOVER.get()));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<FourLeafCloverModifier> {
        @Override
        public FourLeafCloverModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new FourLeafCloverModifier(ailootcondition);
        }

        @Override
        public JsonObject write(FourLeafCloverModifier instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
