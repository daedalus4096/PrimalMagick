package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypeRegistration;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.books.grids.rewards.GridRewardTypeRegistration;
import com.verdantartifice.primalmagick.common.commands.arguments.ArgumentTypeRegistration;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.ingredients.IngredientsPM;
import com.verdantartifice.primalmagick.common.creative.CreativeModeTabsPM;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypeRegistration;
import com.verdantartifice.primalmagick.common.entities.ai.sensing.SensorTypeRegistration;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialsPM;
import com.verdantartifice.primalmagick.common.loot.modifiers.LootModifierSerializersPM;
import com.verdantartifice.primalmagick.common.menus.MenuTypesPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyTypeRegistration;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementTypeRegistration;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicTypeRegistration;
import com.verdantartifice.primalmagick.common.rituals.steps.RitualStepTypeRegistration;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyRegistration;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModTypeRegistration;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadTypeRegistration;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleTypeRegistration;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ProjectMaterialTypeRegistration;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardTypeRegistration;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionTypeRegistration;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructurePieceTypesPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructureTypesPM;

/**
 * Point of initialization for mod deferred registries.
 * 
 * @author Daedalus4096
 */
public class InitRegistries {
    public static void initDeferredRegistries() {
        // Platform implementations of cross-platform registries
        BlocksPM.init();
        ItemsPM.init();
        CreativeModeTabsPM.init();
        ArmorMaterialsPM.init();
        DataComponentsPM.init();
        EntityTypesPM.init();   // FIXME Change rest of class to use this pattern
        BlockEntityTypesPM.init();
        MenuTypesPM.init();
        EffectsPM.init();
        RecipeTypesPM.init();
        RecipeSerializersPM.init();
        SoundsPM.init();
        StructurePieceTypesPM.init();
        StructureTypesPM.init();
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
        SpellVehicleTypeRegistration.init();
        SpellPayloadTypeRegistration.init();
        GridRewardTypeRegistration.init();
        ResearchTopicTypeRegistration.init();
        RitualStepTypeRegistration.init();

        // Platform specific registries
        IngredientsPM.init();
        LootModifierSerializersPM.init();
    }
}
