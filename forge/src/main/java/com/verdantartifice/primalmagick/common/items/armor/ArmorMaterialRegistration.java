package com.verdantartifice.primalmagick.common.items.armor;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Deferred registry for mod armor materials.
 * 
 * @author Daedalus4096
 */
public class ArmorMaterialRegistration {
    private static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, Constants.MOD_ID);

    public static DeferredRegister<ArmorMaterial> getDeferredRegister() {
        return MATERIALS;
    }
    
    public static void init() {
        MATERIALS.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
