package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

/**
 * Collection of custom-defined structure tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class StructureTagsPM {
    public static final TagKey<Structure> SHRINE = create("shrine");
    public static final TagKey<Structure> LIBRARY = create("library");

    private static TagKey<Structure> create(String name) {
        return TagKey.create(Registries.STRUCTURE, PrimalMagick.resource(name));
    }
}
