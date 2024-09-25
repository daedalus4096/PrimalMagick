package com.verdantartifice.primalmagick.common.books;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
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
        return ResourceKey.create(RegistryKeysPM.CULTURES, ResourceUtils.loc(name));
    }
    
    public static void bootstrap(BootstrapContext<Culture> context) {
        context.register(CulturesPM.EARTH, new Culture(ResourceUtils.loc("earth"), LootTablesPM.LIBRARY_EARTH, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN, Blocks.EMERALD_BLOCK.defaultBlockState()));
        context.register(CulturesPM.SEA, new Culture(ResourceUtils.loc("sea"), LootTablesPM.LIBRARY_SEA, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN, Blocks.LAPIS_BLOCK.defaultBlockState()));
        context.register(CulturesPM.SKY, new Culture(ResourceUtils.loc("sky"), LootTablesPM.LIBRARY_SKY, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN, Blocks.DIAMOND_BLOCK.defaultBlockState()));
        context.register(CulturesPM.SUN, new Culture(ResourceUtils.loc("sun"), LootTablesPM.LIBRARY_SUN, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN, Blocks.GOLD_BLOCK.defaultBlockState()));
        context.register(CulturesPM.MOON, new Culture(ResourceUtils.loc("moon"), LootTablesPM.LIBRARY_MOON, LootTablesPM.LIBRARY_WELCOME, LootTablesPM.LIBRARY_HIDDEN, Blocks.IRON_BLOCK.defaultBlockState()));
        context.register(CulturesPM.FORBIDDEN, new Culture(ResourceUtils.loc("forbidden"), LootTablesPM.LIBRARY_FORBIDDEN, LootTablesPM.LIBRARY_WARNING, BuiltInLootTables.EMPTY, Blocks.REDSTONE_BLOCK.defaultBlockState()));
    }
}
