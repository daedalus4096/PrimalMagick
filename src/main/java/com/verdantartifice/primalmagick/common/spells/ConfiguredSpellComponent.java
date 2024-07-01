package com.verdantartifice.primalmagick.common.spells;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Mth;

/**
 * A spell component that has been configured with values for any properties it has
 * 
 * @author Daedalus4096
 */
public class ConfiguredSpellComponent {
    protected final ISpellComponent component;
    protected final Map<SpellProperty, Integer> configuredProperties;
    
    public ConfiguredSpellComponent(ISpellComponent component, Map<SpellProperty, Integer> configuredProperties) {
        this.component = component;
        this.configuredProperties = new HashMap<>(configuredProperties);
    }
    
    public int getPropertyValue(SpellProperty property) {
        return this.configuredProperties.getOrDefault(property, 0);
    }
    
    public void setPropertyValue(SpellProperty property, int value) {
        if (this.component.getProperties().contains(property)) {
            // Ensure that the given value respects this property's bounds
            this.configuredProperties.put(property, Mth.clamp(value, property.min(), property.max()));
        }
    }
    
    /**
     * Determines whether all of this component's properties have been configured with a value.
     * 
     * @return whether all of this component's properties have been configured with a value
     */
    public boolean isFullyConfigured() {
        return this.component.getProperties().stream().allMatch(prop -> this.configuredProperties.containsKey(prop));
    }
}
