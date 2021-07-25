package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.Map;

import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * Definition of a soul drain spell.  This payload afflicts the target with a potion effect which
 * causes soul gems to spawn on its death.  The length of the effect scales with the duration
 * property of the payload.  Has no effect on blocks.
 * 
 * @author Daedalus4096
 */
public class DrainSoulSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "drain_soul";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_DRAIN_SOUL"));
    protected static final int TICKS_PER_DURATION = 60;
    
    public DrainSoulSpellPayload() {
        super();
    }
    
    public DrainSoulSpellPayload(int duration) {
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
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() instanceof LivingEntity) {
                // Grant the potion effect
                LivingEntity entity = (LivingEntity)entityTarget.getEntity();
                int ticks = this.getModdedPropertyValue("duration", spell, spellSource) * TICKS_PER_DURATION;
                entity.addEffect(new MobEffectInstance(EffectsPM.DRAIN_SOUL.get(), ticks));
            }
        }
    }

    @Override
    public Source getSource() {
        return Source.INFERNAL;
    }

    @Override
    public int getBaseManaCost() {
        return 10 * this.getPropertyValue("duration");
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }

}
