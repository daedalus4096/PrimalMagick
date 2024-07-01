package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagick.common.spells.AbstractConfiguredSpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

public class ConfiguredSpellMod<T extends ISpellMod> extends AbstractConfiguredSpellComponent<T> {
    public ConfiguredSpellMod(T mod) {
        super(mod);
    }
    
    public ConfiguredSpellMod(T mod, Map<SpellProperty, Integer> configuredProperties) {
        super(mod, configuredProperties);
    }
    
    /**
     * Get the additive modifier to be applied to the spell mod's package's base cost.
     * 
     * @return the additive modifier for the spell package's cost
     */
    public int getBaseManaCostModifier() {
        return this.component.getBaseManaCostModifier(this.configuredProperties);
    }
    
    /**
     * Get the multiplicative modifier to be applied to the spell mod's package's total cost.
     * 
     * @return the multiplicative modifier for the spell package's cost
     */
    public int getManaCostMultiplier() {
        return this.component.getManaCostMultiplier(this.configuredProperties);
    }
}
