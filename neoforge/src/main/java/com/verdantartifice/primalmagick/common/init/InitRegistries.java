package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import com.verdantartifice.primalmagick.common.components.DataComponentTypeRegistration;
import com.verdantartifice.primalmagick.common.creative.CreativeModeTabRegistration;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialRegistration;

/**
 * Point of initialization for mod deferred registries.
 *
 * @author Daedalus4096
 */
public class InitRegistries {
    public static void initDeferredRegistries() {
        BlockRegistration.init();
        ItemRegistration.init();
        CreativeModeTabRegistration.init();
        ArmorMaterialRegistration.init();
        DataComponentTypeRegistration.init();
    }
}
