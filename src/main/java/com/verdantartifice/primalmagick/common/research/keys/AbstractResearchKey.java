package com.verdantartifice.primalmagick.common.research.keys;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;

import net.minecraft.world.entity.player.Player;

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
    
    public boolean isKnownBy(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            MutableBoolean retVal = new MutableBoolean(false);
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                retVal.setValue(knowledge.isResearchComplete(this));
            });
            return retVal.booleanValue();
        }
    }
    
    @Nullable
    public ResearchEntry getResearchEntry() {
        // Most research key types don't have a corresponding research entry; override in those that do
        return null;
    }
}
