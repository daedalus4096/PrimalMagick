package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Map;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a frost damage spell.  Does standard damage to the target and applies a slowness
 * potion effect.  The strength of the slowness effect scales with the payload's power property and
 * its length scales with the duration property.
 * 
 * @author Daedalus4096
 */
public class FrostDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "frost_damage";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_FROST.get().compoundKey();

    public FrostDamageSpellPayload() {
        super();
    }
    
    public FrostDamageSpellPayload(int power, int duration) {
        super(power);
        this.getProperty("duration").setValue(duration);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("duration", new SpellProperty("duration", "spells.primalmagick.property.duration", 0, 5));
        return propMap;
    }

    @Override
    public Source getSource() {
        return Source.SEA;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.ICE.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }

    @Override
    protected float getTotalDamage(Entity target, SpellPackage spell, ItemStack spellSource) {
        float retVal = super.getTotalDamage(target, spell, spellSource);
        if (target.getType() == EntityType.ENDERMAN) {
            // Endermen are hurt by water
            retVal *= 1.5F;
        }
        return retVal;
    }

    @Override
    protected void applySecondaryEffects(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        int duration = this.getDurationSeconds(spell, spellSource);
        if (target != null && target.getType() == HitResult.Type.ENTITY && duration > 0) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() != null && entityTarget.getEntity() instanceof LivingEntity) {
                int potency = (int)((1.0F + this.getModdedPropertyValue("power", spell, spellSource)) / 3.0F);   // 0, 1, 1, 1, 2
                ((LivingEntity)entityTarget.getEntity()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * duration, potency));
            }
        }
    }
    
    @Override
    public int getBaseManaCost() {
        int power = this.getPropertyValue("power");
        int duration = this.getPropertyValue("duration");
        return (1 << Math.max(0, power - 1)) + (duration == 0 ? 0 : (1 << Math.max(0, duration - 1)) >> 1);
    }

    protected int getDurationSeconds(SpellPackage spell, ItemStack spellSource) {
        return 2 * this.getModdedPropertyValue("duration", spell, spellSource);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getBaseDamage(spell, spellSource)),
                DECIMAL_FORMATTER.format(this.getDurationSeconds(spell, spellSource)));
    }
}
