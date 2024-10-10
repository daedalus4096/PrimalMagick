package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypeRegistration;
import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import com.verdantartifice.primalmagick.common.commands.arguments.ArgumentTypeRegistration;
import com.verdantartifice.primalmagick.common.components.DataComponentTypeRegistration;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializerRegistration;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypeRegistration;
import com.verdantartifice.primalmagick.common.creative.CreativeModeTabRegistration;
import com.verdantartifice.primalmagick.common.effects.MobEffectRegistration;
import com.verdantartifice.primalmagick.common.entities.EntityTypeRegistration;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypeRegistration;
import com.verdantartifice.primalmagick.common.entities.ai.sensing.SensorTypeRegistration;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialRegistration;
import com.verdantartifice.primalmagick.common.loot.modifiers.LootModifierSerializersPM;
import com.verdantartifice.primalmagick.common.menus.MenuTypeRegistration;
import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyTypeRegistration;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementTypeRegistration;
import com.verdantartifice.primalmagick.common.sounds.SoundEventRegistration;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyRegistration;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModTypeRegistration;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ProjectMaterialTypeRegistration;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardTypeRegistration;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionTypeRegistration;
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
        // Platform implementations of cross-platform registries
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
        ParticleTypeRegistration.init();
        ArgumentTypeRegistration.init();
        SensorTypeRegistration.init();
        MemoryModuleTypeRegistration.init();
        ResearchKeyTypeRegistration.init();
        RequirementTypeRegistration.init();
        ProjectMaterialTypeRegistration.init();
        RewardTypeRegistration.init();
        WeightFunctionTypeRegistration.init();
        SpellPropertyRegistration.init();
        SpellModTypeRegistration.init();

        // Platform specific registries
        LootModifierSerializersPM.init();
    }
}
