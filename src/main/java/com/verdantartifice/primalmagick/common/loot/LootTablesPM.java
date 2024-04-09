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
    private static final Set<ResourceLocation> SUBSET_TREEFOLK_BARTERING = new HashSet<>();
    private static final Set<ResourceLocation> SUBSET_THEORYCRAFTING = new HashSet<>();
    private static final Set<ResourceLocation> SUBSET_LIBRARY = new HashSet<>();

    public static final ResourceLocation TREEFOLK_BARTERING = registerTreefolk("gameplay/treefolk_bartering");
    public static final ResourceLocation TREEFOLK_BARTERING_FOOD = registerTreefolk("gameplay/treefolk_bartering/food");
    public static final ResourceLocation TREEFOLK_BARTERING_SAPLINGS = registerTreefolk("gameplay/treefolk_bartering/saplings");
    public static final ResourceLocation TREEFOLK_BARTERING_SEEDS = registerTreefolk("gameplay/treefolk_bartering/seeds");
    public static final ResourceLocation TREEFOLK_BARTERING_JUNK = registerTreefolk("gameplay/treefolk_bartering/junk");
    public static final ResourceLocation TREEFOLK_BARTERING_TREASURE = registerTreefolk("gameplay/treefolk_bartering/treasure");
    
    public static final ResourceLocation THEORYCRAFTING_TRADE = registerTheorycraft("gameplay/theorycrafting/trade");
    public static final ResourceLocation THEORYCRAFTING_PROSPEROUS_TRADE = registerTheorycraft("gameplay/theorycrafting/prosperous_trade");
    public static final ResourceLocation THEORYCRAFTING_RICH_TRADE = registerTheorycraft("gameplay/theorycrafting/rich_trade");
    
    public static final ResourceLocation LIBRARY_EARTH = registerLibrary("gameplay/library/earth");
    public static final ResourceLocation LIBRARY_SEA = registerLibrary("gameplay/library/sea");
    public static final ResourceLocation LIBRARY_SKY = registerLibrary("gameplay/library/sky");
    public static final ResourceLocation LIBRARY_SUN = registerLibrary("gameplay/library/sun");
    public static final ResourceLocation LIBRARY_MOON = registerLibrary("gameplay/library/moon");
    public static final ResourceLocation LIBRARY_FORBIDDEN = registerLibrary("gameplay/library/forbidden");
    public static final ResourceLocation LIBRARY_WELCOME = registerLibrary("gameplay/library/welcome");
    public static final ResourceLocation LIBRARY_WARNING = registerLibrary("gameplay/library/warning");
    public static final ResourceLocation LIBRARY_HIDDEN = registerLibrary("gameplay/library/hidden");
    
    public static final ResourceLocation LIBRARY_CULTURE_EARTH = registerLibrary("gameplay/library/culture/earth");
    public static final ResourceLocation LIBRARY_CULTURE_SEA = registerLibrary("gameplay/library/culture/sea");
    public static final ResourceLocation LIBRARY_CULTURE_SKY = registerLibrary("gameplay/library/culture/sky");
    public static final ResourceLocation LIBRARY_CULTURE_SUN = registerLibrary("gameplay/library/culture/sun");
    public static final ResourceLocation LIBRARY_CULTURE_MOON = registerLibrary("gameplay/library/culture/moon");
    public static final ResourceLocation LIBRARY_CULTURE_TRADE = registerLibrary("gameplay/library/culture/trade");
    public static final ResourceLocation LIBRARY_CULTURE_FORBIDDEN = registerLibrary("gameplay/library/culture/forbidden");
    public static final ResourceLocation LIBRARY_CULTURE_HALLOWED = registerLibrary("gameplay/library/culture/hallowed");
    
    public static final ResourceLocation LIBRARY_CATALOG_COMMON = registerLibrary("gameplay/library/catalog/common");
    public static final ResourceLocation LIBRARY_CATALOG_UNCOMMON = registerLibrary("gameplay/library/catalog/uncommon");
    public static final ResourceLocation LIBRARY_CATALOG_RARE = registerLibrary("gameplay/library/catalog/rare");
    public static final ResourceLocation LIBRARY_CATALOG_TREASURE = registerLibrary("gameplay/library/catalog/treasure");

    private static ResourceLocation registerTreefolk(String id) {
        return register(PrimalMagick.resource(id), SUBSET_TREEFOLK_BARTERING);
    }

    private static ResourceLocation registerTheorycraft(String id) {
        return register(PrimalMagick.resource(id), SUBSET_THEORYCRAFTING);
    }

    private static ResourceLocation registerLibrary(String id) {
        return register(PrimalMagick.resource(id), SUBSET_LIBRARY);
    }

    private static ResourceLocation register(ResourceLocation id, Set<ResourceLocation> tableSubset) {
        if (LOCATIONS.add(id) && tableSubset.add(id)) {
            return id;
        } else {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> all() {
        return Collections.unmodifiableSet(LOCATIONS);
    }

    public static Set<ResourceLocation> treefolkBartering() {
        return Collections.unmodifiableSet(SUBSET_TREEFOLK_BARTERING);
    }

    public static Set<ResourceLocation> theorycrafting() {
        return Collections.unmodifiableSet(SUBSET_THEORYCRAFTING);
    }

    public static Set<ResourceLocation> library() {
        return Collections.unmodifiableSet(SUBSET_LIBRARY);
    }
}
