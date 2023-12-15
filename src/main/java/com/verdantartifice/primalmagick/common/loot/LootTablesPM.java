package com.verdantartifice.primalmagick.common.loot;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;

/**
 * Stores IDs for built-in mod loot tables, i.e. loot tables which are not based directly on a block or entity ID.
 * 
 * @author Daedalus4096
 */
public class LootTablesPM {
    private static final Set<ResourceLocation> LOCATIONS = new HashSet<>();

    public static final ResourceLocation TREEFOLK_BARTERING = register("gameplay/treefolk_bartering");
    public static final ResourceLocation TREEFOLK_BARTERING_FOOD = register("gameplay/treefolk_bartering/food");
    public static final ResourceLocation TREEFOLK_BARTERING_SAPLINGS = register("gameplay/treefolk_bartering/saplings");
    public static final ResourceLocation TREEFOLK_BARTERING_SEEDS = register("gameplay/treefolk_bartering/seeds");
    public static final ResourceLocation TREEFOLK_BARTERING_JUNK = register("gameplay/treefolk_bartering/junk");
    public static final ResourceLocation TREEFOLK_BARTERING_TREASURE = register("gameplay/treefolk_bartering/treasure");
    
    public static final ResourceLocation THEORYCRAFTING_TRADE = register("gameplay/theorycrafting/trade");
    public static final ResourceLocation THEORYCRAFTING_PROSPEROUS_TRADE = register("gameplay/theorycrafting/prosperous_trade");
    public static final ResourceLocation THEORYCRAFTING_RICH_TRADE = register("gameplay/theorycrafting/rich_trade");
    
    private static ResourceLocation register(String id) {
        return register(PrimalMagick.resource(id));
    }

    private static ResourceLocation register(ResourceLocation id) {
        if (LOCATIONS.add(id)) {
            return id;
        } else {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> all() {
        return Collections.unmodifiableSet(LOCATIONS);
    }
}
