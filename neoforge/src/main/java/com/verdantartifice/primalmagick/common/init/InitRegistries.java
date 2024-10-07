package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import com.verdantartifice.primalmagick.common.components.DataComponentTypeRegistration;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializerRegistration;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypeRegistration;
import com.verdantartifice.primalmagick.common.creative.CreativeModeTabRegistration;
import com.verdantartifice.primalmagick.common.effects.MobEffectRegistration;
import com.verdantartifice.primalmagick.common.entities.EntityTypeRegistration;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialRegistration;
import com.verdantartifice.primalmagick.common.menus.MenuTypeRegistration;
import com.verdantartifice.primalmagick.common.sounds.SoundEventRegistration;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypeRegistration;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructurePieceTypeRegistration;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructureTypeRegistration;

/**
 * Point of initialization for mod deferred registries.
 *
 * @author Daedalus4096
 */
public class InitRegistries {
    public static void initDeferredRegistries() {
        // Platform implementations of common registries
        BlockRegistration.init();
        ItemRegistration.init();
        CreativeModeTabRegistration.init();
        ArmorMaterialRegistration.init();
        DataComponentTypeRegistration.init();
        EntityTypeRegistration.init();
        BlockEntityTypeRegistration.init();
        MenuTypeRegistration.init();
        MobEffectRegistration.init();
        RecipeTypeRegistration.init();
        RecipeSerializerRegistration.init();
        SoundEventRegistration.init();
        StructurePieceTypeRegistration.init();
        StructureTypeRegistration.init();

        // Platform specific registries
        // TODO Loot modifiers
    }
}
