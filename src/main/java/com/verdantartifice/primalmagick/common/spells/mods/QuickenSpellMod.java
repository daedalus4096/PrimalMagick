package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

/**
 * Definition of the Quicken spell mod.  This mod causes spells to incur a shorter cooldown upon being
 * cast.  The mod's haste property determines how much the cooldown is reduced.
 * 
 * @author Daedalus4096
 */
public class QuickenSpellMod extends AbstractSpellMod {
    public static final String TYPE = "quicken";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_MOD_QUICKEN.get().compoundKey();

    public QuickenSpellMod() {
        super();
    }
    
    public QuickenSpellMod(int haste) {
        super();
        this.getProperty("haste").setValue(haste);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("haste", new SpellProperty("haste", "spells.primalmagick.property.haste", 1, 5));
        return propMap;
    }

    @Override
    public int getBaseManaCostModifier() {
        return 0;
    }
    
    @Override
    public int getManaCostMultiplier() {
        return 1 + this.getPropertyValue("haste");
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
}
