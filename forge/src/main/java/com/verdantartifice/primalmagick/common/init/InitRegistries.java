package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypeRegistration;
import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import com.verdantartifice.primalmagick.common.books.grids.rewards.GridRewardTypesPM;
import com.verdantartifice.primalmagick.common.commands.arguments.ArgumentTypeRegistration;
import com.verdantartifice.primalmagick.common.components.DataComponentTypeRegistration;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializerRegistration;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypeRegistration;
import com.verdantartifice.primalmagick.common.crafting.ingredients.IngredientsPM;
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
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicTypesPM;
import com.verdantartifice.primalmagick.common.rituals.steps.RitualStepTypesPM;
import com.verdantartifice.primalmagick.common.sounds.SoundEventRegistration;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModsPM;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadsPM;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehiclesPM;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ProjectMaterialTypesPM;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardTypesPM;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionTypesPM;
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

        // Platform specific registries
        IngredientsPM.init();
        LootModifierSerializersPM.init();

        // TODO To be converted
        ProjectMaterialTypesPM.init();
        RewardTypesPM.init();
        WeightFunctionTypesPM.init();
        SpellPropertiesPM.init();
        SpellModsPM.init();
        SpellVehiclesPM.init();
        SpellPayloadsPM.init();
        GridRewardTypesPM.init();
        ResearchTopicTypesPM.init();
        RitualStepTypesPM.init();
    }
}
