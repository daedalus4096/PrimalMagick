package com.verdantartifice.primalmagick.common.entities.ai.memory;

import java.util.List;
import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod AI memory module types.
 * 
 * @author Daedalus4096
 */
public class MemoryModuleTypesPM {
    private static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, PrimalMagick.MODID);
    
    public static void init() {
        MEMORY_MODULE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<MemoryModuleType<List<TreefolkEntity>>> NEARBY_TREEFOLK = MEMORY_MODULE_TYPES.register("nearby_treefolk", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<List<TreefolkEntity>>> NEARBY_ADULT_TREEFOLK = MEMORY_MODULE_TYPES.register("nearby_adult_treefolk", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<List<TreefolkEntity>>> NEAREST_VISIBLE_ADULT_TREEFOLK = MEMORY_MODULE_TYPES.register("nearest_visible_adult_treefolk", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<Boolean>> DANCED_RECENTLY = MEMORY_MODULE_TYPES.register("danced_recently", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<List<BlockPos>>> NEAREST_VALID_FERTILIZABLE_BLOCKS = MEMORY_MODULE_TYPES.register("nearest_valid_fertilizable_blocks", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<BlockPos>> FERTILIZE_LOCATION = MEMORY_MODULE_TYPES.register("fertilize_location", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<Boolean>> FERTILIZED_RECENTLY = MEMORY_MODULE_TYPES.register("fertilized_recently", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<Integer>> TIME_TRYING_TO_REACH_FERTILIZE_BLOCK = MEMORY_MODULE_TYPES.register("time_trying_to_reach_fertilize_block", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryObject<MemoryModuleType<Boolean>> DISABLE_WALK_TO_FERTILIZE_BLOCK = MEMORY_MODULE_TYPES.register("disable_walk_to_fertilize_block", () -> new MemoryModuleType<>(Optional.empty()));
}
