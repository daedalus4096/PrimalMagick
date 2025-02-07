package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.ILootItemConditionTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LootItemConditionTypeRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<LootItemConditionType> implements ILootItemConditionTypeRegistryService {
    private static final DeferredRegister<LootItemConditionType> TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<LootItemConditionType>> getDeferredRegisterSupplier() {
        return () -> TYPES;
    }

    @Override
    protected Registry<LootItemConditionType> getRegistry() {
        return BuiltInRegistries.LOOT_CONDITION_TYPE;
    }
}
