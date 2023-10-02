package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.util.VectorUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of the Fork spell mod.  This mod causes the spell package to execute multiple instances
 * of the spell in a scattered cone of effect.  Its forks property determines how many instances of
 * the spell are executed.  Its precision property determines how wide the cone of effect is; higher
 * values result in a more narrow cone.
 * 
 * @author Daedalus4096
 */
public class ForkSpellMod extends AbstractSpellMod {
    public static final String TYPE = "fork";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_MOD_FORK.get().compoundKey();

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
        propMap.put("forks", new SpellProperty("forks", "spells.primalmagick.property.forks", 2, 5));
        propMap.put("precision", new SpellProperty("precision", "spells.primalmagick.property.precision", 0, 5));
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
    public List<Vec3> getDirectionUnitVectors(@Nonnull Vec3 dir, @Nonnull RandomSource rng) {
        // Determine the direction vectors on which to execute the spell forks
        List<Vec3> retVal = new ArrayList<>();
        Vec3 normDir = dir.normalize();
        int forks = this.getForkCount();
        int degrees = this.getSpreadDegrees();
        double offsetMagnitude = Math.tan(Math.toRadians(degrees)); // Vector offset length needed to produce a given degree angle
        
        for (int index = 0; index < forks; index++) {
            // Scale the offest vector to provide a degree displacement *up to* the computed degree value
            Vec3 offset = VectorUtils.getRandomOrthogonalUnitVector(normDir, rng).scale(offsetMagnitude * rng.nextDouble());
            retVal.add(normDir.add(offset));
        }
        
        return retVal;
    }
    
    protected int getForkCount() {
        return this.getPropertyValue("forks");
    }
    
    protected int getSpreadDegrees() {
        int precision = this.getPropertyValue("precision");
        return 10 + (15 * (5 - precision));  // 85, 70, 55, 40, 25, 10 degrees max from the given direction
    }
    
    protected String getSpreadDegreesText() {
        return "" + this.getSpreadDegrees() + "\u00B0";
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.mod." + this.getModType() + ".detail_tooltip", this.getForkCount(), this.getSpreadDegreesText());
    }
}
