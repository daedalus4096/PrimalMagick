package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.tags.TagKey;

public class SpellPropertyTagsPM {
    public static final TagKey<SpellProperty> AMPLIFIABLE = create("amplifiable");
    
    private static TagKey<SpellProperty> create(String name) {
        return TagKey.create(RegistryKeysPM.SPELL_PROPERTIES, ResourceUtils.loc(name));
    }
}
