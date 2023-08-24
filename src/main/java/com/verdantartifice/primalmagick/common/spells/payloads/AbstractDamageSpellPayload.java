package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
        propMap.put("power", new SpellProperty("power", "spells.primalmagick.property.power", 1, 5));
        return propMap;
    }
    
    protected float getBaseDamage(SpellPackage spell, ItemStack spellSource) {
        return 4.0F + (3.0F * this.getModdedPropertyValue("power", spell, spellSource));
    }
    
    /**
     * Compute the total amount of damage to be done by this payload.
     * 
     * @param target the target entity being damaged
     * @param spell the spell package containing this payload
     * @param spellSource the wand or scroll containing the spell package
     * @return the total amount of damage to be done
     */
    protected float getTotalDamage(Entity target, SpellPackage spell, @Nullable ItemStack spellSource) {
        float damage = this.getBaseDamage(spell, spellSource);
        if (target instanceof Player) {
            // Spells do half damage against other players
            damage *= 0.5F;
        }
        return damage;
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
            if (entityTarget.getEntity() != null) {
                // Damage the target entity
                entityTarget.getEntity().hurt(this.getDamageSource(caster, spell, projectileEntity), this.getTotalDamage(entityTarget.getEntity(), spell, spellSource));
                
                // Update the caster's last hurt mob
                if (caster != null) {
                    caster.setLastHurtMob(entityTarget.getEntity());
                }
            }
        }
        
        // Apply any secondary effects from the payload
        this.applySecondaryEffects(target, burstPoint, spell, world, caster, spellSource);
    }
    
    protected void applySecondaryEffects(@Nullable HitResult target, @Nullable Vec3 burstPoint, @Nonnull SpellPackage spell, @Nonnull Level world, @Nonnull LivingEntity caster, @Nullable ItemStack spellSource) {
        // Do nothing by default
    }
}
