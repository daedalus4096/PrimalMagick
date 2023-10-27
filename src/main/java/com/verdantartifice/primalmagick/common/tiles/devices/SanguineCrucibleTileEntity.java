package com.verdantartifice.primalmagick.common.tiles.devices;

import java.awt.Color;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.blocks.devices.SanguineCrucibleBlock;
import com.verdantartifice.primalmagick.common.items.misc.SanguineCoreItem;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Definition of a sanguine crucible tile entity.  Holds the crucible's core inventory and souls,
 * as well as handling the actual summoning of creatures.
 * 
 * @author Daedalus4096
 */
public class SanguineCrucibleTileEntity extends TileInventoryPM {
    protected static final int FLUID_CAPACITY = 1000;
    protected static final int FLUID_DRAIN = 200;
    protected static final int CHARGE_MAX = 100;
    protected static final int SPAWN_RANGE = 4;
    
    protected int souls;
    protected int fluidAmount;
    protected int charge;
    protected int counter = 0;
    
    public SanguineCrucibleTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.SANGUINE_CRUCIBLE.get(), pos, state, 1);
    }

    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the crucible's core stack for client-side use
        return ImmutableSet.of(Integer.valueOf(0));
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.souls = compound.getInt("Souls");
        this.fluidAmount = compound.getInt("FluidAmount");
        this.charge = compound.getInt("Charge");
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("Souls", this.souls);
        compound.putInt("FluidAmount", this.fluidAmount);
        compound.putInt("Charge", this.charge);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SanguineCrucibleTileEntity entity) {
        entity.counter++;
        if (entity.fluidAmount < FLUID_CAPACITY) {
            entity.fluidAmount++;
        }
        if (entity.hasCore() && entity.fluidAmount >= FLUID_DRAIN) {
            SanguineCoreItem core = entity.getCoreItem();
            if (core != null && entity.souls >= core.getSoulsPerSpawn()) {
                entity.charge++;
                if (entity.charge >= CHARGE_MAX) {
                    entity.charge = 0;
                    entity.fluidAmount -= FLUID_DRAIN;
                    entity.souls -= core.getSoulsPerSpawn();
                    
                    if (!level.isClientSide) {
                        if (!entity.getItem(0).isDamageableItem() || entity.getItem(0).hurt(1, level.random, null)) {
                            entity.getItem(0).shrink(1);
                            level.setBlock(pos, state.setValue(SanguineCrucibleBlock.LIT, false), Block.UPDATE_ALL_IMMEDIATE);
                        }
                        
                        int attempts = 0;
                        boolean success = false;
                        while (attempts++ < 50 && !success) {
                            success = entity.attemptSpawn(core.getEntityType());
                        }
                        
                        PacketHandler.sendToAllAround(new WandPoofPacket(pos.above(), Color.WHITE.getRGB(), true, Direction.UP), level.dimension(), pos, 32.0D);
                        
                        entity.setChanged();
                        entity.syncTile(true);
                    }
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
        BlockPos spawnPos = BlockPos.containing(x, y, z);
        
        if (this.level.noCollision(entityType.getAABB(x, y, z))) {
            ServerLevel serverWorld = (ServerLevel)this.level;
            Entity entity = entityType.spawn(serverWorld, (ItemStack)null, (Player)null, spawnPos, MobSpawnType.SPAWNER, true, !Objects.equals(this.worldPosition, spawnPos));
            if (entity == null) {
                return false;
            }
            entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), this.level.random.nextFloat() * 360.0F, 0.0F);
            
            if (entity instanceof Mob mobEntity) {
                ForgeEventFactory.onFinalizeSpawn(mobEntity, serverWorld, this.level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, null, null);
            }
            
            PacketHandler.sendToAllAround(new WandPoofPacket(x, y, z, Color.WHITE.getRGB(), true, Direction.UP), this.level.dimension(), entity.blockPosition(), 32.0D);
            return true;
        }
        
        return false;
    }

    @Override
    public ItemStack setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        ItemStack retVal = super.setItem(index, stack);
        if (index == 0 && (stack.isEmpty() || !ItemStack.isSameItemSameTags(stack, slotStack))) {
            this.charge = 0;
            this.setChanged();
            this.syncTile(false);
        }
        return retVal;
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
    
    public boolean showBubble(RandomSource rand) {
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
