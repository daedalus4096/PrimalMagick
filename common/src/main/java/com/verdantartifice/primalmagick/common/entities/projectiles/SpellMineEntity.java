package com.verdantartifice.primalmagick.common.entities.projectiles;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

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
    
    protected static final int ARMING_TIME = 60;        // Number of ticks before switching to an armed state
    
    protected SpellPackage spell;
    protected EntityReference<LivingEntity> caster;
    protected ItemStack spellSource;
    protected int currentLife = 0;
    
    public SpellMineEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.spell = null;
    }
    
    public SpellMineEntity(Level world, Vec3 pos, LivingEntity caster, SpellPackage spell, @Nullable ItemStack spellSource, int durationMinutes) {
        super(EntityTypesPM.SPELL_MINE.get(), world);
        this.setPos(pos.x, pos.y, pos.z);
        this.spell = spell;
        this.spellSource = spellSource != null ? spellSource.copy() : null;
        this.caster = EntityReference.of(caster);
        this.setLifespan(20 * 60 * durationMinutes);
        if (spell != null && spell.payload() != null) {
            // Store the spell payload's color for use in rendering
            this.setColor(spell.payload().getComponent().getSource().getColor());
        }
    }
    
    @Nullable
    public SpellPackage getSpell() {
        return this.spell;
    }

    public int getColor() {
        return this.getEntityData().get(COLOR);
    }
    
    protected void setColor(int color) {
        this.getEntityData().set(COLOR, color);
    }
    
    public boolean isArmed() {
        return this.getEntityData().get(ARMED);
    }
    
    protected void setArmed(boolean armed) {
        this.getEntityData().set(ARMED, armed);
    }
    
    protected int getLifespan() {
        return this.getEntityData().get(LIFESPAN);
    }
    
    protected void setLifespan(int ticks) {
        this.getEntityData().set(LIFESPAN, ticks);
    }
    
    @Nullable
    public LivingEntity getCaster() {
        return EntityReference.getLivingEntity(this.caster, this.level());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(COLOR, 0xFFFFFF);
        pBuilder.define(ARMED, Boolean.FALSE);
        pBuilder.define(LIFESPAN, 0);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        this.caster = input.read("Caster", EntityReference.<LivingEntity>codec()).orElse(null);

        this.spell = input.read("Spell", SpellPackage.codec()).orElse(null);
        if (this.spell != null && !this.spell.isValid()) {
            this.spell = null;
        }
        if (this.spell != null && this.spell.payload() != null) {
            this.setColor(this.spell.payload().getComponent().getSource().getColor());
        }
        
        this.spellSource = input.read("SpellSource", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);

        this.currentLife = input.getIntOr("CurrentLife", 0);
        this.setLifespan(input.getIntOr("Lifespan", 0));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        output.store("Caster", EntityReference.codec(), this.caster);
        output.store("Spell", SpellPackage.codec(), this.spell);
        output.store("SpellSource", ItemStack.OPTIONAL_CODEC, this.spellSource);
        output.putInt("CurrentLife", this.currentLife);
        output.putInt("Lifespan", this.getLifespan());
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        if (!level.isClientSide() && (this.spell == null || !this.spell.isValid())) {
            this.discard();
        }
        if (++this.currentLife > this.getLifespan()) {
            this.discard();
        }
        if (!level.isClientSide() && this.isAlive()) {
            if (!this.isArmed() && this.currentLife >= ARMING_TIME) {
                this.setArmed(true);
            }
            if (this.isArmed() && this.currentLife % 5 == 0) {
                // Scan for living entities within a block
                AABB aabb = new AABB(this.position(), this.position()).inflate(1.0D);
                List<Entity> entityList = level.getEntities(this, aabb, e -> (e instanceof LivingEntity));
                boolean found = false;
                for (Entity entity : entityList) {
                    if (entity.isAlive()) {
                        // If found, execute the spell payload on them then remove self
                        if (this.spell != null && this.spell.payload() != null) {
                            this.spell.payload().getComponent().playSounds(level, this.blockPosition());
                        }
                        if (this.getCaster() != null) {
                            SpellManager.executeSpellPayload(this.spell, new EntityHitResult(entity, this.position().add(0.0D, 0.5D, 0.0D)), level, this.getCaster(), this.spellSource, false, this);
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

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float v) {
        return false;
    }
}
