package com.verdantartifice.primalmagick.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Collection of custom-defined extension recipe serializer tags.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class RecipeSerializerTagsExt {
    public static final TagKey<RecipeSerializer<?>> CREATE_AUTOMATION_IGNORE = create("create", "automation_ignore");
    
    private static TagKey<RecipeSerializer<?>> create(String modId, String name) {
        return TagKey.create(Registries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(modId, name));
    }
}
