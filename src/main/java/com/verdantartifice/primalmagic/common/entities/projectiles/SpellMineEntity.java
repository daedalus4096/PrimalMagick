package com.verdantartifice.primalmagic.common.entities.projectiles;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * Definition for a spell mine entity.  Sits in the world until another entity collides with it, at which point it executes a
 * given spell package upon the colliding entity.
 * 
 * @author Daedalus4096
 */
public class SpellMineEntity extends Entity {
    protected static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(SpellMineEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> ARMED = SynchedEntityData.defineId(SpellMineEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(SpellMineEntity.class, EntityDataSerializers.INT);
    
    protected static final int DURATION_FACTOR = 4800;  // Number of ticks to live per duration of the mine
    protected static final int ARMING_TIME = 60;        // Number of ticks before switching to an armed state
    
    protected SpellPackage spell;
    protected LivingEntity caster;
    protected UUID casterId;
    protected ItemStack spellSource;
    protected int currentLife = 0;
    
    public SpellMineEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.spell = null;
    }
    
    public SpellMineEntity(Level world, Vec3 pos, LivingEntity caster, SpellPackage spell, @Nullable ItemStack spellSource, int duration) {
        super(EntityTypesPM.SPELL_MINE.get(), world);
        this.setPos(pos.x, pos.y, pos.z);
        this.spell = spell;
        this.spellSource = spellSource.copy();
        this.caster = caster;
        this.casterId = caster.getUUID();
        this.setLifespan(duration * DURATION_FACTOR);
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
    
    public boolean isArmed() {
        return this.getEntityData().get(ARMED).booleanValue();
    }
    
    protected void setArmed(boolean armed) {
        this.getEntityData().set(ARMED, Boolean.valueOf(armed));
    }
    
    protected int getLifespan() {
        return this.getEntityData().get(LIFESPAN).intValue();
    }
    
    protected void setLifespan(int ticks) {
        this.getEntityData().set(LIFESPAN, Integer.valueOf(ticks));
    }
    
    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterId != null && this.level instanceof ServerLevel) {
            // If the caster cache is empty, find the entity matching the caster's unique ID
            Entity entity = ((ServerLevel)this.level).getEntity(this.casterId);
            if (entity != null && entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            } else {
                this.casterId = null;
            }
        }
        return this.caster;
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(COLOR, 0xFFFFFF);
        this.getEntityData().define(ARMED, Boolean.FALSE);
        this.getEntityData().define(LIFESPAN, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.caster = null;
        if (compound.contains("Caster", Constants.NBT.TAG_COMPOUND)) {
            this.casterId =  compound.getUUID("Caster");
        }
        
        this.spell = null;
        if (compound.contains("Spell", Constants.NBT.TAG_COMPOUND)) {
            this.spell = new SpellPackage(compound.getCompound("Spell"));
        }
        if (this.spell != null && !this.spell.isValid()) {
            this.spell = null;
        }
        if (this.spell != null && this.spell.getPayload() != null) {
            this.setColor(this.spell.getPayload().getSource().getColor());
        }
        
        this.spellSource = null;
        if (compound.contains("SpellSource", Constants.NBT.TAG_COMPOUND)) {
            this.spellSource = ItemStack.of(compound.getCompound("SpellSource"));
        }
        
        this.currentLife = compound.getInt("CurrentLife");
        this.setLifespan(compound.getInt("Lifespan"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.casterId != null) {
            compound.putUUID("Caster", this.casterId);
        }
        if (this.spell != null) {
            compound.put("Spell", this.spell.serializeNBT());
        }
        if (this.spellSource != null) {
            compound.put("SpellSource", this.spellSource.serializeNBT());
        }
        compound.putInt("CurrentLife", this.currentLife);
        compound.putInt("Lifespan", this.getLifespan());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && (this.spell == null || !this.spell.isValid())) {
            this.discard();
        }
        if (++this.currentLife > this.getLifespan()) {
            this.discard();
        }
        if (!this.level.isClientSide && this.isAlive()) {
            if (!this.isArmed() && this.currentLife >= ARMING_TIME) {
                this.setArmed(true);
            }
            if (this.isArmed() && this.currentLife % 5 == 0) {
                // Scan for living entities within a block
                AABB aabb = new AABB(this.position(), this.position()).inflate(1.0D);
                List<Entity> entityList = this.level.getEntities(this, aabb, e -> (e instanceof LivingEntity));
                boolean found = false;
                for (Entity entity : entityList) {
                    if (entity.isAlive()) {
                        // If found, execute the spell payload on them then remove self
                        if (this.spell != null && this.spell.getPayload() != null) {
                            this.spell.getPayload().playSounds(this.level, this.blockPosition());
                        }
                        if (this.getCaster() != null) {
                            SpellManager.executeSpellPayload(this.spell, new EntityHitResult(entity, this.position().add(0.0D, 0.5D, 0.0D)), this.level, this.getCaster(), this.spellSource, false);
                        }
                        found = true;
                    }
                }
                if (found) {
                    this.discard();
                }
            }
        }
    }
}
