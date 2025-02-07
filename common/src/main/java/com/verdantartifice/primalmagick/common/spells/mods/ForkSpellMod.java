package com.verdantartifice.primalmagick.common.spells.mods;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;
import com.verdantartifice.primalmagick.common.util.VectorUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Definition of the Fork spell mod.  This mod causes the spell package to execute multiple instances
 * of the spell in a scattered cone of effect.  Its forks property determines how many instances of
 * the spell are executed.  Its precision property determines how wide the cone of effect is; higher
 * values result in a more narrow cone.
 * 
 * @author Daedalus4096
 */
public class ForkSpellMod extends AbstractSpellMod<ForkSpellMod> {
    public static final ForkSpellMod INSTANCE = new ForkSpellMod();
    
    public static final MapCodec<ForkSpellMod> CODEC = MapCodec.unit(ForkSpellMod.INSTANCE);
    public static final StreamCodec<ByteBuf, ForkSpellMod> STREAM_CODEC = StreamCodec.unit(ForkSpellMod.INSTANCE);
    
    public static final String TYPE = "fork";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_MOD_FORK));
    protected static final Supplier<List<SpellProperty>> PROPERTIES = () -> Arrays.asList(SpellPropertiesPM.FORKS.get(), SpellPropertiesPM.PRECISION.get());

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static ForkSpellMod getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellModType<ForkSpellMod> getType() {
        return SpellModsPM.FORK.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return PROPERTIES.get();
    }

    @Override
    public int getBaseManaCostModifier(SpellPropertyConfiguration properties) {
        return 0;
    }

    @Override
    public int getManaCostMultiplier(SpellPropertyConfiguration properties) {
        int forks = properties.get(SpellPropertiesPM.FORKS.get());
        int precision = properties.get(SpellPropertiesPM.PRECISION.get());
        return 1 + (forks * forks) + (precision * precision);
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
    
    @Nonnull
    public List<Vec3> getDirectionUnitVectors(@Nonnull Vec3 dir, @Nonnull RandomSource rng, @Nonnull SpellPackage spell, @Nonnull ItemStack spellSource) {
        // Determine the direction vectors on which to execute the spell forks
        List<Vec3> retVal = new ArrayList<>();
        Vec3 normDir = dir.normalize();
        int forks = this.getForkCount(spell, spellSource);
        int degrees = this.getSpreadDegrees(spell, spellSource);
        double offsetMagnitude = Math.tan(Math.toRadians(degrees)); // Vector offset length needed to produce a given degree angle
        
        for (int index = 0; index < forks; index++) {
            // Scale the offest vector to provide a degree displacement *up to* the computed degree value
            Vec3 offset = VectorUtils.getRandomOrthogonalUnitVector(normDir, rng).scale(offsetMagnitude * rng.nextDouble());
            retVal.add(normDir.add(offset));
        }
        
        return retVal;
    }
    
    protected int getForkCount(SpellPackage spell, ItemStack spellSource) {
        return spell.getMod(SpellModsPM.FORK.get()).orElseThrow().getPropertyValue(SpellPropertiesPM.FORKS.get());
    }
    
    protected int getSpreadDegrees(SpellPackage spell, ItemStack spellSource) {
        int precision = spell.getMod(SpellModsPM.FORK.get()).orElseThrow().getPropertyValue(SpellPropertiesPM.PRECISION.get());
        return 10 + (15 * (5 - precision));  // 85, 70, 55, 40, 25, 10 degrees max from the given direction
    }
    
    protected String getSpreadDegreesText(SpellPackage spell, ItemStack spellSource) {
        return "" + this.getSpreadDegrees(spell, spellSource) + "\u00B0";
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource, HolderLookup.Provider registries) {
        return Component.translatable("spells.primalmagick.mod." + this.getModType() + ".detail_tooltip", this.getForkCount(spell, spellSource), this.getSpreadDegreesText(spell, spellSource));
    }
}
