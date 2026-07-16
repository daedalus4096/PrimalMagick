package com.verdantartifice.primalmagick.common.loot.conditions;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/**
 * Registration for the mod's loot condition types.
 * 
 * @author Daedalus4096
 */
public class LootConditionTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.LOOT_ITEM_CONDITION_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<MapCodec<? extends LootItemCondition>, MapCodec<MatchBlockTag>> MATCH_BLOCK_TAG = register("match_block_tag", MatchBlockTag.CODEC);

    private static <T extends LootItemCondition> IRegistryItem<MapCodec<? extends LootItemCondition>, MapCodec<T>> register(String name, MapCodec<T> codec) {
        return Services.LOOT_ITEM_CONDITION_TYPES_REGISTRY.register(name, () -> codec);
    }
}
