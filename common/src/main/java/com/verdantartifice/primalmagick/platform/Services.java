package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IArgumentTypeService;
import com.verdantartifice.primalmagick.platform.services.IArmorMaterialService;
import com.verdantartifice.primalmagick.platform.services.IBlockEntityTypeService;
import com.verdantartifice.primalmagick.platform.services.IBlockService;
import com.verdantartifice.primalmagick.platform.services.ICreativeModeTabService;
import com.verdantartifice.primalmagick.platform.services.IDataComponentTypeService;
import com.verdantartifice.primalmagick.platform.services.IEntityTypeService;
import com.verdantartifice.primalmagick.platform.services.IItemService;
import com.verdantartifice.primalmagick.platform.services.IMenuTypeService;
import com.verdantartifice.primalmagick.platform.services.IMobEffectService;
import com.verdantartifice.primalmagick.platform.services.IParticleTypeService;
import com.verdantartifice.primalmagick.platform.services.IPlatformService;
import com.verdantartifice.primalmagick.platform.services.IRecipeSerializerService;
import com.verdantartifice.primalmagick.platform.services.IRecipeTypeService;
import com.verdantartifice.primalmagick.platform.services.ISoundEventService;
import com.verdantartifice.primalmagick.platform.services.IStructurePieceTypeService;
import com.verdantartifice.primalmagick.platform.services.IStructureTypeService;
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