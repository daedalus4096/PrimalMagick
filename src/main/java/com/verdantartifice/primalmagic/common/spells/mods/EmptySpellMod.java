package com.verdantartifice.primalmagic.common.spells.mods;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.sources.SourceList;

public class EmptySpellMod extends AbstractSpellMod {
    public static final String TYPE = "none";

    public EmptySpellMod() {
        super();
    }
    
    @Override
    public boolean isActive() {
        return false;
    }
    
    @Override
    protected String getModType() {
        return TYPE;
    }
    
    public static CompoundResearchKey getResearch() {
        return null;
    }
    
    @Override
    public SourceList modifyManaCost(SourceList cost) {
        return cost;
    }
}
