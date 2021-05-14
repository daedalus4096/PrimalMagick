package com.verdantartifice.primalmagic.common.entities.misc;

import java.util.List;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.entities.FlyingCarpetItem;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.TeleportationRepositioner.Result;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Definition of a flying carpet entity, a vehicle that can be used by players to soar through the sky.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetEntity extends Entity {
    protected static final DataParameter<Integer> DYE_COLOR = EntityDataManager.createKey(FlyingCarpetEntity.class, DataSerializers.VARINT);

    private float momentum;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;
    private boolean forwardInputDown;
    private boolean backInputDown;

    public FlyingCarpetEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.preventEntitySpawning = true;
        this.setNoGravity(true);
    }
    
    public FlyingCarpetEntity(World worldIn, double x, double y, double z) {
        this(EntityTypesPM.FLYING_CARPET.get(), worldIn);
        this.setPosition(x, y, z);
        this.setMotion(Vector3d.ZERO);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }
    
    public FlyingCarpetEntity(World worldIn, BlockPos pos) {
        this(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

    @Override
    protected void registerData() {
        this.dataManager.register(DYE_COLOR, -1);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        // Nothing to do
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        // Nothing to do
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canCollide(Entity entity) {
        return (entity.func_241845_aY() || entity.canBePushed()) && !this.isRidingSameEntity(entity);
    }

    @Override
    public boolean func_241845_aY() {
        return true;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    protected Vector3d func_241839_a(Axis axis, Result result) {
        return LivingEntity.func_242288_h(super.func_241839_a(axis, result));
    }

    @Override
    public double getMountedYOffset() {
        return -0.1D;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = (double)yaw;
        this.lerpPitch = (double)pitch;
        this.lerpSteps = 10;
    }

    @Override
    public Direction getAdjustedHorizontalFacing() {
        return this.getHorizontalFacing().rotateY();
    }

    @Override
    public void tick() {
        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();
        super.tick();
        this.tickLerp();
        
        if (this.isBeingRidden() && this.canPassengerSteer()) {
            this.updateMotion();
            if (this.world.isRemote) {
                this.controlCarpet();
            }
            this.move(MoverType.SELF, this.getMotion());
        } else {
            this.setMotion(Vector3d.ZERO);
            if (this.world.isRemote) {
                this.updateInputs(false, false);
            }
        }
        
        this.doBlockCollisions();
    }

    private void tickLerp() {
        if (this.canPassengerSteer()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getPosX(), this.getPosY(), this.getPosZ());
        }
        if (this.lerpSteps > 0) {
            double newX = this.getPosX() + ((this.lerpX - this.getPosX()) / (double)this.lerpSteps);
            double newY = this.getPosY() + ((this.lerpY - this.getPosY()) / (double)this.lerpSteps);
            double newZ = this.getPosZ() + ((this.lerpZ - this.getPosZ()) / (double)this.lerpSteps);
            double deltaYaw = MathHelper.wrapDegrees(this.lerpYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + (deltaYaw / (double)this.lerpSteps));
            this.rotationPitch = (float)((double)this.rotationPitch + ((this.lerpPitch - (double)this.rotationPitch) / (double)this.lerpSteps));
            this.lerpSteps--;
            this.setPosition(newX, newY, newZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }
    
    private void updateMotion() {
        this.momentum = 0.9F;
        this.setMotion(this.getMotion().scale(this.momentum).add(0.0D, (this.hasNoGravity() ? 0.0D : -0.04D), 0.0D));
    }
    
    private void controlCarpet() {
        if (this.isBeingRidden()) {
            Entity pilot = this.getControllingPassenger();
            this.prevRotationYaw = this.rotationYaw;
            this.rotationYaw = pilot.rotationYaw;
            
            float f = 0.0F;
            if (this.forwardInputDown) {
                f += 0.03F;
            }
            if (this.backInputDown) {
                f -= 0.005F;
            }
            Vector3d newMotion = this.getMotion().add(
                    (double)(MathHelper.sin(-this.rotationYaw * (float)(Math.PI / 180.0D)) * f), 
                    (double)(MathHelper.sin(-pilot.rotationPitch * (float)(Math.PI / 180.0D)) * f), 
                    (double)(MathHelper.cos(this.rotationYaw * (float)(Math.PI / 180.0D)) * f));
            this.setMotion(newMotion);
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
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void applyOrientationToEntity(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (!this.world.isRemote && this.isAlive()) {
            if (player.isSecondaryUseActive()) {
                ItemStack stack = new ItemStack(ItemsPM.FLYING_CARPET.get());
                DyeColor color = this.getDyeColor();
                if (color != null) {
                    ((FlyingCarpetItem)stack.getItem()).setDyeColor(stack, color);
                }
                this.entityDropItem(stack, 0.0F);
                this.remove();
                return ActionResultType.SUCCESS;
            } else {
                player.startRiding(this);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
    
    public DyeColor getDyeColor() {
        int value = this.dataManager.get(DYE_COLOR).intValue();
        if (value == -1) {
            return null;
        } else {
            return DyeColor.byId(value);
        }
    }
    
    public void setDyeColor(DyeColor color) {
        if (color == null) {
            this.dataManager.set(DYE_COLOR, Integer.valueOf(-1));
        } else {
            this.dataManager.set(DYE_COLOR, Integer.valueOf(color.getId()));
        }
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        // Shield the carpet and passenger from falling damage
        this.fallDistance = 0;
        if (this.isBeingRidden()) {
            this.getControllingPassenger().fallDistance = 0;
        }
    }

    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.canPassengerSteer() && this.lerpSteps > 0) {
            this.lerpSteps = 0;
            this.setPositionAndRotation(this.lerpX, this.lerpY, this.lerpZ, (float)this.lerpYaw, (float)this.lerpPitch);
        }
    }
}
