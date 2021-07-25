package com.verdantartifice.primalmagic.common.loot.conditions;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

/**
 * Registration for the mod's loot condition types.
 * 
 * @author Daedalus4096
 */
public class LootConditionTypesPM {
    public static final LootItemConditionType MATCH_BLOCK_TAG = register("match_block_tag", new MatchBlockTag.Serializer());
    
    public static void register() {
        // No-op method to ensure that the class is loaded
    }
    
    protected static LootItemConditionType register(String name, Serializer<? extends LootItemCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(PrimalMagic.MODID, name), new LootItemConditionType(serializer));
    }
}
