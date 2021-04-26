package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

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
        return DamageSource.causeThrownDamage(target, source);
    }

    @Override
    public void execute(RayTraceResult target, Vector3d burstPoint, SpellPackage spell, World world, LivingEntity caster, ItemStack spellSource) {
        if (target != null && target.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityTarget = (EntityRayTraceResult)target;
            if (entityTarget.getEntity() != null) {
                // Damage the target entity
                entityTarget.getEntity().attackEntityFrom(this.getDamageSource(entityTarget.getEntity(), caster), this.getTotalDamage(entityTarget.getEntity(), spell, spellSource));
            }
        }
        
        // Apply any secondary effects from the payload
        this.applySecondaryEffects(target, burstPoint, spell, world, caster, spellSource);
    }
    
    protected void applySecondaryEffects(@Nullable RayTraceResult target, @Nullable Vector3d burstPoint, @Nonnull SpellPackage spell, @Nonnull World world, @Nonnull LivingEntity caster, @Nullable ItemStack spellSource) {
        // Do nothing by default
    }
}
