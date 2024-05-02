package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.Codec;

/**
 * Base class representing an atom in the research hierarchy.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractResearchKey {
    public static final Codec<AbstractResearchKey> CODEC = ResearchKeyTypesPM.TYPES.get().getCodec().dispatch("key_type", AbstractResearchKey::getType, ResearchKeyType::codec);
    
    @Override
    public abstract String toString();

    protected abstract ResearchKeyType<?> getType();
}
