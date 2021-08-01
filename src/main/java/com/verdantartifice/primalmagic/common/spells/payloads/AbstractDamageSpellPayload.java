package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Base class for a damaging spell.  Deals projectile damage to the target entity, scaling with the
 * payload's power property.  Frequently applies a secondary effect to the target, such as applying
 * a potion effect.  Has no effect on blocks.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractDamageSpellPayload extends AbstractSpellPayload {
    public AbstractDamageSpellPayload() {
        super();
    }
    
    public AbstractDamageSpellPayload(int power) {
        super();
        this.getProperty("power").setValue(power);
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("power", new SpellProperty("power", "primalmagic.spell.property.power", 1, 5));
        return propMap;
    }
    
    /**
     * Compute the total amount of damage to be done by this payload.
     * 
     * @param target the target entity being damaged
     * @param spell the spell package containing this payload
     * @param spellSource the wand or scroll containing the spell package
     * @return the total amount of damage to be done
     */
    protected abstract float getTotalDamage(Entity target, SpellPackage spell, @Nullable ItemStack spellSource);
    
    protected DamageSource getDamageSource(Entity target, LivingEntity source) {
        return DamageSource.thrown(target, source);
    }

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() != null) {
                // Damage the target entity
                entityTarget.getEntity().hurt(this.getDamageSource(entityTarget.getEntity(), caster), this.getTotalDamage(entityTarget.getEntity(), spell, spellSource));
            }
        }
        
        // Apply any secondary effects from the payload
        this.applySecondaryEffects(target, burstPoint, spell, world, caster, spellSource);
    }
    
    protected void applySecondaryEffects(@Nullable HitResult target, @Nullable Vec3 burstPoint, @Nonnull SpellPackage spell, @Nonnull Level world, @Nonnull LivingEntity caster, @Nullable ItemStack spellSource) {
        // Do nothing by default
    }
}
