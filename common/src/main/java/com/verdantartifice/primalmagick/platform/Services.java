package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IInputService;
import com.verdantartifice.primalmagick.platform.services.registries.IArgumentTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IArmorMaterialService;
import com.verdantartifice.primalmagick.platform.services.registries.IBlockEntityTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IBlockService;
import com.verdantartifice.primalmagick.platform.services.registries.ICreativeModeTabService;
import com.verdantartifice.primalmagick.platform.services.registries.IDataComponentTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IEntityTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IGridRewardTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IItemService;
import com.verdantartifice.primalmagick.platform.services.registries.IMemoryModuleTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IMenuTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IMobEffectService;
import com.verdantartifice.primalmagick.platform.services.registries.IParticleTypeService;
import com.verdantartifice.primalmagick.platform.services.IPlatformService;
import com.verdantartifice.primalmagick.platform.services.registries.IProjectMaterialTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IRecipeSerializerService;
import com.verdantartifice.primalmagick.platform.services.registries.IRecipeTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IRequirementTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IResearchKeyTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IResearchTopicTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IRewardTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IRitualStepTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.ISensorTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.ISoundEventService;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellModTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellPayloadTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellPropertyService;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellVehicleTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IStructurePieceTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IStructureTypeService;
import com.verdantartifice.primalmagick.platform.services.registries.IWeightFunctionTypeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

/**
 * Definition point for cross-platform services. These are used to allow common code to call
 * into platform-specific (e.g. Forge) code using Java services.
 *
 * @author Daedalus4096
 */
public class Services {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final IPlatformService PLATFORM = load(IPlatformService.class);
    public static final IInputService INPUT = load(IInputService.class);

    // Registry services
    public static final IBlockService BLOCKS = load(IBlockService.class);
    public static final IItemService ITEMS = load(IItemService.class);
    public static final ICreativeModeTabService CREATIVE_MODE_TABS = load(ICreativeModeTabService.class);
    public static final IArmorMaterialService ARMOR_MATERIALS = load(IArmorMaterialService.class);
    public static final IDataComponentTypeService DATA_COMPONENT_TYPES = load(IDataComponentTypeService.class);
    public static final IEntityTypeService ENTITY_TYPES = load(IEntityTypeService.class);
    public static final IBlockEntityTypeService BLOCK_ENTITY_TYPES = load(IBlockEntityTypeService.class);
    public static final IMenuTypeService MENU_TYPES = load(IMenuTypeService.class);
    public static final IMobEffectService MOB_EFFECTS = load(IMobEffectService.class);
    public static final IRecipeTypeService RECIPE_TYPES = load(IRecipeTypeService.class);
    public static final IRecipeSerializerService RECIPE_SERIALIZERS = load(IRecipeSerializerService.class);
    public static final ISoundEventService SOUND_EVENTS = load(ISoundEventService.class);
    public static final IStructurePieceTypeService STRUCTURE_PIECE_TYPES = load(IStructurePieceTypeService.class);
    public static final IStructureTypeService STRUCTURE_TYPES = load(IStructureTypeService.class);
    public static final IParticleTypeService PARTICLE_TYPES = load(IParticleTypeService.class);
    public static final IArgumentTypeService ARGUMENT_TYPES = load(IArgumentTypeService.class);
    public static final ISensorTypeService SENSOR_TYPES = load(ISensorTypeService.class);
    public static final IMemoryModuleTypeService MEMORY_MODULE_TYPES = load(IMemoryModuleTypeService.class);
    public static final IResearchKeyTypeService RESEARCH_KEY_TYPES = load(IResearchKeyTypeService.class);
    public static final IRequirementTypeService REQUIREMENT_TYPES = load(IRequirementTypeService.class);
    public static final IProjectMaterialTypeService PROJECT_MATERIAL_TYPES = load(IProjectMaterialTypeService.class);
    public static final IRewardTypeService REWARD_TYPES = load(IRewardTypeService.class);
    public static final IWeightFunctionTypeService WEIGHT_FUNCTION_TYPES = load(IWeightFunctionTypeService.class);
    public static final ISpellPropertyService SPELL_PROPERTIES = load(ISpellPropertyService.class);
    public static final ISpellModTypeService SPELL_MOD_TYPES = load(ISpellModTypeService.class);
    public static final ISpellVehicleTypeService SPELL_VEHICLE_TYPES = load(ISpellVehicleTypeService.class);
    public static final ISpellPayloadTypeService SPELL_PAYLOAD_TYPES = load(ISpellPayloadTypeService.class);
    public static final IGridRewardTypeService GRID_REWARD_TYPES = load(IGridRewardTypeService.class);
    public static final IResearchTopicTypeService RESEARCH_TOPIC_TYPES = load(IResearchTopicTypeService.class);
    public static final IRitualStepTypeService RITUAL_STEP_TYPES = load(IRitualStepTypeService.class);

    // This code is used to load a service for the current environment. Your implementation of the service must be defined
    // manually by including a text file in META-INF/services named with the fully qualified class name of the service.
    // Inside the file you should write the fully qualified class name of the implementation to load for the platform.
    private static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}