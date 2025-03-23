package com.verdantartifice.primalmagick.common.tiles.devices;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.blocks.devices.SanguineCrucibleBlock;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.items.misc.SanguineCoreItem;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Definition of a sanguine crucible tile entity.  Holds the crucible's core inventory and souls,
 * as well as handling the actual summoning of creatures.
 * 
 * @author Daedalus4096
 */
public abstract class SanguineCrucibleTileEntity extends AbstractTileSidedInventoryPM {
    protected static final int INPUT_INV_INDEX = 0;
    protected static final int FLUID_CAPACITY = 1000;
    protected static final int FLUID_DRAIN = 200;
    protected static final int CHARGE_MAX = 100;
    protected static final int SPAWN_RANGE = 4;
    
    protected int souls;
    protected int fluidAmount;
    protected int charge;
    protected int counter = 0;
    
    public SanguineCrucibleTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.SANGUINE_CRUCIBLE.get(), pos, state);
    }

    @Override
    protected Set<Integer> getSyncedSlotIndices(int inventoryIndex) {
        // Sync the crucible's core stack for client-side use
        return inventoryIndex == INPUT_INV_INDEX ? ImmutableSet.of(Integer.valueOf(0)) : ImmutableSet.of();
    }
    
    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.souls = compound.getInt("Souls");
        this.fluidAmount = compound.getInt("FluidAmount");
        this.charge = compound.getInt("Charge");
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
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
                    
                    if (level instanceof ServerLevel serverLevel) {
                        ItemStack coreStack = entity.getItem(INPUT_INV_INDEX, 0);
                        if (coreStack.isDamageableItem()) {
                            int newDamage = coreStack.getDamageValue() + 1;
                            if (newDamage >= coreStack.getMaxDamage()) {
                                coreStack.shrink(1);
                                entity.updateLitState();
                            } else {
                                coreStack.setDamageValue(newDamage);
                            }
                        } else {
                            coreStack.shrink(1);
                            entity.updateLitState();
                        }
                        
                        int attempts = 0;
                        boolean success = false;
                        while (attempts++ < 50 && !success) {
                            success = entity.attemptSpawn(core.getEntityType());
                        }
                        
                        PacketHandler.sendToAllAround(new WandPoofPacket(pos.above(), Color.WHITE.getRGB(), true, Direction.UP), serverLevel, pos, 32.0D);
                        
                        entity.setChanged();
                        entity.syncTile(true);
                    }
                }
            }
        }
    }
    
    protected void updateLitState() {
        if (this.hasLevel()) {
            this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SanguineCrucibleBlock.LIT, !this.getItem().isEmpty()), Block.UPDATE_ALL_IMMEDIATE);
        }
    }
    
    protected boolean attemptSpawn(EntityType<?> entityType) {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return false;
        }
        
        double x = (double)this.worldPosition.getX() + (SPAWN_RANGE * (this.level.random.nextDouble() - this.level.random.nextDouble())) + 0.5D;
        double y = (double)this.worldPosition.getY() + this.level.random.nextInt(3) - 1;
        double z = (double)this.worldPosition.getZ() + (SPAWN_RANGE * (this.level.random.nextDouble() - this.level.random.nextDouble())) + 0.5D;
        BlockPos spawnPos = BlockPos.containing(x, y, z);
        
        if (this.level.noCollision(entityType.getSpawnAABB(x, y, z))) {
            ServerLevel serverWorld = (ServerLevel)this.level;
            Entity entity = entityType.spawn(serverWorld, (ItemStack)null, (Player)null, spawnPos, MobSpawnType.SPAWNER, true, !Objects.equals(this.worldPosition, spawnPos));
            if (entity == null) {
                return false;
            }
            entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), this.level.random.nextFloat() * 360.0F, 0.0F);
            
            if (entity instanceof Mob mobEntity) {
                Services.EVENTS.finalizeMobSpawn(mobEntity, serverWorld, this.level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, null);
            }
            
            PacketHandler.sendToAllAround(new WandPoofPacket(x, y, z, Color.WHITE.getRGB(), true, Direction.UP), serverLevel, entity.blockPosition(), 32.0D);
            return true;
        }
        
        return false;
    }

    @Override
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        ItemStack slotStack = this.getItem(invIndex, slotIndex);
        super.setItem(invIndex, slotIndex, stack);
        if (invIndex == INPUT_INV_INDEX && (stack.isEmpty() || !ItemStack.isSameItemSameComponents(stack, slotStack))) {
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
        return !this.getItem(INPUT_INV_INDEX, 0).isEmpty();
    }
    
    public boolean showBubble(RandomSource rand) {
        return rand.nextBoolean() && this.counter % 5 == 0;
    }
    
    @Nullable
    protected SanguineCoreItem getCoreItem() {
        ItemStack stack = this.level.isClientSide ? this.getSyncedItem(INPUT_INV_INDEX, 0) : this.getItem(INPUT_INV_INDEX, 0);
        if (stack.getItem() instanceof SanguineCoreItem core) {
            return core;
        } else {
            return null;
        }
    }
    
    public ItemStack getItem() {
        return this.getItem(INPUT_INV_INDEX, 0);
    }
    
    public void setItem(ItemStack stack) {
        this.setItem(INPUT_INV_INDEX, 0, stack);
    }
    
    public ItemStack removeItem(int count) {
        ItemStack stack = this.itemHandlers.get(INPUT_INV_INDEX).extractItem(0, count, false);
        if (!stack.isEmpty()) {
            // Sync the inventory change to nearby clients
            this.syncSlots(null);
        }
        this.setChanged();
        return stack;
    }

    @Override
    protected int getInventoryCount() {
        return 1;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return inventoryIndex == INPUT_INV_INDEX ? 1 : 0;
    }

    @Override
    public Optional<Integer> getInventoryIndexForFace(@NotNull Direction face) {
        return switch (face) {
            case UP, DOWN -> Optional.empty();
            default -> Optional.of(INPUT_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<IItemHandlerPM> createHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create output handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .slotLimitFunction(slot -> 1)
                .itemValidFunction((slot, stack) -> stack.getItem() instanceof SanguineCoreItem)
                .contentsChangedFunction(slot -> SanguineCrucibleTileEntity.this.updateLitState())
                .build());

        return retVal;
    }
}
