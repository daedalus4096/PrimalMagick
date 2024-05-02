package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.player.Player;

/**
 * Requirement that the player has completed a given research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchEntryRequirement extends AbstractRequirement {
    public static final Codec<ResearchEntryRequirement> CODEC = Codec.STRING.fieldOf("rootKey").xmap(ResearchEntryRequirement::new, req -> req.rootKey).codec();
    
    protected final String rootKey; // TODO Replace with a ResourceKey once the research system refactor is complete
    
    public ResearchEntryRequirement(String rootKey) {
        this.rootKey = rootKey;
    }

    @Override
    public boolean isKnownBy(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected RequirementType<?> getType() {
        return RequirementsPM.RESEARCH_ENTRY.get();
    }
}
