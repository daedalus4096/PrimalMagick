package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialRegistration;
import com.verdantartifice.primalmagick.platform.services.IArmorMaterialService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the armor material registry service.
 *
 * @author Daedalus4096
 */
public class ArmorMaterialServiceForge extends AbstractRegistryServiceForge<ArmorMaterial> implements IArmorMaterialService {
    @Override
    protected Supplier<DeferredRegister<ArmorMaterial>> getDeferredRegisterSupplier() {
        return ArmorMaterialRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<ArmorMaterial> getRegistry() {
        return BuiltInRegistries.ARMOR_MATERIAL;
    }
}
