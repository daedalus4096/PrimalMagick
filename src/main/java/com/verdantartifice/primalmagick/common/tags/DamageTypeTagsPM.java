package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * Collection of custom-defined damage type tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class DamageTypeTagsPM {
    public static final TagKey<DamageType> IS_MAGIC = create("is_magic");
    public static final TagKey<DamageType> IS_SORCERY = create("is_sorcery");
    
    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, PrimalMagick.resource(name));
    }
}
