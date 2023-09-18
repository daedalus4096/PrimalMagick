package com.verdantartifice.primalmagick.client.tips;

import java.util.Optional;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

import net.minecraft.network.chat.Component;

/**
 * Contains the definition of a game hint that can be displayed to the user in the Grimoire if
 * they've advanced sufficiently far in mod progression.
 * 
 * @author Daedalus4096
 */
public record TipDefinition(String translationKey, Optional<CompoundResearchKey> requiredResearch) {
    public Component getText() {
        return Component.translatable(this.translationKey);
    }
}
