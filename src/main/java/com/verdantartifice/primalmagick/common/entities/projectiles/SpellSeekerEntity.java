package com.verdantartifice.primalmagick.common.entities.projectiles;

import java.util.UUID;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a spell seeker entity.  Shot from a wand or scroll, they home in on a target entity,
 * like a shulker bullet, until they collide with a block or entity, at which point it executes a given
 * spell package upon the collider.
 * 
 * @author Daedalus4096
 */
public class SpellSeekerEntity extends Projectile {
    protected static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(SpellSeekerEntity.class, EntityDataSerializers.INT);
    
    protected final SpellPackage spell;
    protected final ItemStack spellSource;
    protected Entity target;
    protected UUID targetId;
    
    public SpellSeekerEntity(EntityType<? extends Projectile> type, Level worldIn) {
        super(type, worldIn);
        this.spell = null;
        this.spellSource = null;
        this.target = null;
        this.targetId = null;
    }

    public SpellSeekerEntity(Level world, LivingEntity thrower, @Nullable Entity target, SpellPackage spell, @Nullable ItemStack spellSource) {
        super(EntityTypesPM.SPELL_SEEKER.get(), world);
        this.spell = spell;
        this.spellSource = spellSource == null ? null : spellSource.copy();
        this.target = target;
        this.targetId = (target == null) ? null : target.getUUID();
        if (spell != null && spell.getPayload() != null) {
            // Store the spell payload's color for use in rendering
            this.setColor(spell.getPayload().getSource().getColor());
        }
    }
    
    @Nullable
    public SpellPackage getSpell() {
        return this.spell;
    }
    
    public int getColor() {
        return this.getEntityData().get(COLOR).intValue();
    }
    
    protected void setColor(int color) {
        this.getEntityData().set(COLOR, Integer.valueOf(color));
    }

    @Override
    protected void defineSynchedData() {
        // Nothing to define
    }

}
