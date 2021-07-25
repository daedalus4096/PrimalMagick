package com.verdantartifice.primalmagic.common.tiles.devices;

import java.awt.Color;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.common.blocks.devices.SanguineCrucibleBlock;
import com.verdantartifice.primalmagic.common.items.misc.SanguineCoreItem;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a sanguine crucible tile entity.  Holds the crucible's core inventory and souls,
 * as well as handling the actual summoning of creatures.
 * 
 * @author Daedalus4096
 */
public class SanguineCrucibleTileEntity extends TileInventoryPM implements TickableBlockEntity {
    protected static final int FLUID_CAPACITY = 1000;
    protected static final int FLUID_DRAIN = 200;
    protected static final int CHARGE_MAX = 100;
    protected static final int SPAWN_RANGE = 4;
    
    protected int souls;
    protected int fluidAmount;
    protected int charge;
    protected int counter = 0;
    
    public SanguineCrucibleTileEntity() {
        super(TileEntityTypesPM.SANGUINE_CRUCIBLE.get(), 1);
    }

    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the crucible's core stack for client-side use
        return ImmutableSet.of(Integer.valueOf(0));
    }
    
    @Override
    public void load(BlockState state, CompoundTag compound) {
        super.load(state, compound);
        this.souls = compound.getInt("Souls");
        this.fluidAmount = compound.getInt("FluidAmount");
        this.charge = compound.getInt("Charge");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("Souls", this.souls);
        compound.putInt("FluidAmount", this.fluidAmount);
        compound.putInt("Charge", this.charge);
        return super.save(compound);
    }

    @Override
    public void tick() {
        this.counter++;
        if (this.fluidAmount < FLUID_CAPACITY) {
            this.fluidAmount++;
        }
        if (this.hasCore() && this.fluidAmount >= FLUID_DRAIN) {
            SanguineCoreItem core = this.getCoreItem();
            if (core != null && this.souls >= core.getSoulsPerSpawn()) {
                this.charge++;
                if (this.charge >= CHARGE_MAX) {
                    this.charge = 0;
                    this.fluidAmount -= FLUID_DRAIN;
                    this.souls -= core.getSoulsPerSpawn();
                    
                    if (!this.level.isClientSide) {
                        if (!this.getItem(0).isDamageableItem() || this.getItem(0).hurt(1, this.level.random, null)) {
                            this.getItem(0).shrink(1);
                            this.level.setBlock(this.worldPosition, this.level.getBlockState(worldPosition).setValue(SanguineCrucibleBlock.LIT, false), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                        }
                        
                        int attempts = 0;
                        boolean success = false;
                        while (attempts++ < 50 && !success) {
                            success = this.attemptSpawn(core.getEntityType());
                        }
                        
                        PacketHandler.sendToAllAround(new WandPoofPacket(this.worldPosition.above(), Color.WHITE.getRGB(), true, Direction.UP), this.level.dimension(), this.worldPosition, 32.0D);
                    }
                    
                    this.setChanged();
                    this.syncTile(true);
                }
            }
        }
    }
    
    protected boolean attemptSpawn(EntityType<?> entityType) {
        if (this.level.isClientSide) {
            return false;
        }
        
        double x = (double)this.worldPosition.getX() + (SPAWN_RANGE * (this.level.random.nextDouble() - this.level.random.nextDouble())) + 0.5D;
        double y = (double)this.worldPosition.getY() + this.level.random.nextInt(3) - 1;
        double z = (double)this.worldPosition.getZ() + (SPAWN_RANGE * (this.level.random.nextDouble() - this.level.random.nextDouble())) + 0.5D;
        BlockPos spawnPos = new BlockPos(x, y, z);
        
        if (this.level.noCollision(entityType.getAABB(x, y, z))) {
            ServerLevel serverWorld = (ServerLevel)this.level;
            Entity entity = entityType.spawn(serverWorld, null, null, spawnPos, MobSpawnType.SPAWNER, true, !Objects.equals(this.worldPosition, spawnPos));
            if (entity == null) {
                return false;
            }
            entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), this.level.random.nextFloat() * 360.0F, 0.0F);
            
            if (entity instanceof Mob) {
                ((Mob)entity).finalizeSpawn(serverWorld, this.level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, null, null);
            }
            
            PacketHandler.sendToAllAround(new WandPoofPacket(x, y, z, Color.WHITE.getRGB(), true, Direction.UP), this.level.dimension(), entity.blockPosition(), 32.0D);
            return true;
        }
        
        return false;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        if (index == 0 && (stack.isEmpty() || !stack.sameItem(slotStack) || !ItemStack.tagMatches(stack, slotStack))) {
            this.charge = 0;
            this.setChanged();
            this.syncTile(false);
        }
    }
    
    public int getSouls() {
        return this.souls;
    }
    
    public void addSouls(int count) {
        this.souls += count;
        this.setChanged();
        this.syncTile(false);
    }
    
    public int getFluidAmount() {
        return this.fluidAmount;
    }
    
    public float getFluidHeight() {
        return 0.3F + (0.5F * ((float)this.getFluidAmount() / (float)FLUID_CAPACITY));
    }
    
    public int getCharge() {
        return this.charge;
    }
    
    public double getSmokeChance() {
        return (double)this.charge / (double)CHARGE_MAX;
    }
    
    public boolean hasCore() {
        return !this.items.get(0).isEmpty();
    }
    
    public boolean showBubble(Random rand) {
        return rand.nextBoolean() && this.counter % 5 == 0;
    }
    
    @Nullable
    protected SanguineCoreItem getCoreItem() {
        ItemStack stack = this.level.isClientSide ? this.getSyncedStackInSlot(0) : this.getItem(0);
        if (stack.getItem() instanceof SanguineCoreItem) {
            return ((SanguineCoreItem)stack.getItem());
        } else {
            return null;
        }
    }
}
