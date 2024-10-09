package com.verdantartifice.primalmagick.common.entities.ai.memory;

import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Deferred registry for mod AI memory module types.
 * 
 * @author Daedalus4096
 */
public class MemoryModuleTypesPM {
    public static final IRegistryItem<MemoryModuleType<?>, MemoryModuleType<List<TreefolkEntity>>> NEARBY_TREEFOLK = register("nearby_treefolk", () -> new MemoryModuleType<>(Optional.empty()));
    public static final IRegistryItem<MemoryModuleType<?>, MemoryModuleType<List<TreefolkEntity>>> NEARBY_ADULT_TREEFOLK = register("nearby_adult_treefolk", () -> new MemoryModuleType<>(Optional.empty()));
    public static final IRegistryItem<MemoryModuleType<?>, MemoryModuleType<List<TreefolkEntity>>> NEAREST_VISIBLE_ADULT_TREEFOLK = register("nearest_visible_adult_treefolk", () -> new MemoryModuleType<>(Optional.empty()));
    public static final IRegistryItem<MemoryModuleType<?>, MemoryModuleType<Boolean>> DANCED_RECENTLY = register("danced_recently", () -> new MemoryModuleType<>(Optional.empty()));
    public static final IRegistryItem<MemoryModuleType<?>, MemoryModuleType<List<BlockPos>>> NEAREST_VALID_FERTILIZABLE_BLOCKS = register("nearest_valid_fertilizable_blocks", () -> new MemoryModuleType<>(Optional.empty()));
    public static final IRegistryItem<MemoryModuleType<?>, MemoryModuleType<BlockPos>> FERTILIZE_LOCATION = register("fertilize_location", () -> new MemoryModuleType<>(Optional.empty()));
    public static final IRegistryItem<MemoryModuleType<?>, MemoryModuleType<Boolean>> FERTILIZED_RECENTLY = register("fertilized_recently", () -> new MemoryModuleType<>(Optional.empty()));
    public static final IRegistryItem<MemoryModuleType<?>, MemoryModuleType<Integer>> TIME_TRYING_TO_REACH_FERTILIZE_BLOCK = register("time_trying_to_reach_fertilize_block", () -> new MemoryModuleType<>(Optional.empty()));
    public static final IRegistryItem<MemoryModuleType<?>, MemoryModuleType<Boolean>> DISABLE_WALK_TO_FERTILIZE_BLOCK = register("disable_walk_to_fertilize_block", () -> new MemoryModuleType<>(Optional.empty()));

    private static <T> IRegistryItem<MemoryModuleType<?>, MemoryModuleType<T>> register(String name, Supplier<MemoryModuleType<T>> supplier) {
        return Services.MEMORY_MODULE_TYPES.register(name, supplier);
    }
}
