package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.IArmorMaterialRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the armor material registry service.
 *
 * @author Daedalus4096
 */
public class ArmorMaterialRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<ArmorMaterial> implements IArmorMaterialRegistryService {
    private static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<ArmorMaterial>> getDeferredRegisterSupplier() {
        return () -> MATERIALS;
    }

    @Override
    protected Registry<ArmorMaterial> getRegistry() {
        return BuiltInRegistries.ARMOR_MATERIAL;
    }
}
