package com.verdantartifice.primalmagick.common.spells;

import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/**
 * Definition of a spell property.  Spell components have zero to two properties that determine their
 * functionality (e.g. power, duration).  Each property is bounded to a min and max value.
 * 
 * @author Daedalus4096
 */
public class SpellProperty {
    protected int value;
    protected final String name;
    protected final String translationKey;
    protected final int min;
    protected final int max;
    
    public SpellProperty(@Nonnull String name, @Nonnull String translationKey, int min, int max) {
        this.name = name;
        this.translationKey = translationKey;
        this.min = min;
        this.max = max;
        this.value = min;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Component getDescription() {
        return Component.translatable(this.translationKey);
    }
    
    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getValue() {
        return this.value;
    }
    
    public void setValue(int newValue) {
        // Ensure that the given value respects this property's bounds
        this.value = Mth.clamp(newValue, this.min, this.max);
    }
}
