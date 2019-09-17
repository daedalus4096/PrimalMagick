package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.ArcaneWorkbenchContainer;
import com.verdantartifice.primalmagic.common.containers.GrimoireContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.IForgeRegistry;

public class InitContainers {
    public static void initContainers(IForgeRegistry<ContainerType<?>> registry) {
        registry.register(IForgeContainerType.create((windowId, inv, data) -> {
            return new GrimoireContainer(windowId);
        }).setRegistryName(PrimalMagic.MODID, "grimoire"));
        
        registry.register(IForgeContainerType.create((windowId, inv, data) -> {
            return new ArcaneWorkbenchContainer(windowId, inv);
        }).setRegistryName(PrimalMagic.MODID, "arcane_workbench"));
    }
}
