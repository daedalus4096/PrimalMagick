package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod project material types in Neoforge.
 *
 * @author Daedalus4096
 */
public class ProjectMaterialTypeRegistration {
    public static final Registry<ProjectMaterialType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.PROJECT_MATERIAL_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<ProjectMaterialType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<ProjectMaterialType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}
