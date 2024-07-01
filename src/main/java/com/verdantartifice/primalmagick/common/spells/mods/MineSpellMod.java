package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
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
public class MineSpellMod extends AbstractSpellMod<MineSpellMod> {
    public static final MineSpellMod INSTANCE = new MineSpellMod();
    
    public static final MapCodec<MineSpellMod> CODEC = MapCodec.unit(MineSpellMod.INSTANCE);
    public static final StreamCodec<ByteBuf, MineSpellMod> STREAM_CODEC = StreamCodec.unit(MineSpellMod.INSTANCE);
    
    public static final String TYPE = "mine";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_MOD_MINE));
    protected static final List<SpellProperty> PROPERTIES = Arrays.asList(SpellPropertiesPM.MINE_DURATION.get());

    public MineSpellMod() {
    }
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    @Override
    public SpellModType<MineSpellMod> getType() {
        return SpellModsPM.MINE.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return PROPERTIES;
    }

    @Override
    public int getBaseManaCostModifier(SpellPropertyConfiguration properties) {
        return properties.get(SpellPropertiesPM.MINE_DURATION.get());
    }
    
    @Override
    public int getManaCostMultiplier(SpellPropertyConfiguration properties) {
        return 1;
    }

    @Override
    protected String getModType() {
        return TYPE;
    }
    
    public int getDurationMinutes(SpellPackage spell, ItemStack spellSource) {
        int duration = spell.getMod(SpellModsPM.MINE.get()).orElseThrow().getPropertyValue(SpellPropertiesPM.MINE_DURATION.get());
        return 4 * duration;
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.mod." + this.getModType() + ".detail_tooltip", this.getDurationMinutes(spell, spellSource));
    }
}
