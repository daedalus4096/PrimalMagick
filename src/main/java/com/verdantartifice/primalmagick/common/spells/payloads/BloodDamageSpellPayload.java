package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a blood damage spell.  Does standard damage to the target, and bypasses any armor
 * that it may have.  No secondary effects.
 * 
 * @author Daedalus4096
 */
public class BloodDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "blood_damage";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_BLOOD"));
    
    public BloodDamageSpellPayload() {
        super();
    }
    
    public BloodDamageSpellPayload(int power) {
        super(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    public Source getSource() {
        return Source.BLOOD;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.BLOOD.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }
    
    @Override
    protected float getBaseDamage(SpellPackage spell, ItemStack spellSource) {
        return 3.0F + (2.0F * this.getModdedPropertyValue("power", spell, spellSource));
    }

    @Override
    protected DamageSource getDamageSource(LivingEntity source, SpellPackage spell, Entity projectileEntity) {
        // Bypass the target's armor, if any
        return super.getDamageSource(source, spell, projectileEntity).bypassArmor();
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    @Override
    public int getBaseManaCost() {
        int power = this.getPropertyValue("power");
        return (1 << Math.max(0, power - 1)) + ((1 << Math.max(0, power - 1)) >> 1);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("primalmagick.spell.payload.detail_tooltip." + this.getPayloadType(), DECIMAL_FORMATTER.format(this.getBaseDamage(spell, spellSource)));
    }
}
