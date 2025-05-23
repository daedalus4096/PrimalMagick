package com.verdantartifice.primalmagick.common.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Map;
import java.util.Objects;

public class SpellPropertyConfiguration {
    public static final Codec<SpellPropertyConfiguration> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(Codec.simpleMap(SpellProperty.CODEC, Codec.INT, StringRepresentable.keys(SpellPropertiesPM.getAll().toArray(SpellProperty[]::new))).xmap(Object2IntOpenHashMap::new, Object2IntOpenHashMap::new).fieldOf("spellProperties").forGetter(spc -> spc.propertyValues)).apply(instance, SpellPropertyConfiguration::new);
    });
    public static final StreamCodec<ByteBuf, SpellPropertyConfiguration> STREAM_CODEC = ByteBufCodecs.map(Object2IntOpenHashMap::new, SpellProperty.STREAM_CODEC, ByteBufCodecs.VAR_INT).map(SpellPropertyConfiguration::new, l -> l.propertyValues);
    
    public static final SpellPropertyConfiguration EMPTY = new SpellPropertyConfiguration();
    
    protected final Object2IntOpenHashMap<SpellProperty> propertyValues;
    
    protected SpellPropertyConfiguration() {
        this.propertyValues = new Object2IntOpenHashMap<>();
    }
    
    protected SpellPropertyConfiguration(SpellPropertyConfiguration other) {
        this.propertyValues = new Object2IntOpenHashMap<>(other.propertyValues);
    }
    
    protected SpellPropertyConfiguration(Map<SpellProperty, Integer> values) {
        this.propertyValues = new Object2IntOpenHashMap<>(values);
    }
    
    public SpellPropertyConfiguration copy() {
        return new SpellPropertyConfiguration(this);
    }
    
    public int get(SpellProperty property) {
        // If the property is not yet present, default to its minimum possible value, which will not always be zero
        return this.getOrDefault(property, property.min());
    }
    
    public int getOrDefault(SpellProperty property, int defaultValue) {
        return this.propertyValues.getOrDefault(property, defaultValue);
    }
    
    public boolean contains(SpellProperty property) {
        return this.propertyValues.containsKey(property);
    }
    
    public void set(SpellProperty property, int value) {
        this.propertyValues.put(property, value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SpellPropertyConfiguration that)) return false;
        return Objects.equals(propertyValues, that.propertyValues);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(propertyValues);
    }
}
