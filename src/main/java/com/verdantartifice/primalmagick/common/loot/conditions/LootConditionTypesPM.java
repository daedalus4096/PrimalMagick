package com.verdantartifice.primalmagick.common.loot.conditions;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * Registration for the mod's loot condition types.
 * 
 * @author Daedalus4096
 */
public class LootConditionTypesPM {
    public static final LootItemConditionType MATCH_BLOCK_TAG = register("match_block_tag", MatchBlockTag.CODEC);
    
    public static void register() {
        // No-op method to ensure that the class is loaded
    }
    
    protected static LootItemConditionType register(String name, Codec<? extends LootItemCondition> codec) {
        return Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, PrimalMagick.resource(name), new LootItemConditionType(codec));
    }
}
