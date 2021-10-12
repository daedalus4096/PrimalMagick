package com.verdantartifice.primalmagic.common.loot.modifiers;

import java.util.List;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Global loot modifier that allows mobs in the conditioned tag to drop Blood-Scrawled Ravings when killed.
 * 
 * @author Daedalus4096
 */
public class BloodNotesModifier extends LootModifier {
    public BloodNotesModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        // Random chance is controlled by the RandomChanceWithLooting condition in the modifier JSON
        generatedLoot.add(new ItemStack(ItemsPM.BLOOD_NOTES.get()));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<BloodNotesModifier> {
        @Override
        public BloodNotesModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new BloodNotesModifier(ailootcondition);
        }

        @Override
        public JsonObject write(BloodNotesModifier instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
