package com.verdantartifice.primalmagick.common.research.requirements;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.world.entity.player.Player;

/**
 * Requirement that the player has completed a given research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchRequirement extends AbstractRequirement {
    public static final Codec<ResearchRequirement> CODEC = ResearchEntryKey.CODEC.fieldOf("rootKey").xmap(ResearchRequirement::new, req -> req.rootKey).codec();
    
    protected final ResearchEntryKey rootKey;
    
    public ResearchRequirement(ResearchEntryKey rootKey) {
        this.rootKey = Preconditions.checkNotNull(rootKey);
    }

    @Override
    public boolean isMetBy(Player player) {
        return player == null ? false : this.rootKey.isKnownBy(player);
    }

    @Override
    protected RequirementType<?> getType() {
        return RequirementsPM.RESEARCH.get();
    }
}
