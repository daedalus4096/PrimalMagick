package com.verdantartifice.primalmagic.common.entities.companions;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCompanions.CompanionType;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * Base class for an entity that follows a player as a friendly companion, similar to a tamed creature.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractCompanionEntity extends CreatureEntity {
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(AbstractCompanionEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    protected boolean staying;

    protected AbstractCompanionEntity(EntityType<? extends AbstractCompanionEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("CompanionStaying", this.isCompanionStaying());
        if (this.hasCompanionOwner()) {
            compound.putUniqueId("CompanionOwner", this.getCompanionOwnerId());
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setCompanionStaying(compound.getBoolean("CompanionStaying"));
        if (compound.hasUniqueId("CompanionOwner")) {
            this.setCompanionOwnerId(compound.getUniqueId("CompanionOwner"));
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
        return this.dataManager.get(OWNER_UNIQUE_ID).orElse(null);
    }

    /**
     * Set the unique ID of this companion entity's owner.
     * 
     * @param ownerId the unique ID of this companion entity's new owner
     */
    public void setCompanionOwnerId(@Nullable UUID ownerId) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(ownerId));
    }

    /**
     * Get this companion entity's owner entity.
     * 
     * @param world a valid entity reader
     * @return this companion entity's owner entity
     */
    @Nullable
    public PlayerEntity getCompanionOwner() {
        try {
            UUID ownerId = this.getCompanionOwnerId();
            return ownerId == null ? null : this.world.getPlayerByUuid(ownerId);
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
        return entity instanceof PlayerEntity && ((PlayerEntity)entity) == this.getCompanionOwner();
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
    public boolean shouldAttackEntity(LivingEntity target, PlayerEntity owner) {
        if (target instanceof AbstractCompanionEntity) {
            AbstractCompanionEntity otherCompanion = (AbstractCompanionEntity)target;
            return !otherCompanion.hasCompanionOwner() || otherCompanion.getCompanionOwner() != owner;
        } else if (target instanceof PlayerEntity && !owner.canAttackPlayer((PlayerEntity)target)) {
            return false;
        } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
            return false;
        } else {
            return !(target instanceof TameableEntity) || !((TameableEntity)target).isTamed();
        }
    }

    /**
     * Get the type of companion represented by this entity.
     * 
     * @return this entity's companion type
     */
    public abstract CompanionType getCompanionType();

    @Override
    public void remove() {
        if (this.hasCompanionOwner()) {
            CompanionManager.removeCompanion(this.getCompanionOwner(), this);
        }
        super.remove();
    }
}
