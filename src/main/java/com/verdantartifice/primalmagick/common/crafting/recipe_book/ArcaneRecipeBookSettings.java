package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;

/**
 * Settings for the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBookSettings {
    private static final Map<ArcaneRecipeBookType, Pair<String, String>> TAG_FIELDS = ImmutableMap.of(
            ArcaneRecipeBookType.CRAFTING, Pair.of("isArcaneWorkbenchGuiOpen", "isArcaneWorkbenchFilteringCraftable"), 
            ArcaneRecipeBookType.CONCOCTER, Pair.of("isConcocterGuiOpen", "isConcocterFilteringCraftable"),
            ArcaneRecipeBookType.DISSOLUTION, Pair.of("isDissolutionGuiOpen", "isDissolutionFilteringCraftable"),
            ArcaneRecipeBookType.FURNACE, Pair.of("isInfernalFurnaceGuiOpen", "isInfernalFurnaceFilteringCraftable"));
    private final Map<ArcaneRecipeBookType, ArcaneRecipeBookSettings.TypeSettings> states;

    private ArcaneRecipeBookSettings(Map<ArcaneRecipeBookType, ArcaneRecipeBookSettings.TypeSettings> states) {
        this.states = states;
    }

    public ArcaneRecipeBookSettings() {
        this(Util.make(Maps.newEnumMap(ArcaneRecipeBookType.class), (map) -> {
            for(ArcaneRecipeBookType type : ArcaneRecipeBookType.values()) {
                map.put(type, new ArcaneRecipeBookSettings.TypeSettings(false, false));
            }
        }));
    }
    
    public boolean isOpen(ArcaneRecipeBookType type) {
        return this.states.get(type).open;
    }
    
    public void setOpen(ArcaneRecipeBookType type, boolean open) {
        this.states.get(type).open = open;
    }
    
    public boolean isFiltering(ArcaneRecipeBookType type) {
        return this.states.get(type).filtering;
    }
    
    public void setFiltering(ArcaneRecipeBookType type, boolean filtering) {
        this.states.get(type).filtering = filtering;
    }
    
    public void clear() {
        for (ArcaneRecipeBookType type : ArcaneRecipeBookType.values()) {
            this.setOpen(type, false);
            this.setFiltering(type, false);
        }
    }
    
    public static ArcaneRecipeBookSettings read(CompoundTag tag) {
        Map<ArcaneRecipeBookType, ArcaneRecipeBookSettings.TypeSettings> map = Maps.newEnumMap(ArcaneRecipeBookType.class);
        TAG_FIELDS.forEach((type, pair) -> {
            map.put(type, new ArcaneRecipeBookSettings.TypeSettings(tag.getBoolean(pair.getFirst()), tag.getBoolean(pair.getSecond())));
        });
        return new ArcaneRecipeBookSettings(map);
    }
    
    public void write(CompoundTag tag) {
        TAG_FIELDS.forEach((type, pair) -> {
            ArcaneRecipeBookSettings.TypeSettings typeSettings = this.states.get(type);
            tag.putBoolean(pair.getFirst(), typeSettings.open);
            tag.putBoolean(pair.getSecond(), typeSettings.filtering);
        });
    }
    
    public ArcaneRecipeBookSettings copy() {
        Map<ArcaneRecipeBookType, ArcaneRecipeBookSettings.TypeSettings> map = Maps.newEnumMap(ArcaneRecipeBookType.class);
        for (ArcaneRecipeBookType type : ArcaneRecipeBookType.values()) {
            map.put(type, this.states.get(type).copy());
        }
        return new ArcaneRecipeBookSettings(map);
    }
    
    public void replaceFrom(ArcaneRecipeBookSettings other) {
        this.states.clear();
        for (ArcaneRecipeBookType type : ArcaneRecipeBookType.values()) {
            this.states.put(type, other.states.get(type).copy());
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(states);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArcaneRecipeBookSettings other = (ArcaneRecipeBookSettings) obj;
        return Objects.equals(states, other.states);
    }

    protected static class TypeSettings {
        public boolean open;
        public boolean filtering;
        
        public TypeSettings(boolean open, boolean filtering) {
            this.open = open;
            this.filtering = filtering;
        }
        
        public ArcaneRecipeBookSettings.TypeSettings copy() {
            return new ArcaneRecipeBookSettings.TypeSettings(this.open, this.filtering);
        }

        @Override
        public int hashCode() {
            return Objects.hash(filtering, open);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TypeSettings other = (TypeSettings) obj;
            return filtering == other.filtering && open == other.open;
        }

        public String toString() {
           return "[open=" + this.open + ", filtering=" + this.filtering + "]";
        }
    }
}
