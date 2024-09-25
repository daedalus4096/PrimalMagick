package com.verdantartifice.primalmagick.common.loot;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores IDs for built-in mod loot tables, i.e. loot tables which are not based directly on a block or entity ID.
 * 
 * @author Daedalus4096
 */
public class LootTablesPM {
    private static final Set<ResourceKey<LootTable>> LOCATIONS = new HashSet<>();
    private static final Set<ResourceKey<LootTable>> SUBSET_TREEFOLK_BARTERING = new HashSet<>();
    private static final Set<ResourceKey<LootTable>> SUBSET_THEORYCRAFTING = new HashSet<>();
    private static final Set<ResourceKey<LootTable>> SUBSET_LIBRARY = new HashSet<>();

    public static final ResourceKey<LootTable> TREEFOLK_BARTERING = registerTreefolk("gameplay/treefolk_bartering");
    public static final ResourceKey<LootTable> TREEFOLK_BARTERING_FOOD = registerTreefolk("gameplay/treefolk_bartering/food");
    public static final ResourceKey<LootTable> TREEFOLK_BARTERING_SAPLINGS = registerTreefolk("gameplay/treefolk_bartering/saplings");
    public static final ResourceKey<LootTable> TREEFOLK_BARTERING_SEEDS = registerTreefolk("gameplay/treefolk_bartering/seeds");
    public static final ResourceKey<LootTable> TREEFOLK_BARTERING_JUNK = registerTreefolk("gameplay/treefolk_bartering/junk");
    public static final ResourceKey<LootTable> TREEFOLK_BARTERING_TREASURE = registerTreefolk("gameplay/treefolk_bartering/treasure");
    
    public static final ResourceKey<LootTable> THEORYCRAFTING_TRADE = registerTheorycraft("gameplay/theorycrafting/trade");
    public static final ResourceKey<LootTable> THEORYCRAFTING_PROSPEROUS_TRADE = registerTheorycraft("gameplay/theorycrafting/prosperous_trade");
    public static final ResourceKey<LootTable> THEORYCRAFTING_RICH_TRADE = registerTheorycraft("gameplay/theorycrafting/rich_trade");
    
    public static final ResourceKey<LootTable> LIBRARY_EARTH = registerLibrary("gameplay/library/earth");
    public static final ResourceKey<LootTable> LIBRARY_SEA = registerLibrary("gameplay/library/sea");
    public static final ResourceKey<LootTable> LIBRARY_SKY = registerLibrary("gameplay/library/sky");
    public static final ResourceKey<LootTable> LIBRARY_SUN = registerLibrary("gameplay/library/sun");
    public static final ResourceKey<LootTable> LIBRARY_MOON = registerLibrary("gameplay/library/moon");
    public static final ResourceKey<LootTable> LIBRARY_FORBIDDEN = registerLibrary("gameplay/library/forbidden");
    public static final ResourceKey<LootTable> LIBRARY_WELCOME = registerLibrary("gameplay/library/welcome");
    public static final ResourceKey<LootTable> LIBRARY_WARNING = registerLibrary("gameplay/library/warning");
    public static final ResourceKey<LootTable> LIBRARY_HIDDEN = registerLibrary("gameplay/library/hidden");
    public static final ResourceKey<LootTable> LIBRARY_ARCHAEOLOGY = registerLibrary("gameplay/library/archaeology");
    
    public static final ResourceKey<LootTable> LIBRARY_CULTURE_EARTH = registerLibrary("gameplay/library/culture/earth");
    public static final ResourceKey<LootTable> LIBRARY_CULTURE_SEA = registerLibrary("gameplay/library/culture/sea");
    public static final ResourceKey<LootTable> LIBRARY_CULTURE_SKY = registerLibrary("gameplay/library/culture/sky");
    public static final ResourceKey<LootTable> LIBRARY_CULTURE_SUN = registerLibrary("gameplay/library/culture/sun");
    public static final ResourceKey<LootTable> LIBRARY_CULTURE_MOON = registerLibrary("gameplay/library/culture/moon");
    public static final ResourceKey<LootTable> LIBRARY_CULTURE_TRADE = registerLibrary("gameplay/library/culture/trade");
    public static final ResourceKey<LootTable> LIBRARY_CULTURE_FORBIDDEN = registerLibrary("gameplay/library/culture/forbidden");
    public static final ResourceKey<LootTable> LIBRARY_CULTURE_HALLOWED = registerLibrary("gameplay/library/culture/hallowed");
    
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_COMMON = registerLibrary("gameplay/library/catalog/common");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_UNCOMMON = registerLibrary("gameplay/library/catalog/uncommon");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_RARE = registerLibrary("gameplay/library/catalog/rare");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_EPIC_EARTH = registerLibrary("gameplay/library/catalog/epic/earth");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_EPIC_SEA = registerLibrary("gameplay/library/catalog/epic/sea");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_EPIC_SKY = registerLibrary("gameplay/library/catalog/epic/sky");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_EPIC_SUN = registerLibrary("gameplay/library/catalog/epic/sun");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_EPIC_MOON = registerLibrary("gameplay/library/catalog/epic/moon");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_EPIC_TRADE = registerLibrary("gameplay/library/catalog/epic/trade");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_EPIC_FORBIDDEN = registerLibrary("gameplay/library/catalog/epic/forbidden");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_EPIC_HALLOWED = registerLibrary("gameplay/library/catalog/epic/hallowed");
    public static final ResourceKey<LootTable> LIBRARY_CATALOG_TREASURE = registerLibrary("gameplay/library/catalog/treasure");

    private static ResourceKey<LootTable> registerTreefolk(String id) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, ResourceUtils.loc(id)), SUBSET_TREEFOLK_BARTERING);
    }

    private static ResourceKey<LootTable> registerTheorycraft(String id) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, ResourceUtils.loc(id)), SUBSET_THEORYCRAFTING);
    }

    private static ResourceKey<LootTable> registerLibrary(String id) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, ResourceUtils.loc(id)), SUBSET_LIBRARY);
    }

    private static ResourceKey<LootTable> register(ResourceKey<LootTable> id, Set<ResourceKey<LootTable>> tableSubset) {
        if (LOCATIONS.add(id) && tableSubset.add(id)) {
            return id;
        } else {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceKey<LootTable>> all() {
        return Collections.unmodifiableSet(LOCATIONS);
    }

    public static Set<ResourceKey<LootTable>> treefolkBartering() {
        return Collections.unmodifiableSet(SUBSET_TREEFOLK_BARTERING);
    }

    public static Set<ResourceKey<LootTable>> theorycrafting() {
        return Collections.unmodifiableSet(SUBSET_THEORYCRAFTING);
    }

    public static Set<ResourceKey<LootTable>> library() {
        return Collections.unmodifiableSet(SUBSET_LIBRARY);
    }
}
