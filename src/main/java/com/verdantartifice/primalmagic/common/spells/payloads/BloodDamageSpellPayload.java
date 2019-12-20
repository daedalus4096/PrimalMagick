package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

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
    public void playSounds(World world, LivingEntity caster) {
        world.playSound(null, caster.getPosition(), SoundsPM.BLOOD, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }
    
    @Override
    protected DamageSource getDamageSource(Entity target, LivingEntity source) {
        return super.getDamageSource(target, source).setDamageBypassesArmor();
    }

    @Override
    protected float getTotalDamage() {
        return 3.0F + this.getPropertyValue("power");
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
