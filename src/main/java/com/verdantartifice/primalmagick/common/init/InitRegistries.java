package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.commands.arguments.ArgumentTypesPM;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.creative.CreativeModeTabsPM;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;
import com.verdantartifice.primalmagick.common.entities.ai.sensing.SensorTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialsPM;
import com.verdantartifice.primalmagick.common.loot.modifiers.LootModifierSerializersPM;
import com.verdantartifice.primalmagick.common.menus.MenuTypesPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyTypesPM;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementsPM;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModsPM;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadsPM;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehiclesPM;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ProjectMaterialTypesPM;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardTypesPM;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionTypesPM;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructureFeaturesPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructurePieceTypesPM;

/**
 * Point of initialization for mod deferred registries.
 * 
 * @author Daedalus4096
 */
public class InitRegistries {
    public static void initDeferredRegistries() {
        BlocksPM.init();
        ItemsPM.init();
        CreativeModeTabsPM.init();
        TileEntityTypesPM.init();
        MenuTypesPM.init();
        EntityTypesPM.init();
        EffectsPM.init();
        RecipeTypesPM.init();
        RecipeSerializersPM.init();
        SoundsPM.init();
        StructurePieceTypesPM.init();
        StructureFeaturesPM.init();
        EnchantmentsPM.init();
        LootModifierSerializersPM.init();
        ParticleTypesPM.init();
        ArgumentTypesPM.init();
        SensorTypesPM.init();
        MemoryModuleTypesPM.init();
        ResearchKeyTypesPM.init();
        RequirementsPM.init();
        ProjectMaterialTypesPM.init();
        RewardTypesPM.init();
        WeightFunctionTypesPM.init();
        DataComponentsPM.init();
        SpellPropertiesPM.init();
        SpellModsPM.init();
        SpellVehiclesPM.init();
        SpellPayloadsPM.init();
        ArmorMaterialsPM.init();
    }
}
