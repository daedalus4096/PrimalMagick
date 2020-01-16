package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;
import com.verdantartifice.primalmagic.common.util.VectorUtils;

import net.minecraft.util.math.Vec3d;

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
        propMap.put("precision", new SpellProperty("precision", "primalmagic.spell.property.precision", 0, 5));
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
    
    @Nonnull
    public List<Vec3d> getDirectionUnitVectors(@Nonnull Vec3d dir, @Nonnull Random rng) {
        List<Vec3d> retVal = new ArrayList<>();
        Vec3d normDir = dir.normalize();
        int forks = this.getPropertyValue("forks");
        int precision = this.getPropertyValue("precision");
        int degrees = 10 + (15 * (5 - precision));  // 85, 70, 55, 40, 25, 10
        double offsetMagnitude = Math.tan(Math.toRadians(degrees));
        
        for (int index = 0; index < forks; index++) {
            Vec3d offset = VectorUtils.getRandomOrthogonalUnitVector(normDir, rng).scale(offsetMagnitude * rng.nextDouble());
            retVal.add(normDir.add(offset));
        }
        
        return retVal;
    }
}
