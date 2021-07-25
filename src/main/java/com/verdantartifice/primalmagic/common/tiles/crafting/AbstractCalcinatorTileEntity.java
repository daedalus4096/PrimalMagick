package com.verdantartifice.primalmagic.common.tiles.crafting;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.affinities.AffinityManager;
import com.verdantartifice.primalmagic.common.blocks.crafting.AbstractCalcinatorBlock;
import com.verdantartifice.primalmagic.common.containers.CalcinatorContainer;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.tiles.base.IOwnedTileEntity;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;

/**
 * Base definition of a calcinator tile entity.  Provides the melting functionality for the corresponding
 * block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.crafting.AbstractCalcinatorBlock}
 * @see {@link net.minecraft.tileentity.FurnaceTileEntity}
 */
public abstract class AbstractCalcinatorTileEntity extends TileInventoryPM implements TickableBlockEntity, MenuProvider, IOwnedTileEntity {
    protected static final int OUTPUT_CAPACITY = 9;
    
    protected int burnTime;
    protected int burnTimeTotal;
    protected int cookTime;
    protected int cookTimeTotal;
    protected Player owner;
    protected UUID ownerUUID;
    
    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData calcinatorData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return AbstractCalcinatorTileEntity.this.burnTime;
            case 1:
                return AbstractCalcinatorTileEntity.this.burnTimeTotal;
            case 2:
                return AbstractCalcinatorTileEntity.this.cookTime;
            case 3:
                return AbstractCalcinatorTileEntity.this.cookTimeTotal;
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
            case 0:
                AbstractCalcinatorTileEntity.this.burnTime = value;
                break;
            case 1:
                AbstractCalcinatorTileEntity.this.burnTimeTotal = value;
                break;
            case 2:
                AbstractCalcinatorTileEntity.this.cookTime = value;
                break;
            case 3:
                AbstractCalcinatorTileEntity.this.cookTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    
    public AbstractCalcinatorTileEntity(BlockEntityType<? extends AbstractCalcinatorTileEntity> tileEntityType) {
        super(tileEntityType, OUTPUT_CAPACITY + 2);
    }
    
    protected boolean isBurning() {
        return this.burnTime > 0;
    }
    
