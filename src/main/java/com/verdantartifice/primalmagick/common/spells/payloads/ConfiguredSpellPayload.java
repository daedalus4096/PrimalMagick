package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Map;

import com.verdantartifice.primalmagick.common.spells.AbstractConfiguredSpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

public class ConfiguredSpellPayload<T extends ISpellPayload> extends AbstractConfiguredSpellComponent<T> {
    public ConfiguredSpellPayload(T payload) {
        super(payload);
    }
    
    public ConfiguredSpellPayload(T payload, Map<SpellProperty, Integer> configuredProperties) {
        super(payload, configuredProperties);
    }

    /**
     * Get the base cost of the spell payload.
     * 
     * @return the base cost of the spell payload
     */
    public int getBaseManaCost() {
        return this.component.getBaseManaCost(this.configuredProperties);
    }
}
