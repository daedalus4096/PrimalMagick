package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.world.entity.player.Player;

/**
 * Requirement that the player has completed a given research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchEntryRequirement extends AbstractRequirement {
    public static final Codec<ResearchEntryRequirement> CODEC = ResearchEntryKey.CODEC.fieldOf("rootKey").xmap(ResearchEntryRequirement::new, req -> req.rootKey).codec();
    
    protected final ResearchEntryKey rootKey;
    
    public ResearchEntryRequirement(ResearchEntryKey rootKey) {
        this.rootKey = rootKey;
    }

    @Override
    public boolean isMetBy(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected RequirementType<?> getType() {
        return RequirementsPM.RESEARCH_ENTRY.get();
    }
}
