package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition for an earth damage spell.  Does standard damage to the target and knocks it back.  The
 * magnitude of the knockback scales with the payload's power.
 * 
 * @author Daedalus4096
 */
public class EarthDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "earth_damage";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.BASIC_SORCERY.get().compoundKey();
    
    public EarthDamageSpellPayload() {
        super();
    }
    
    public EarthDamageSpellPayload(int power) {
        super(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    @Override
    public Source getSource() {
        return Source.EARTH;
    }
    
    @Override
    protected void applySecondaryEffects(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() != null && entityTarget.getEntity() instanceof LivingEntity) {
                Vec3 knockbackVec;
                if (entityTarget.getEntity().equals(caster)) {
                    // If for some reason the caster targets themselves, knock them directly backward
                    knockbackVec = caster.getLookAngle().scale(-1.0D).normalize();
                } else {
                    // If this was a Burst spell, knock targets away from the impact point; otherwise,
                    // knock them away from the caster
                    Vec3 knockbackSource = burstPoint == null || burstPoint.equals(target.getLocation()) ? caster.getEyePosition(1.0F) : burstPoint;
                    knockbackVec = target.getLocation().subtract(knockbackSource).scale(-1.0D).normalize();
                }
                ((LivingEntity)entityTarget.getEntity()).knockback(0.25F * this.getTotalDamage(entityTarget.getEntity(), spell, spellSource), knockbackVec.x, knockbackVec.z);
            }
        }
    }
    
    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.ROCKSLIDE.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }
    
    @Override
    public int getBaseManaCost() {
        int power = this.getPropertyValue("power");
        return (1 << Math.max(0, power - 1)) + ((1 << Math.max(0, power - 1)) >> 1);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getBaseDamage(spell, spellSource)));
    }
}
