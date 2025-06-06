package com.verdantartifice.primalmagick.common.spells;

import net.minecraft.util.Mth;

import java.util.Map;
import java.util.Objects;

/**
 * A spell component that has been configured with values for any properties it has
 * 
 * @author Daedalus4096
 */
public abstract class AbstractConfiguredSpellComponent<T extends ISpellComponent> {
    protected final T component;
    protected final SpellPropertyConfiguration configuredProperties;
    
    protected AbstractConfiguredSpellComponent(T component) {
        this.component = component;
        this.configuredProperties = SpellPropertyConfiguration.EMPTY.copy();
    }
    
    protected AbstractConfiguredSpellComponent(T component, Map<SpellProperty, Integer> configuredProperties) {
        this.component = component;
        this.configuredProperties = new SpellPropertyConfiguration(configuredProperties);
    }
    
    protected AbstractConfiguredSpellComponent(T component, SpellPropertyConfiguration configuredProperties) {
        this.component = component;
        this.configuredProperties = new SpellPropertyConfiguration(configuredProperties);
    }
    
    public T getComponent() {
        return this.component;
    }
    
    public int getPropertyValue(SpellProperty property) {
        return this.configuredProperties.getOrDefault(property, property.min());
    }
    
    public void setPropertyValue(SpellProperty property, int value) {
        if (this.component.getProperties().contains(property)) {
            // Ensure that the given value respects this property's bounds
            this.configuredProperties.set(property, Mth.clamp(value, property.min(), property.max()));
        }
    }
    
    /**
     * Determines whether all of this component's properties have been configured with a value.
     * 
     * @return whether all of this component's properties have been configured with a value
     */
    public boolean isFullyConfigured() {
        return this.component != null && this.component.getProperties().stream().allMatch(prop -> this.configuredProperties.contains(prop));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AbstractConfiguredSpellComponent<?> that)) return false;
        return Objects.equals(component, that.component) && Objects.equals(configuredProperties, that.configuredProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(component, configuredProperties);
    }
}
