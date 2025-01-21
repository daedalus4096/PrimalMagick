package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.IArmorMaterialRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the armor material registry service.
 *
 * @author Daedalus4096
 */
public class ArmorMaterialRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<ArmorMaterial> implements IArmorMaterialRegistryService {
    private static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<ArmorMaterial>> getDeferredRegisterSupplier() {
        return () -> MATERIALS;
    }

    @Override
    protected Registry<ArmorMaterial> getRegistry() {
        return BuiltInRegistries.ARMOR_MATERIAL;
    }
}
