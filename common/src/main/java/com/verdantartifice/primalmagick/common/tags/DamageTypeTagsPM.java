package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
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
    public static final TagKey<DamageType> IS_SORCERY_EARTH = create("is_sorcery/earth");
    public static final TagKey<DamageType> IS_SORCERY_SEA = create("is_sorcery/sea");
    public static final TagKey<DamageType> IS_SORCERY_SKY = create("is_sorcery/sky");
    public static final TagKey<DamageType> IS_SORCERY_SUN = create("is_sorcery/sun");
    public static final TagKey<DamageType> IS_SORCERY_MOON = create("is_sorcery/moon");
    public static final TagKey<DamageType> IS_SORCERY_BLOOD = create("is_sorcery/blood");
    public static final TagKey<DamageType> IS_SORCERY_INFERNAL = create("is_sorcery/infernal");
    public static final TagKey<DamageType> IS_SORCERY_VOID = create("is_sorcery/void");
    public static final TagKey<DamageType> IS_SORCERY_HALLOWED = create("is_sorcery/hallowed");
    
    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceUtils.loc(name));
    }
}
