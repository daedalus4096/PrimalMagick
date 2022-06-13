package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.commands.arguments.ArgumentTypesPM;
import com.verdantartifice.primalmagick.common.containers.ContainersPM;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.loot.modifiers.LootModifierSerializersPM;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.worldgen.features.StructureFeaturesPM;
import com.verdantartifice.primalmagick.common.worldgen.features.StructurePieceTypesPM;

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
        StructurePieceTypesPM.init();
        StructureFeaturesPM.init();
        EnchantmentsPM.init();
        LootModifierSerializersPM.init();
        ParticleTypesPM.init();
        ArgumentTypesPM.init();
    }
}
