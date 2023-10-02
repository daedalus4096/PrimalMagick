package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.Map;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of a Mine spell mod.  This mod causes the spell package, rather than execute its payload
 * upon the target immediately, to create a spell mine entity at the target location.  The mine has a
 * short arming time, after which it will trigger when any entity comes within a block of it.  This
 * causes the mine to execute its contained spell payload upon the triggering entity.  The spell mod's
 * duration property determines how long the mine exists before despawning.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.entities.projectiles.SpellMineEntity}
 */
public class MineSpellMod extends AbstractSpellMod {
    public static final String TYPE = "mine";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_MOD_MINE.get().compoundKey();

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
        propMap.put("duration", new SpellProperty("duration", "spells.primalmagick.property.duration", 1, 5));
        return propMap;
    }
    
    @Override
    public int getBaseManaCostModifier() {
        return this.getPropertyValue("duration");
    }
    
    @Override
    public int getManaCostMultiplier() {
        return 1;
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
    
    public int getDurationMinutes(SpellPackage spell, ItemStack spellSource) {
        return 4 * this.getModdedPropertyValue("duration", spell, spellSource);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.mod." + this.getModType() + ".detail_tooltip", this.getDurationMinutes(spell, spellSource));
    }
}
