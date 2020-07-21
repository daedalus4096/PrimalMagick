package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public void playSounds(World world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.BLOOD.get(), SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }
    
    @Override
    protected DamageSource getDamageSource(Entity target, LivingEntity source) {
        // Bypass the target's armor, if any
        return super.getDamageSource(target, source).setDamageBypassesArmor();
    }

    @Override
    protected float getTotalDamage(Entity target, SpellPackage spell, ItemStack spellSource) {
        return 3.0F + this.getModdedPropertyValue("power", spell, spellSource);
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    @Override
    public int getBaseManaCost() {
        return 2 * this.getPropertyValue("power");
    }
}
