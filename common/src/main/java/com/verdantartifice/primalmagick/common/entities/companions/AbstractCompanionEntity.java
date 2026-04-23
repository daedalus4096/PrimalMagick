package com.verdantartifice.primalmagick.common.entities.companions;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions.CompanionType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Base class for an entity that follows a player as a friendly companion, similar to a tamed creature.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractCompanionEntity extends PathfinderMob {
    protected static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER_REFERENCE =
            SynchedEntityData.defineId(AbstractCompanionEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

    protected boolean staying;

    protected AbstractCompanionEntity(EntityType<? extends AbstractCompanionEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(OWNER_REFERENCE, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("CompanionStaying", this.isCompanionStaying());
        if (this.getCompanionOwnerReference() != null) {
            this.getCompanionOwnerReference().store(output, "CompanionOwner");
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setCompanionStaying(input.getBooleanOr("CompanionStaying", false));
        this.setCompanionOwner(EntityReference.getLivingEntity(EntityReference.read(input, "CompanionOwner"), this.level()));
    }

    /**
     * Get the entity reference of this companion entity's owner, if any.
     * 
     * @return the entity reference of this companion entity's owner, if any
     */
    @Nullable
    private EntityReference<LivingEntity> getCompanionOwnerReference() {
        return this.entityData.get(OWNER_REFERENCE).orElse(null);
    }

    /**
     * Set the unique ID of this companion entity's owner.
     * 
     * @param entity the companion entity's new owner
     */
    public void setCompanionOwner(@Nullable LivingEntity entity) {
        this.entityData.set(OWNER_REFERENCE, Optional.ofNullable(EntityReference.of(entity)));
    }

    /**
     * Get this companion entity's owner entity.
     *
     * @return this companion entity's owner entity
     */
    @Nullable
    public Player getCompanionOwner() {
        return EntityReference.getLivingEntity(this.getCompanionOwnerReference(), this.level()) instanceof Player player ? player : null;
    }
    
    /**
     * Get whether this companion entity has an owner.
     * 
     * @return whether this companion entity has an owner
     */
    public boolean hasCompanionOwner() {
        return this.getCompanionOwnerReference() != null;
    }
    
    /**
     * Return whether the given player is this entity's companion owner.
     * 
     * @param entity the entity to test
     * @return whether the given player is this entity's companion owner
     */
    public boolean isCompanionOwner(LivingEntity entity) {
        return entity instanceof Player player && player == this.getCompanionOwner();
    }
    
    @Override
    public PlayerTeam getTeam() {
        if (this.hasCompanionOwner()) {
            LivingEntity owner = this.getCompanionOwner();
            if (owner != null) {
                return owner.getTeam();
            }
        }
        return super.getTeam();
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
        return switch (target) {
            case AbstractCompanionEntity otherCompanion ->
                    !otherCompanion.hasCompanionOwner() || otherCompanion.getCompanionOwner() != owner;
            case Player player when !owner.canHarmPlayer(player) -> false;
            case AbstractHorse abstractHorse when abstractHorse.isTamed() -> false;
            default -> !(target instanceof TamableAnimal) || !((TamableAnimal) target).isTame();
        };
    }

    /**
     * Get the type of companion represented by this entity.
     * 
     * @return this entity's companion type
     */
    public abstract CompanionType getCompanionType();

    @Override
    public void remove(@NotNull Entity.RemovalReason reason) {
        if (this.hasCompanionOwner()) {
            CompanionManager.removeCompanion(this.getCompanionOwner(), this);
        }
        super.remove(reason);
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        Level level = this.level();
        if (level instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(GameRules.SHOW_DEATH_MESSAGES) && this.getCompanionOwner() instanceof ServerPlayer player) {
            player.sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }
        super.die(cause);
    }

    @Override
    public void tick() {
        super.tick();
        
        // Kill this companion if it's no longer present on its owner's companion list
        Level level = this.level();
        if (!level.isClientSide() && this.tickCount % 100 == 0) {
            Player owner = this.getCompanionOwner();
            if (owner != null && !CompanionManager.isCurrentCompanion(owner, this)) {
                this.setCompanionOwner(null);
                if (level instanceof ServerLevel serverLevel) {
                    this.kill(serverLevel);
                }
            }
        }
    }
}
