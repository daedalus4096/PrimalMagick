package com.verdantartifice.primalmagic.common.loot.modifiers;

import java.util.List;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
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

    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        // Random chance is controlled by the RandomChanceWithLooting condition in the modifier JSON
        generatedLoot.add(new ItemStack(ItemsPM.BLOODY_FLESH.get()));
        return generatedLoot;
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
