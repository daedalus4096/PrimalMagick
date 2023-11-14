package com.verdantartifice.primalmagick.common.books;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Record containing all the parameters needed to render a player's view of an encoded static book.
 * 
 * @author Daedalus4096
 */
public record BookView(ResourceKey<?> bookKey, ResourceLocation languageId, int comprehension) {

}
