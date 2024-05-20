package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.stream.Stream;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * Datapack registry for the mod's theorycrafting project templates.
 * 
 * @author Daedalus4096
 */
public class ProjectTemplates {
    // TODO Define other project template keys
    public static final ResourceKey<ProjectTemplate> TRADE = create("trade");
    
    public static ResourceKey<ProjectTemplate> create(String name) {
        return ResourceKey.create(RegistryKeysPM.PROJECT_TEMPLATES, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<ProjectTemplate> context) {
        // TODO Initialize theorycrafting project templates
    }
    
    public static Stream<ProjectTemplate> stream(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.PROJECT_TEMPLATES).stream();
    }
}
