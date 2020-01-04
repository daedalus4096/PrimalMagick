package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

public class QuickenSpellMod extends AbstractSpellMod {
    public static final String TYPE = "quicken";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_MOD_QUICKEN"));

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
        propMap.put("haste", new SpellProperty("haste", "primalmagic.spell.property.haste", 1, 5));
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
