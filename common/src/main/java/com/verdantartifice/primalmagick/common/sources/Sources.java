package com.verdantartifice.primalmagick.common.sources;

import com.google.common.base.Suppliers;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Sources {
    private static final Map<ResourceLocation, Source> SOURCES = new HashMap<>();
    
    public static final Source EARTH = new Source(ResourceUtils.loc("earth"), 0x20702B, ChatFormatting.DARK_GREEN, 0.5D, StatsPM.MANA_SPENT_EARTH, 100);
    public static final Source SEA = new Source(ResourceUtils.loc("sea"), 0x117899, ChatFormatting.BLUE, 1.0D, StatsPM.MANA_SPENT_SEA, 200);
    public static final Source SKY = new Source(ResourceUtils.loc("sky"), 0x87CEEB, ChatFormatting.AQUA, 1.0D, StatsPM.MANA_SPENT_SKY, 300);
    public static final Source SUN = new Source(ResourceUtils.loc("sun"), 0xF9C81C, ChatFormatting.YELLOW, 0.75D, StatsPM.MANA_SPENT_SUN, 400);
    public static final Source MOON = new Source(ResourceUtils.loc("moon"), 0xD1DDE3, ChatFormatting.GRAY, 1.0D, StatsPM.MANA_SPENT_MOON, 500);
    public static final Source BLOOD = new Source(ResourceUtils.loc("blood"), 0x8A0303, ChatFormatting.DARK_RED, 1.5D, StatsPM.MANA_SPENT_BLOOD, ResearchEntries.DISCOVER_BLOOD, 600);
    public static final Source INFERNAL = new Source(ResourceUtils.loc("infernal"), 0xED2F1B, ChatFormatting.GOLD, 2.0D, StatsPM.MANA_SPENT_INFERNAL, ResearchEntries.DISCOVER_INFERNAL, 700);
    public static final Source VOID = new Source(ResourceUtils.loc("void"), 0x551A8B, ChatFormatting.DARK_PURPLE, 2.0D, StatsPM.MANA_SPENT_VOID, ResearchEntries.DISCOVER_VOID, 800);
    public static final Source HALLOWED = new Source(ResourceUtils.loc("hallowed"), 0xEEEBD9, ChatFormatting.WHITE, 3.0D, StatsPM.MANA_SPENT_HALLOWED, ResearchEntries.DISCOVER_HALLOWED, 900);
    
    private static Supplier<List<Source>> cachedGetAllSorted = Suppliers.memoize(Sources::getAllSortedInner);
    
    @Nullable
    public static Source get(ResourceLocation id) {
        return SOURCES.get(id);
    }

    public static Collection<Source> getAll() {
        return Collections.unmodifiableCollection(SOURCES.values());
    }
    
    public static List<Source> getAllSorted() {
        return cachedGetAllSorted.get();
    }
    
    private static List<Source> getAllSortedInner() {
        return streamSorted().toList();
    }
    
    public static Stream<Source> stream() {
        return SOURCES.values().stream();
    }
    
    public static Stream<Source> streamSorted() {
        return stream().sorted((a, b) -> Integer.compare(a.getSortOrder(), b.getSortOrder()));
    }
    
    private static void invalidate() {
        cachedGetAllSorted = Suppliers.memoize(Sources::getAllSortedInner);
    }

    public static void register(Source source) {
        if (SOURCES.containsKey(source.getId())) {
            // Don't allow a given source to be registered more than once
            throw new IllegalArgumentException("Source " + source.getId().toString() + " already registered!");
        } else {
            SOURCES.put(source.getId(), source);
            invalidate();
        }
    }
}
