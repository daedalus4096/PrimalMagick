package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

public class AmplifySpellMod extends AbstractSpellMod {
    public static final String TYPE = "amplify";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_MOD_AMPLIFY"));

    public AmplifySpellMod() {
        super();
    }
    
    public AmplifySpellMod(int power) {
        super();
        this.getProperty("power").setValue(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("power", new SpellProperty("power", "primalmagic.spell.property.power", 1, 5));
        return propMap;
    }
    
    @Override
    public SourceList modifyManaCost(SourceList cost) {
        SourceList newCost = cost.copy();
        int power = this.getPropertyValue("power");
        for (Source source : cost.getSources()) {
            int amount = cost.getAmount(source);
            if (amount > 0) {
                newCost.add(source, amount * power);
            }
        }
        return newCost;
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
    
    @Override
    public int getModdedPropertyValue(String name, SpellPackage spell) {
        // Don't amplify self
        return this.getPropertyValue(name);
    }
}
