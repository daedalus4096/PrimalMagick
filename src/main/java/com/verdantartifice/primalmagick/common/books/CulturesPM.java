package com.verdantartifice.primalmagick.common.books;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

/**
 * Datapack registry for ancient cultures that can be found in the world.
 * 
 * @author Daedalus4096
 */
public class CulturesPM {
    // Register cultures
    public static final ResourceKey<Culture> GLOBAL = create("global"); // TODO Generate data for archaeology
    public static final ResourceKey<Culture> EARTH = create("earth");
    public static final ResourceKey<Culture> SEA = create("sea");
    public static final ResourceKey<Culture> SKY = create("sky");
    public static final ResourceKey<Culture> SUN = create("sun");
    public static final ResourceKey<Culture> MOON = create("moon");
    public static final ResourceKey<Culture> FORBIDDEN = create("forbidden");
    
    public static ResourceKey<Culture> create(String name) {
        return ResourceKey.create(RegistryKeysPM.CULTURES, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<Culture> context) {
        context.register(CulturesPM.EARTH, new Culture(PrimalMagick.resource("earth"), LootTablesPM.LIBRARY_EARTH, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN));
        context.register(CulturesPM.SEA, new Culture(PrimalMagick.resource("sea"), LootTablesPM.LIBRARY_SEA, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN));
        context.register(CulturesPM.SKY, new Culture(PrimalMagick.resource("sky"), LootTablesPM.LIBRARY_SKY, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN));
        context.register(CulturesPM.SUN, new Culture(PrimalMagick.resource("sun"), LootTablesPM.LIBRARY_SUN, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN));
        context.register(CulturesPM.MOON, new Culture(PrimalMagick.resource("moon"), LootTablesPM.LIBRARY_MOON, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN));
        context.register(CulturesPM.FORBIDDEN, new Culture(PrimalMagick.resource("forbidden"), LootTablesPM.LIBRARY_FORBIDDEN, LootTablesPM.LIBRARY_WARNING, BuiltInLootTables.EMPTY));
    }
}
