package com.verdantartifice.primalmagick.common.spells.mods;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

/**
 * Definition of an empty spell mod.  This mod has no effect and is not valid in spells.  Its only
 * purpose is to provide a selection entry in the spellcrafting altar GUI for when the player has
 * not selected either a primary or secondary mod for the spell.
 * 
 * @author Daedalus4096
 */
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
    public int getBaseManaCostModifier() {
        return 0;
    }
    
    @Override
    public int getManaCostMultiplier() {
        return 1;
    }
}
