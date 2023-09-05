package com.verdantartifice.primalmagick.common.registries;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookDefinition;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * Home for resource keys for custom mod registries.
 * 
 * @author Daedalus4096
 */
public class RegistryKeysPM {
    public static final ResourceKey<Registry<BookDefinition>> BOOKS = key("books");
    
    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(PrimalMagick.resource(name));
    }
}
