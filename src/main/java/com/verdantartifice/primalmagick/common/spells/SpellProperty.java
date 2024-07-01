package com.verdantartifice.primalmagick.common.spells;

import net.minecraft.network.chat.Component;

/**
 * Definition of a spell property.  Spell components have zero to two properties that determine their
 * functionality (e.g. power, duration).  Each property is bounded to a min and max value.
 * 
 * @author Daedalus4096
 */
public record SpellProperty(String name, String translationKey, int min, int max) {
    public Component getDescription() {
        return Component.translatable(this.translationKey);
    }
}
