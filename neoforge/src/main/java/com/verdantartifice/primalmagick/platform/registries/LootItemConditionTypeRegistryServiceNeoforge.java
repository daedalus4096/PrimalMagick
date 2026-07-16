package com.verdantartifice.primalmagick.platform.registries;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.ILootItemConditionTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LootItemConditionTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<MapCodec<? extends LootItemCondition>> implements ILootItemConditionTypeRegistryService {
    private static final DeferredRegister<MapCodec<? extends LootItemCondition>> TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<MapCodec<? extends LootItemCondition>>> getDeferredRegisterSupplier() {
        return () -> TYPES;
    }

    @Override
    protected Registry<MapCodec<? extends LootItemCondition>> getRegistry() {
        return BuiltInRegistries.LOOT_CONDITION_TYPE;
    }
}
