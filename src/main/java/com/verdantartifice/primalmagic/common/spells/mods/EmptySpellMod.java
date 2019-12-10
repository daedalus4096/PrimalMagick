package com.verdantartifice.primalmagic.common.spells.mods;

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
}
