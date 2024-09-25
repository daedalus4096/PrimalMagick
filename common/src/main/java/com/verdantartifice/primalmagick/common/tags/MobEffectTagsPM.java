package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

/**
 * Collection of custom-defined mob effect tags for the mod.  Used to determine tag contents
 * and for data file generation.
 * 
 * @author Daedalus4096
 */
public class MobEffectTagsPM {
    public static final TagKey<MobEffect> IMMUNITY_PRIMALITE_GOLEM = create("immunity/primalite_golems");
    public static final TagKey<MobEffect> IMMUNITY_HEXIUM_GOLEM = create("immunity/hexium_golems");
    public static final TagKey<MobEffect> IMMUNITY_HALLOWSTEEL_GOLEM = create("immunity/hallowsteel_golems");

    private static TagKey<MobEffect> create(String name) {
        return TagKey.create(Registries.MOB_EFFECT, ResourceUtils.loc(name));
    }
}
