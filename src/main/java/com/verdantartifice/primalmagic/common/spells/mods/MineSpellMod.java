package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

public class MineSpellMod extends AbstractSpellMod {
    public static final String TYPE = "mine";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_MOD_MINE"));

    public MineSpellMod() {
        super();
    }
    
    public MineSpellMod(int duration) {
        super();
        this.getProperty("duration").setValue(duration);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("duration", new SpellProperty("duration", "primalmagic.spell.property.duration", 1, 5));
        return propMap;
    }
    
    @Override
    public SourceList modifyManaCost(SourceList cost) {
        SourceList newCost = cost.copy();
        for (Source source : newCost.getSources()) {
            if (newCost.getAmount(source) > 0) {
                newCost.add(source, this.getPropertyValue("duration"));
            }
        }
        return newCost;
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
}
