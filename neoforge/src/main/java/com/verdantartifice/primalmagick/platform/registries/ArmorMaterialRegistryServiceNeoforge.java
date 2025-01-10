package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialRegistration;
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
    @Override
    protected Supplier<DeferredRegister<ArmorMaterial>> getDeferredRegisterSupplier() {
        return ArmorMaterialRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<ArmorMaterial> getRegistry() {
        return BuiltInRegistries.ARMOR_MATERIAL;
    }
}
