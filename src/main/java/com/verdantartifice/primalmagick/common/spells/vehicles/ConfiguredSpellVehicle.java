package com.verdantartifice.primalmagick.common.spells.vehicles;

import java.util.Map;

import com.verdantartifice.primalmagick.common.spells.AbstractConfiguredSpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

public class ConfiguredSpellVehicle<T extends ISpellVehicle> extends AbstractConfiguredSpellComponent<T> {
    public ConfiguredSpellVehicle(T vehicle) {
        super(vehicle);
    }
    
    public ConfiguredSpellVehicle(T vehicle, Map<SpellProperty, Integer> configuredProperties) {
        super(vehicle, configuredProperties);
    }
    
    /**
     * Get the additive modifier to be applied to the spell vehicle's package's base cost.
     * 
     * @return the additive modifier for the spell package's cost
     */
    public int getBaseManaCostModifier() {
        return this.component.getBaseManaCostModifier(this.configuredProperties);
    }
    
    /**
     * Get the multiplicative modifier to be applied to the spell vehicle's package's total cost.
     * 
     * @return the multiplicative modifier for the spell package's cost
     */
    public int getManaCostMultiplier() {
        return this.component.getManaCostMultiplier(this.configuredProperties);
    }
}
