package com.verdantartifice.primalmagic.common.spells.mods;

public class EmptySpellMod extends AbstractSpellMod {
    public static final String TYPE = "none";

    public EmptySpellMod() {
        super();
    }
    
    @Override
    protected String getModType() {
        return TYPE;
    }
}
