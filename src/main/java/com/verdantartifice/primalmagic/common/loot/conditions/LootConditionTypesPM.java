package com.verdantartifice.primalmagic.common.loot.conditions;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

/**
 * Registration for the mod's loot condition types.
 * 
 * @author Daedalus4096
 */
public class LootConditionTypesPM {
    public static final LootConditionType MATCH_BLOCK_TAG = register("match_block_tag", new MatchBlockTag.Serializer());
    
    public static void register() {
        // No-op method to ensure that the class is loaded
    }
    
    protected static LootConditionType register(String name, ILootSerializer<? extends ILootCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(PrimalMagic.MODID, name), new LootConditionType(serializer));
    }
}
