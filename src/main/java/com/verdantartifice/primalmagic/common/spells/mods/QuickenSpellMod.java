package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
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
    public SourceList modifyManaCost(SourceList cost) {
        SourceList newCost = cost.copy();
        int haste = this.getPropertyValue("haste");
        for (Source source : cost.getSources()) {
            int amount = cost.getAmount(source);
            if (amount > 0) {
                newCost.add(source, amount * haste);
            }
        }
        return newCost;
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
}
