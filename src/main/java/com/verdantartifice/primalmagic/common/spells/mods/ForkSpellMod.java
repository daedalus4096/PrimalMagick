package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

public class ForkSpellMod extends AbstractSpellMod {
    public static final String TYPE = "fork";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_MOD_FORK"));

    public ForkSpellMod() {
        super();
    }
    
    public ForkSpellMod(int forks, int precision) {
        super();
        this.getProperty("forks").setValue(forks);
        this.getProperty("precision").setValue(precision);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("forks", new SpellProperty("forks", "primalmagic.spell.property.forks", 2, 5));
        propMap.put("precision", new SpellProperty("precision", "primalmagic.spell.property.precision", 1, 5));
        return propMap;
    }

    @Override
    public int getBaseManaCostModifier() {
        return 0;
    }

    @Override
    public int getManaCostMultiplier() {
        int forks = this.getPropertyValue("forks");
        int precision = this.getPropertyValue("precision");
        return 1 + (forks * forks) + (precision * precision);
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
}
