package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.ContainersPM;
import com.verdantartifice.primalmagic.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.loot.modifiers.LootModifierSerializersPM;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;

/**
 * Point of initialization for mod deferred registries.
 * 
 * @author Daedalus4096
 */
public class InitRegistries {
    public static void initDeferredRegistries() {
        BlocksPM.init();
        ItemsPM.init();
        TileEntityTypesPM.init();
        ContainersPM.init();
        EntityTypesPM.init();
        EffectsPM.init();
        RecipeSerializersPM.init();
        SoundsPM.init();
        FeaturesPM.init();
        EnchantmentsPM.init();
        LootModifierSerializersPM.init();
        PrimalMagic.proxy.initDeferredRegistries();
    }
}
