package com.verdantartifice.primalmagick.common.entities.misc;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.entities.FlyingCarpetItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.BlockUtil.FoundRectangle;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Definition of a flying carpet entity, a vehicle that can be used by players to soar through the sky.
 * 
 * @author Daedalus4096
 */
// TODO Should this inherit from VehicleEntity now?
public class FlyingCarpetEntity extends Entity {
    protected static final EntityDataAccessor<Integer> DYE_COLOR = SynchedEntityData.defineId(FlyingCarpetEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(FlyingCarpetEntity.class, EntityDataSerializers.FLOAT);

    private boolean forwardInputDown;
    private boolean backInputDown;

    public FlyingCarpetEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.blocksBuilding = true;
        this.setNoGravity(true);
    }
    
    public FlyingCarpetEntity(Level worldIn, double x, double y, double z) {
        this(EntityTypesPM.FLYING_CARPET.get(), worldIn);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DYE_COLOR, -1);
        pBuilder.define(DAMAGE, 0.0F);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        // Nothing to do
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        // Nothing to do
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return (entity.canBeCollidedWith(this) || entity.isPushable()) && !this.isPassengerOfSameVehicle(entity);
    }

    @Override
    public boolean canBeCollidedWith(Entity entity) {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    @NotNull
    public Vec3 getRelativePortalPosition(@NotNull Axis axis, @NotNull FoundRectangle result) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, result));
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    @Override
    @NotNull
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    @Override
    public void tick() {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        
        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }
        
        super.tick();
        if (this.getInterpolation() != null) {
            this.getInterpolation().interpolate();
        }

        Level level = this.level();
        if (this.isVehicle() && this.isLocalClientAuthoritative()) {
            this.updateMotion();
            if (level.isClientSide()) {
                this.controlCarpet();
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(Vec3.ZERO);
            if (level.isClientSide()) {
                this.updateInputs(false, false);
            }
        }
        
        this.applyEffectsFromBlocks();
    }

    private void updateMotion() {
        final float momentum = 0.9F;
        this.setDeltaMovement(this.getDeltaMovement().scale(momentum).add(0.0D, (this.isNoGravity() ? 0.0D : -0.04D), 0.0D));
    }
    
    private void controlCarpet() {
        if (this.isVehicle()) {
            Entity pilot = this.getControllingPassenger();
            if (pilot != null) {
                this.yRotO = this.getYRot();
                this.setYRot(pilot.getYRot());

                float f = 0.0F;
                if (this.forwardInputDown) {
                    f += 0.03F;
                }
                if (this.backInputDown) {
                    f -= 0.005F;
                }
                Vec3 newMotion = this.getDeltaMovement().add(
                        Mth.sin(-this.getYRot() * (float)(Math.PI / 180.0D)) * f,
                        Mth.sin(-pilot.getXRot() * (float)(Math.PI / 180.0D)) * f,
                        Mth.cos(this.getYRot() * (float)(Math.PI / 180.0D)) * f);
                this.setDeltaMovement(newMotion);
            }
        }
    }
    
    public void updateInputs(boolean forwardDown, boolean backwardDown) {
        this.forwardInputDown = forwardDown;
        this.backInputDown = backwardDown;
    }

    /**
     * Applies this carpet's yaw to the given entity. Used to update the orientation of its passenger.
     */
    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(entityToUpdate.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        entityToUpdate.yRotO += f1 - f;
        entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
        entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
    }

    @Override
    public void onPassengerTurned(@NotNull Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    @Override
    @NotNull
    public InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.level() instanceof ServerLevel serverLevel && this.isAlive()) {
            if (player.isSecondaryUseActive()) {
                this.spawnAtLocation(serverLevel, this.getDropItem(), 0.0F);
                this.discard();
            } else {
                player.startRiding(this);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource pSource, float pAmount) {
        if (this.isInvulnerableToBase(pSource)) {
            return false;
        } else if (!level.isClientSide() && !this.isRemoved()) {
            this.setDamage(this.getDamage() + pAmount * 10.0F);
            this.markHurt();
            this.gameEvent(GameEvent.ENTITY_DAMAGE, pSource.getEntity());
            boolean flag = pSource.getEntity() instanceof Player player && player.getAbilities().instabuild;
            if (flag || this.getDamage() > 40F) {
                if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
                    this.spawnAtLocation(level, this.getDropItem(), 0.0F);
                }
                this.discard();
            }
            return true;
        } else {
            return true;
        }
    }

    protected ItemStack getDropItem() {
        ItemStack stack = new ItemStack(ItemsPM.FLYING_CARPET.get());
        DyeColor color = this.getDyeColor();
        if (color != null) {
            ((FlyingCarpetItem)stack.getItem()).setDyeColor(stack, color);
        }
        return stack;
    }
    
    public DyeColor getDyeColor() {
        int value = this.entityData.get(DYE_COLOR);
        if (value == -1) {
            return null;
        } else {
            return DyeColor.byId(value);
        }
    }
    
    public void setDyeColor(DyeColor color) {
        if (color == null) {
            this.entityData.set(DYE_COLOR, -1);
        } else {
            this.entityData.set(DYE_COLOR, color.getId());
        }
    }
    
    public void setDamage(float damageTaken) {
        this.entityData.set(DAMAGE, damageTaken);
    }
    
    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
        // Shield the carpet and passenger from falling damage
        this.fallDistance = 0;
        if (this.isVehicle()) {
            Entity pilot = this.getControllingPassenger();
            if (pilot != null) {
                pilot.fallDistance = 0;
            }
        }
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof LivingEntity living) {
            return living;
        } else {
            return null;
        }
    }
}
