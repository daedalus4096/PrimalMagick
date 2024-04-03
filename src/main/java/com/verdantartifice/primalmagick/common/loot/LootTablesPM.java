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
    
    public static final ResourceLocation LIBRARY_TEST = register("gameplay/library/test");
    public static final ResourceLocation LIBRARY_EARTH = register("gameplay/library/earth");
    public static final ResourceLocation LIBRARY_SEA = register("gameplay/library/sea");
    public static final ResourceLocation LIBRARY_SKY = register("gameplay/library/sky");
    public static final ResourceLocation LIBRARY_SUN = register("gameplay/library/sun");
    public static final ResourceLocation LIBRARY_MOON = register("gameplay/library/moon");
    public static final ResourceLocation LIBRARY_WELCOME = register("gameplay/library/welcome");
    public static final ResourceLocation LIBRARY_HIDDEN = register("gameplay/library/hidden");
    
    public static final ResourceLocation LIBRARY_CULTURE_EARTH = register("gameplay/library/culture/earth");
    public static final ResourceLocation LIBRARY_CULTURE_SEA = register("gameplay/library/culture/sea");
    public static final ResourceLocation LIBRARY_CULTURE_SKY = register("gameplay/library/culture/sky");
    public static final ResourceLocation LIBRARY_CULTURE_SUN = register("gameplay/library/culture/sun");
    public static final ResourceLocation LIBRARY_CULTURE_MOON = register("gameplay/library/culture/moon");
    public static final ResourceLocation LIBRARY_CULTURE_TRADE = register("gameplay/library/culture/trade");
    public static final ResourceLocation LIBRARY_CULTURE_FORBIDDEN = register("gameplay/library/culture/forbidden");
    public static final ResourceLocation LIBRARY_CULTURE_HALLOWED = register("gameplay/library/culture/hallowed");
    
    public static final ResourceLocation LIBRARY_CATALOG_COMMON = register("gameplay/library/catalog/common");
    public static final ResourceLocation LIBRARY_CATALOG_UNCOMMON = register("gameplay/library/catalog/uncommon");
    public static final ResourceLocation LIBRARY_CATALOG_RARE = register("gameplay/library/catalog/rare");
    public static final ResourceLocation LIBRARY_CATALOG_TREASURE = register("gameplay/library/catalog/treasure");

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
