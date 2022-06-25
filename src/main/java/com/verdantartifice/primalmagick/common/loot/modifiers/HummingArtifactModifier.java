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
 * Global loot modifier that gives a chance for Humming Artifacts in dungeon loot chests.
 * 
 * @author Daedalus4096
 */
public class HummingArtifactModifier extends LootModifier {
    public HummingArtifactModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Random chance is controlled by the LootItemRandomChance condition in the modifier JSON
        generatedLoot.add(new ItemStack(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<HummingArtifactModifier> {
        @Override
        public HummingArtifactModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new HummingArtifactModifier(ailootcondition);
        }

        @Override
        public JsonObject write(HummingArtifactModifier instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
