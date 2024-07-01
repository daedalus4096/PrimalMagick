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
}
