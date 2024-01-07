package com.verdantartifice.primalmagick.common.entities.projectiles;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellTrailPacket;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Definition for a spell projectile entity.  Shot from a wand or scroll, they fly through the world until colliding with a
 * block or another entity, at which point it executes a given spell package upon the collider.
 * 
 * @author Daedalus4096
 */
public class SpellProjectileEntity extends AbstractArrow {
    protected static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(SpellProjectileEntity.class, EntityDataSerializers.INT);
    
    protected final SpellPackage spell;
    protected final ItemStack spellSource;
    
    public SpellProjectileEntity(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
        this.spell = null;
        this.spellSource = null;
    }
    
    public SpellProjectileEntity(Level world, LivingEntity thrower, SpellPackage spell, @Nullable ItemStack spellSource) {
        super(EntityTypesPM.SPELL_PROJECTILE.get(), thrower, world);
        this.spell = spell;
        this.spellSource = spellSource == null ? null : spellSource.copy();
        if (spell != null && spell.getPayload() != null) {
            // Store the spell payload's color for use in rendering
            this.setColor(spell.getPayload().getSource().getColor());
        }
    }
    
    public SpellProjectileEntity(Level world, double x, double y, double z, SpellPackage spell, @Nullable ItemStack spellSource) {
        super(EntityTypesPM.SPELL_PROJECTILE.get(), x, y, z, world);
        this.spell = spell;
        this.spellSource = spellSource == null ? null : spellSource.copy();
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
    public void tick() {
        super.tick();
        Level level = this.level();
        if (!level.isClientSide && this.isAlive() && this.tickCount % 2 == 0 && this.spell != null && this.spell.getPayload() != null) {
            // Leave a trail of particles in this entity's wake
            PacketHandler.sendToAllAround(
                    new SpellTrailPacket(this.position(), this.spell.getPayload().getSource().getColor()), 
                    level.dimension(), 
                    this.blockPosition(), 
                    64.0D);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        Level level = this.level();
        if (!level.isClientSide) {
            if (result.getType() == HitResult.Type.ENTITY && ((EntityHitResult)result).getEntity() instanceof SpellProjectileEntity) {
                // Don't collide with other spell projectiles
                return;
            }
            if (this.spell != null && this.spell.getPayload() != null) {
                LivingEntity shooter = (this.getOwner() instanceof LivingEntity) ? (LivingEntity)this.getOwner() : null;
                SpellManager.executeSpellPayload(this.spell, result, level, shooter, this.spellSource, true, this);
            }
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(COLOR, 0xFFFFFF);
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
