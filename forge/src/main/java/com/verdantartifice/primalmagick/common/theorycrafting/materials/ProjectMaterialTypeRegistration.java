package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod project material types in Forge.
 * 
 * @author Daedalus4096
 */
public class ProjectMaterialTypeRegistration {
    private static final DeferredRegister<ProjectMaterialType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.PROJECT_MATERIAL_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<ProjectMaterialType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    public static DeferredRegister<ProjectMaterialType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static Supplier<IForgeRegistry<ProjectMaterialType<?>>> getRegistry() {
        return TYPES;
    }
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
