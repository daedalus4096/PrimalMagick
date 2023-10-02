package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Map;

import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a healing spell.  Restores health to the target entity based on the power property of
 * the payload.  If the entity is undead, it instead takes damage based on power.  Has no effect on
 * blocks.
 * 
 * @author Daedalus4096
 */
public class HealingSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "healing";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_HEALING.get().compoundKey();

    public HealingSpellPayload() {
        super();
    }
    
    public HealingSpellPayload(int power) {
        super();
        this.getProperty("power").setValue(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("power", new SpellProperty("power", "spells.primalmagick.property.power", 1, 5));
        return propMap;
    }
    
    protected DamageSource getDamageSource(LivingEntity source, SpellPackage spell, Entity projectileEntity) {
        if (projectileEntity != null) {
            // If the spell was a projectile or a mine, then it's indirect now matter how it was deployed
            return DamageSourcesPM.sorcery(source.level(), this.getSource(), projectileEntity, source);
        } else if (spell.getVehicle().isIndirect()) {
            // If the spell vehicle is indirect but no projectile was given, then it's still indirect
            return DamageSourcesPM.sorcery(source.level(), this.getSource(), null, source);
        } else {
            // Otherwise, do direct damage
            return DamageSourcesPM.sorcery(source.level(), this.getSource(), source);
        }
    }

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() instanceof LivingEntity entity) {
                if (entity.isInvertedHealAndHarm()) {
                    // Undead entities get dealt damage
                    entity.hurt(this.getDamageSource(caster, spell, projectileEntity), 1.5F * this.getBaseAmount(spell, spellSource));
                } else {
                    // All other entities are healed
                    float curHealth = entity.getHealth();
                    float maxHealth = entity.getMaxHealth();
                    float healAmount = (float)this.getBaseAmount(spell, spellSource);
                    float overhealing = (curHealth + healAmount) - maxHealth;
                    entity.heal(healAmount);
                    if (overhealing > 0 && overhealing >= entity.getAbsorptionAmount()) {
                        // Grant a level of absorption for each four points of overhealing done
                        int level = (int)Math.floor(overhealing / 4.0F);
                        if (level > 0) {
                            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, level - 1));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Source getSource() {
        return Source.SUN;
    }

    @Override
    public int getBaseManaCost() {
        int power = this.getPropertyValue("power");
        return (1 << Math.max(0, power - 1)) + ((1 << Math.max(0, power - 1)) >> 1);
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.HEAL.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }

    protected int getBaseAmount(SpellPackage spell, ItemStack spellSource) {
        return 2 * this.getModdedPropertyValue("power", spell, spellSource);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getBaseAmount(spell, spellSource)));
    }
}
