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
}