    @Override
    public void load(BlockState state, CompoundTag compound) {
        super.load(state, compound);
        
        this.burnTime = compound.getInt("BurnTime");
        this.burnTimeTotal = compound.getInt("BurnTimeTotal");
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        
        this.owner = null;
        this.ownerUUID = null;
        if (compound.contains("OwnerUUID")) {
            String ownerUUIDStr = compound.getString("OwnerUUID");
            if (!ownerUUIDStr.isEmpty()) {
                this.ownerUUID = UUID.fromString(ownerUUIDStr);
            }
        }
    }
    
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("BurnTimeTotal", this.burnTimeTotal);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        if (this.ownerUUID != null) {
            compound.putString("OwnerUUID", this.ownerUUID.toString());
        }
        return super.save(compound);
    }

    @Override
    public void tick() {
        boolean burningAtStart = this.isBurning();
        boolean shouldMarkDirty = false;
        
        if (burningAtStart) {
            this.burnTime--;
        }
        if (!this.level.isClientSide) {
            ItemStack inputStack = this.items.get(0);
            ItemStack fuelStack = this.items.get(1);
            if (this.isBurning() || !fuelStack.isEmpty() && !inputStack.isEmpty()) {
                // If the calcinator isn't burning, but has meltable input in place, light it up
                if (!this.isBurning() && this.canCalcinate(inputStack)) {
                    this.burnTime = ForgeHooks.getBurnTime(fuelStack);
                    this.burnTimeTotal = this.burnTime;
                    if (this.isBurning()) {
                        shouldMarkDirty = true;
                        if (fuelStack.hasContainerItem()) {
                            // If the fuel has a container item (e.g. a lava bucket), place the empty container in the fuel slot
                            this.items.set(1, fuelStack.getContainerItem());
                        } else if (!fuelStack.isEmpty()) {
                            // Otherwise, shrink the fuel stack
                            fuelStack.shrink(1);
                            if (fuelStack.isEmpty()) {
                                this.items.set(1, fuelStack.getContainerItem());
                            }
                        }
                    }
                }
                
                // If the calcinator is burning and has meltable input in place, process it
                if (this.isBurning() && this.canCalcinate(inputStack)) {
                    this.cookTime++;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTimeTotal();
                        this.doCalcination();
                        shouldMarkDirty = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                // Decay any cooking progress if the calcinator isn't lit
                this.cookTime = Mth.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }
            
            if (burningAtStart != this.isBurning()) {
                // Update the tile's block state if the calcinator was lit up or went out this tick
                shouldMarkDirty = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractCalcinatorBlock.LIT, Boolean.valueOf(this.isBurning())), Constants.BlockFlags.DEFAULT);
            }
        }
        if (shouldMarkDirty) {
            this.setChanged();
            this.syncTile(true);
        }
    }

    protected void doCalcination() {
        ItemStack inputStack = this.items.get(0);
        if (!inputStack.isEmpty() && this.canCalcinate(inputStack)) {
            // Merge the items already in the output inventory with the new output items from the melting
            List<ItemStack> currentOutputs = this.items.subList(2, this.items.size());
            List<ItemStack> newOutputs = this.getCalcinationOutput(inputStack, false);
            List<ItemStack> mergedOutputs = ItemUtils.mergeItemStackLists(currentOutputs, newOutputs);
            for (int index = 0; index < Math.min(mergedOutputs.size(), OUTPUT_CAPACITY); index++) {
                ItemStack out = mergedOutputs.get(index);
                this.items.set(index + 2, (out == null ? ItemStack.EMPTY : out));
            }
            
            // Shrink the input stack
            inputStack.shrink(1);
        }
    }

    protected abstract int getCookTimeTotal();

    public static boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack) > 0;
    }

    protected boolean canCalcinate(ItemStack inputStack) {
        if (inputStack != null && !inputStack.isEmpty()) {
            SourceList sources = AffinityManager.getInstance().getAffinityValues(inputStack, this.level);
            if (sources == null || sources.isEmpty()) {
                // An item without affinities cannot be melted
                return false;
            } else {
                // Merge the items already in the output inventory with the new output items from the melting
                List<ItemStack> currentOutputs = this.items.subList(2, this.items.size());
                List<ItemStack> newOutputs = this.getCalcinationOutput(inputStack, true);   // Force dreg generation to prevent random overflow
                List<ItemStack> mergedOutputs = ItemUtils.mergeItemStackLists(currentOutputs, newOutputs);
                return (mergedOutputs.size() <= OUTPUT_CAPACITY);
            }
        } else {
            return false;
        }
    }
    
    @Nonnull
    protected abstract List<ItemStack> getCalcinationOutput(ItemStack inputStack, boolean alwaysGenerateDregs);
    
    @Nonnull
    protected ItemStack getOutputEssence(EssenceType type, Source source, int count) {
        if (source.isDiscovered(this.getTileOwner())) {
            return EssenceItem.getEssence(type, source, count);
        } else {
            // If the calcinator's owner hasn't discovered the given source, only produce alchemical waste
            return new ItemStack(ItemsPM.ALCHEMICAL_WASTE.get(), count);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new CalcinatorContainer(windowId, playerInv, this, this.calcinatorData);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        boolean flag = !stack.isEmpty() && stack.sameItem(slotStack) && ItemStack.tagMatches(stack, slotStack);
        if (index == 0 && !flag) {
            this.cookTimeTotal = this.getCookTimeTotal();
            this.cookTime = 0;
            this.setChanged();
        }
    }

    @Override
    public void setTileOwner(Player owner) {
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
    }

    @Override
    public Player getTileOwner() {
        if (this.owner == null && this.ownerUUID != null && this.hasLevel() && this.level instanceof ServerLevel) {
            // If the owner cache is empty, find the entity matching the owner's unique ID
            ServerPlayer player = ((ServerLevel)this.level).getServer().getPlayerList().getPlayer(this.ownerUUID);
            if (player != null) {
                this.owner = player;
            } else {
                this.ownerUUID = null;
            }
        }
        return this.owner;
    }
}
