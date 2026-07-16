package com.verdantartifice.primalmagick.platform.services.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public interface ILootItemConditionTypeRegistryService extends IRegistryService<MapCodec<? extends LootItemCondition>> {
}
