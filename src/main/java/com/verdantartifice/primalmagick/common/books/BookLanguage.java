package com.verdantartifice.primalmagick.common.books;

import net.minecraft.resources.ResourceLocation;

/**
 * Definition for a language in which a static book can be written/encoded.
 * 
 * @author Daedalus4096
 */
public record BookLanguage(ResourceLocation languageId, ResourceLocation font, int complexity) {
    public boolean isTranslatable() {
        return this.complexity() > 0;
    }
}
