package com.verdantartifice.primalmagick.common.entities.companions;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions.CompanionType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;

/**
 * Base class for an entity that follows a player as a friendly companion, similar to a tamed creature.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractCompanionEntity extends PathfinderMob {
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(AbstractCompanionEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    protected boolean staying;

    protected AbstractCompanionEntity(EntityType<? extends AbstractCompanionEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("CompanionStaying", this.isCompanionStaying());
        if (this.hasCompanionOwner()) {
            compound.putUUID("CompanionOwner", this.getCompanionOwnerId());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCompanionStaying(compound.getBoolean("CompanionStaying"));
        if (compound.hasUUID("CompanionOwner")) {
            this.setCompanionOwnerId(compound.getUUID("CompanionOwner"));
        } else {
            this.setCompanionOwnerId(null);
        }
    }

    /**
     * Get the unique ID of this companion entity's owner, if any.
     * 
     * @return the unique ID of this companion entity's owner, if any
     */
    @Nullable
    public UUID getCompanionOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse(null);
    }

    /**
     * Set the unique ID of this companion entity's owner.
     * 
     * @param ownerId the unique ID of this companion entity's new owner
     */
    public void setCompanionOwnerId(@Nullable UUID ownerId) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(ownerId));
    }

    /**
     * Get this companion entity's owner entity.
     * 
     * @param world a valid entity reader
     * @return this companion entity's owner entity
     */
    @Nullable
    public Player getCompanionOwner() {
        try {
            UUID ownerId = this.getCompanionOwnerId();
            return ownerId == null ? null : this.level().getPlayerByUUID(ownerId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Get whether this companion entity has an owner.
     * 
     * @return whether this companion entity has an owner
     */
    public boolean hasCompanionOwner() {
        return this.getCompanionOwnerId() != null;
    }
    
    /**
     * Return whether the given player is this entity's companion owner.
     * 
     * @param entity the entity to test
     * @return whether the given player is this entity's companion owner
     */
    public boolean isCompanionOwner(LivingEntity entity) {
        return entity instanceof Player && ((Player)entity) == this.getCompanionOwner();
    }
    
    @Override
    public Team getTeam() {
        if (this.hasCompanionOwner()) {
            Player owner = this.getCompanionOwner();
            if (owner != null) {
                return owner.getTeam();
            }
        }
        return super.getTeam();
    }

    @Override
    public boolean isAlliedTo(Entity other) {
        if (this.hasCompanionOwner()) {
            Player owner = this.getCompanionOwner();
            if (other == owner) {
                return true;
            } else if (owner != null) {
                return owner.isAlliedTo(other);
            }
        }
        return super.isAlliedTo(other);
    }

    /**
     * Get whether this companion entity has been ordered to stay put.
     * 
     * @return whether this companion entity has been ordered to stay put
     */
    public boolean isCompanionStaying() {
        return this.staying;
    }

    /**
     * Set whether this companion entity has been ordered to stay put.
     * 
     * @param stay whether this companion entity should stay put
     */
    public void setCompanionStaying(boolean stay) {
        this.staying = stay;
    }
    
    /**
     * Get whether this companion entity should target the given target entity when a target goal triggers for it.
     * 
     * @param target the entity to be targeted
     * @param owner the companion's owner
     * @return whether this companion entity should target the given target entity
     */
    public boolean shouldAttackEntity(LivingEntity target, Player owner) {
        if (target instanceof AbstractCompanionEntity) {
            AbstractCompanionEntity otherCompanion = (AbstractCompanionEntity)target;
            return !otherCompanion.hasCompanionOwner() || otherCompanion.getCompanionOwner() != owner;
        } else if (target instanceof Player && !owner.canHarmPlayer((Player)target)) {
            return false;
        } else if (target instanceof AbstractHorse && ((AbstractHorse)target).isTamed()) {
            return false;
        } else {
            return !(target instanceof TamableAnimal) || !((TamableAnimal)target).isTame();
        }
    }

    /**
     * Get the type of companion represented by this entity.
     * 
     * @return this entity's companion type
     */
    public abstract CompanionType getCompanionType();

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (this.hasCompanionOwner()) {
            CompanionManager.removeCompanion(this.getCompanionOwner(), this);
        }
        super.remove(reason);
    }

    @Override
    public void die(DamageSource cause) {
        Level level = this.level();
        if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getCompanionOwner() instanceof ServerPlayer) {
            this.getCompanionOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }
        super.die(cause);
    }

    @Override
    public void tick() {
        super.tick();
        
        // Kill this companion if it's no longer present on its owner's companion list
        Level level = this.level();
        if (!level.isClientSide && this.tickCount % 100 == 0) {
            Player owner = this.getCompanionOwner();
            if (owner != null && !CompanionManager.isCurrentCompanion(owner, this)) {
                this.setCompanionOwnerId(null);
                this.kill();
            }
        }
    }
}
