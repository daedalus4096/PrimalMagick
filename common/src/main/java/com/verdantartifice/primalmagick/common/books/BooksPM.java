package com.verdantartifice.primalmagick.common.books;

import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Datapack registry for mod books.
 * 
 * @author Daedalus4096
 */
public class BooksPM {
    // Register general static books
    public static final ResourceKey<BookDefinition> TEST_BOOK = create("test");
    public static final ResourceKey<BookDefinition> DREAM_JOURNAL = create("dream_journal");
    public static final ResourceKey<BookDefinition> WELCOME = create("welcome");
    public static final ResourceKey<BookDefinition> WARNING = create("warning");
    public static final ResourceKey<BookDefinition> SOURCE_PRIMER = create("source_primer");
    
    // Register setting tome books
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_PRELUDE = create("five_cultures/prelude");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_INTRODUCTION = create("five_cultures/introduction");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_EARTH_PART_1 = create("five_cultures/earth_part_1");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_EARTH_PART_2 = create("five_cultures/earth_part_2");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_EARTH_INNOVATIONS = create("five_cultures/earth_innovations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_EARTH_RELATIONS = create("five_cultures/earth_relations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SEA_PART_1 = create("five_cultures/sea_part_1");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SEA_PART_2 = create("five_cultures/sea_part_2");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SEA_INNOVATIONS = create("five_cultures/sea_innovations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SEA_RELATIONS = create("five_cultures/sea_relations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SKY_PART_1 = create("five_cultures/sky_part_1");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SKY_PART_2 = create("five_cultures/sky_part_2");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SKY_INNOVATIONS = create("five_cultures/sky_innovations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SKY_RELATIONS = create("five_cultures/sky_relations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SUN_PART_1 = create("five_cultures/sun_part_1");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SUN_PART_2 = create("five_cultures/sun_part_2");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SUN_INNOVATIONS = create("five_cultures/sun_innovations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_SUN_RELATIONS = create("five_cultures/sun_relations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_MOON_PART_1 = create("five_cultures/moon_part_1");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_MOON_PART_2 = create("five_cultures/moon_part_2");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_MOON_INNOVATIONS = create("five_cultures/moon_innovations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_MOON_RELATIONS = create("five_cultures/moon_relations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_FORBIDDEN_MAGICK = create("five_cultures/forbidden_magick");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_FORBIDDEN_INNOVATIONS = create("five_cultures/forbidden_innovations");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_HEAVENLY_MAGICK = create("five_cultures/heavenly_magick");
    public static final ResourceKey<BookDefinition> FIVE_CULTURES_HEAVENLY_INNOVATIONS = create("five_cultures/heavenly_innovations");

    // Register earth-themed vignette books
    public static final ResourceKey<BookDefinition> MAGICKAL_TOME_THREE_STONES = create("magickal_tome/three_stones");
    public static final ResourceKey<BookDefinition> TRAVEL_DIARY_SEVEN_PILLARS = create("travel_diary/seven_pillars");
    public static final ResourceKey<BookDefinition> HELP = create("help");
    public static final ResourceKey<BookDefinition> JOURNEYS_ARCH = create("journeys/arch");
    public static final ResourceKey<BookDefinition> TOURNAMENT_FLYER = create("tournament_flyer");
    
    // Register sea-themed vignette books
    public static final ResourceKey<BookDefinition> AVAST = create("avast");
    public static final ResourceKey<BookDefinition> MAGICKAL_TOME_ON_THE_BEACH = create("magickal_tome/on_the_beach");
    public static final ResourceKey<BookDefinition> RECIPES_SEAWEED = create("recipes/seaweed");
    public static final ResourceKey<BookDefinition> TRAVEL_DIARY_YERRAN = create("travel_diary/yerran");
    public static final ResourceKey<BookDefinition> JOURNEYS_CURRENT = create("journeys/current");

    // Register sky-themed vignette books
    public static final ResourceKey<BookDefinition> TRAVEL_DIARY_FLYING = create("travel_diary/flying");
    public static final ResourceKey<BookDefinition> MAGICKAL_TOME_LIGHTNING_IN_MY_GRASP = create("magickal_tome/lightning_in_my_grasp");
    public static final ResourceKey<BookDefinition> OUR_DIPLOMATIC_MISSION = create("our_diplomatic_mission");
    public static final ResourceKey<BookDefinition> JOURNEYS_MOTION = create("journeys/motion");

    // Register sun-themed vignette books
    public static final ResourceKey<BookDefinition> CALL_TO_ARMS = create("call_to_arms");
    public static final ResourceKey<BookDefinition> TRAVEL_DIARY_ELDE = create("travel_diary/elde");
    public static final ResourceKey<BookDefinition> MAGICKAL_TOME_NEAR_DEATH_EXPERIENCE = create("magickal_tome/near_death_experience");
    public static final ResourceKey<BookDefinition> RECIPES_SUN_TEA = create("recipes/sun_tea");
    public static final ResourceKey<BookDefinition> JOURNEYS_WINDOWS = create("journeys/windows");

    // Register moon-themed vignette books
    public static final ResourceKey<BookDefinition> TRAVEL_DIARY_CUSP = create("travel_diary/cusp");
    public static final ResourceKey<BookDefinition> MAGICKAL_TOME_IN_DREAMS = create("magickal_tome/in_dreams");
    public static final ResourceKey<BookDefinition> A_CURIOUS_EXCHANGE = create("a_curious_exchange");
    public static final ResourceKey<BookDefinition> RECIPES_MUSHROOMS = create("recipes/mushrooms");
    public static final ResourceKey<BookDefinition> JOURNEYS_SHAPES = create("journeys/shapes");

    // Register forbidden-themed vignette books
    public static final ResourceKey<BookDefinition> TRAVEL_DIARY_BEYOND = create("travel_diary/beyond");
    public static final ResourceKey<BookDefinition> MAGICKAL_TOME_SOMETHING_MOMENTOUS = create("magickal_tome/something_momentous");
    public static final ResourceKey<BookDefinition> DELAY = create("delay");
    public static final ResourceKey<BookDefinition> JOURNEYS_EDITORS_NOTE = create("journeys/editors_note");

    // Register hallowed-themed vignette books
    public static final ResourceKey<BookDefinition> MAGICKAL_TOME_TRANSGRESSIONS = create("magickal_tome/transgressions");
    public static final ResourceKey<BookDefinition> SHELTER_FROM_THE_STORMS = create("shelter_from_the_storm");
    public static final ResourceKey<BookDefinition> JOURNEYS_ANEW = create("journeys/anew");
    
    // Register unique tablet vignettes
    public static final ResourceKey<BookDefinition> BESIEGED = create("besieged");
    public static final ResourceKey<BookDefinition> CAPTAINS_LOG = create("captains_log");
    public static final ResourceKey<BookDefinition> RUNNING = create("running");
    public static final ResourceKey<BookDefinition> IN_GLORIOUS_MEMORY = create("in_glorious_memory");
    public static final ResourceKey<BookDefinition> EVACUATION = create("evacuation");
    public static final ResourceKey<BookDefinition> END_OF_ALL_THINGS = create("end_of_all_things");
    public static final ResourceKey<BookDefinition> VICTORY = create("victory");
    public static final ResourceKey<BookDefinition> OUR_FAILURE = create("our_failure");

    public static ResourceKey<BookDefinition> create(String name) {
        return ResourceKey.create(RegistryKeysPM.BOOKS, ResourceUtils.loc(name));
    }
    
    public static void bootstrap(BootstrapContext<BookDefinition> context) {
        register(context, BooksPM.TEST_BOOK);
        register(context, BooksPM.DREAM_JOURNAL);
        register(context, BooksPM.WELCOME);
        register(context, BooksPM.WARNING);
        register(context, BooksPM.SOURCE_PRIMER);
        
        register(context, BooksPM.FIVE_CULTURES_PRELUDE);
        register(context, BooksPM.FIVE_CULTURES_INTRODUCTION);
        register(context, BooksPM.FIVE_CULTURES_EARTH_PART_1);
        register(context, BooksPM.FIVE_CULTURES_EARTH_PART_2);
        register(context, BooksPM.FIVE_CULTURES_EARTH_INNOVATIONS);
        register(context, BooksPM.FIVE_CULTURES_EARTH_RELATIONS);
        register(context, BooksPM.FIVE_CULTURES_SEA_PART_1);
        register(context, BooksPM.FIVE_CULTURES_SEA_PART_2);
        register(context, BooksPM.FIVE_CULTURES_SEA_INNOVATIONS);
        register(context, BooksPM.FIVE_CULTURES_SEA_RELATIONS);
        register(context, BooksPM.FIVE_CULTURES_SKY_PART_1);
        register(context, BooksPM.FIVE_CULTURES_SKY_PART_2);
        register(context, BooksPM.FIVE_CULTURES_SKY_INNOVATIONS);
        register(context, BooksPM.FIVE_CULTURES_SKY_RELATIONS);
        register(context, BooksPM.FIVE_CULTURES_SUN_PART_1);
        register(context, BooksPM.FIVE_CULTURES_SUN_PART_2);
        register(context, BooksPM.FIVE_CULTURES_SUN_INNOVATIONS);
        register(context, BooksPM.FIVE_CULTURES_SUN_RELATIONS);
        register(context, BooksPM.FIVE_CULTURES_MOON_PART_1);
        register(context, BooksPM.FIVE_CULTURES_MOON_PART_2);
        register(context, BooksPM.FIVE_CULTURES_MOON_INNOVATIONS);
        register(context, BooksPM.FIVE_CULTURES_MOON_RELATIONS);
        register(context, BooksPM.FIVE_CULTURES_FORBIDDEN_MAGICK);
        register(context, BooksPM.FIVE_CULTURES_FORBIDDEN_INNOVATIONS);
        register(context, BooksPM.FIVE_CULTURES_HEAVENLY_MAGICK);
        register(context, BooksPM.FIVE_CULTURES_HEAVENLY_INNOVATIONS);
        
        register(context, BooksPM.MAGICKAL_TOME_THREE_STONES);
        register(context, BooksPM.TRAVEL_DIARY_SEVEN_PILLARS);
        register(context, BooksPM.HELP);
        register(context, BooksPM.JOURNEYS_ARCH);
        register(context, BooksPM.TOURNAMENT_FLYER);
        
        register(context, BooksPM.AVAST);
        register(context, BooksPM.MAGICKAL_TOME_ON_THE_BEACH);
        register(context, BooksPM.RECIPES_SEAWEED);
        register(context, BooksPM.TRAVEL_DIARY_YERRAN);
        register(context, BooksPM.JOURNEYS_CURRENT);
        
        register(context, BooksPM.TRAVEL_DIARY_FLYING);
        register(context, BooksPM.MAGICKAL_TOME_LIGHTNING_IN_MY_GRASP);
        register(context, BooksPM.OUR_DIPLOMATIC_MISSION);
        register(context, BooksPM.JOURNEYS_MOTION);
        
        register(context, BooksPM.CALL_TO_ARMS);
        register(context, BooksPM.TRAVEL_DIARY_ELDE);
        register(context, BooksPM.MAGICKAL_TOME_NEAR_DEATH_EXPERIENCE);
        register(context, BooksPM.RECIPES_SUN_TEA);
        register(context, BooksPM.JOURNEYS_WINDOWS);
        
        register(context, BooksPM.TRAVEL_DIARY_CUSP);
        register(context, BooksPM.MAGICKAL_TOME_IN_DREAMS);
        register(context, BooksPM.A_CURIOUS_EXCHANGE);
        register(context, BooksPM.RECIPES_MUSHROOMS);
        register(context, BooksPM.JOURNEYS_SHAPES);
        
        register(context, BooksPM.TRAVEL_DIARY_BEYOND);
        register(context, BooksPM.MAGICKAL_TOME_SOMETHING_MOMENTOUS);
        register(context, BooksPM.DELAY);
        register(context, BooksPM.JOURNEYS_EDITORS_NOTE);
        
        register(context, BooksPM.MAGICKAL_TOME_TRANSGRESSIONS);
        register(context, BooksPM.SHELTER_FROM_THE_STORMS);
        register(context, BooksPM.JOURNEYS_ANEW);
        
        register(context, BooksPM.BESIEGED);
        register(context, BooksPM.CAPTAINS_LOG);
        register(context, BooksPM.RUNNING);
        register(context, BooksPM.IN_GLORIOUS_MEMORY);
        register(context, BooksPM.EVACUATION);
        register(context, BooksPM.END_OF_ALL_THINGS);
        register(context, BooksPM.VICTORY);
        register(context, BooksPM.OUR_FAILURE);
    }
    
    private static Holder.Reference<BookDefinition> register(BootstrapContext<BookDefinition> context, ResourceKey<BookDefinition> key) {
        return context.register(key, new BookDefinition(ResourceLocation.fromNamespaceAndPath(key.location().getNamespace(), key.location().getPath().replace('/', '.'))));
    }
    
    /**
     * Optionally gets a reference holder for the given book definition, should that definition be found.
     * 
     * @param registryAccess a registry accessor
     * @return an optional reference holder for this view's book definition
     */
    public static Optional<Holder.Reference<BookDefinition>> getBookDefinition(ResourceKey<BookDefinition> bookKey, RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.BOOKS).getHolder(bookKey);
    }
    
    /**
     * Gets a reference holder for the given book definitions, or the given default if the given definition cannot be found.
     * Throws if the given default also cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @param defaultBook the default book definition to use if the view's is not found
     * @return a reference holder for the given book definition, or the given default
     */
    public static Holder.Reference<BookDefinition> getBookDefinitionOrDefault(ResourceKey<BookDefinition> bookKey, RegistryAccess registryAccess, ResourceKey<BookDefinition> defaultBook) {
        Registry<BookDefinition> registry = registryAccess.registryOrThrow(RegistryKeysPM.BOOKS);
        return registry.getHolder(bookKey).orElse(registry.getHolderOrThrow(defaultBook));
    }
    
    /**
     * Gets a reference holder for the given book definition, or throws if the given book definition cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @return a reference holder for the given book definition
     */
    public static Holder.Reference<BookDefinition> getBookDefinitionOrThrow(ResourceKey<BookDefinition> bookKey, RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.BOOKS).getHolderOrThrow(bookKey);
    }
}
