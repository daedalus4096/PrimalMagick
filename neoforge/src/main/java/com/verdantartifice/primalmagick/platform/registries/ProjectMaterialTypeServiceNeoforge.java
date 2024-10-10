package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.theorycrafting.materials.ProjectMaterialType;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ProjectMaterialTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IProjectMaterialTypeService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the project material type registry service.
 *
 * @author Daedalus4096
 */
public class ProjectMaterialTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<ProjectMaterialType<?>> implements IProjectMaterialTypeService {
    @Override
    protected Supplier<DeferredRegister<ProjectMaterialType<?>>> getDeferredRegisterSupplier() {
        return ProjectMaterialTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<ProjectMaterialType<?>> getRegistry() {
        return ProjectMaterialTypeRegistration.TYPES;
    }
}
