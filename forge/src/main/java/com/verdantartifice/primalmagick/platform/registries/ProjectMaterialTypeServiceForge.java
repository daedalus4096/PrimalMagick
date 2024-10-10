package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.theorycrafting.materials.ProjectMaterialType;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ProjectMaterialTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IProjectMaterialTypeService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the project material type registry service.
 *
 * @author Daedalus4096
 */
public class ProjectMaterialTypeServiceForge extends AbstractCustomRegistryServiceForge<ProjectMaterialType<?>> implements IProjectMaterialTypeService {
    @Override
    protected Supplier<DeferredRegister<ProjectMaterialType<?>>> getDeferredRegisterSupplier() {
        return ProjectMaterialTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<ProjectMaterialType<?>>> getRegistry() {
        return ProjectMaterialTypeRegistration.getRegistry();
    }
}
