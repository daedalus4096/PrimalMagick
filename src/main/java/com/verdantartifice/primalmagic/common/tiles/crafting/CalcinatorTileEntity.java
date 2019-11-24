package com.verdantartifice.primalmagic.common.tiles.crafting;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.blocks.crafting.CalcinatorBlock;
import com.verdantartifice.primalmagic.common.containers.CalcinatorContainer;
import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.ForgeEventFactory;

public class CalcinatorTileEntity extends TilePM implements ITickableTileEntity, INamedContainerProvider, IInventory {
    protected static final int OUTPUT_CAPACITY = 9;
    
    protected NonNullList<ItemStack> items = NonNullList.withSize(OUTPUT_CAPACITY + 2, ItemStack.EMPTY);
    protected int burnTime;
    protected int burnTimeTotal;
    protected int cookTime;
    protected int cookTimeTotal;
    
    protected final IIntArray calcinatorData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return CalcinatorTileEntity.this.burnTime;
            case 1:
                return CalcinatorTileEntity.this.burnTimeTotal;
            case 2:
                return CalcinatorTileEntity.this.cookTime;
            case 3:
                return CalcinatorTileEntity.this.cookTimeTotal;
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
            case 0:
                CalcinatorTileEntity.this.burnTime = value;
                break;
            case 1:
                CalcinatorTileEntity.this.burnTimeTotal = value;
                break;
            case 2:
                CalcinatorTileEntity.this.cookTime = value;
                break;
            case 3:
                CalcinatorTileEntity.this.cookTimeTotal = value;
                break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };
    
    public CalcinatorTileEntity() {
        super(TileEntityTypesPM.CALCINATOR);
    }
    
    protected boolean isBurning() {
        return this.burnTime > 0;
    }
    
    @Override
    protected void readFromTileNBT(CompoundNBT compound) {
        this.items = NonNullList.withSize(11, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.items);
        this.burnTime = compound.getInt("BurnTime");
        this.burnTimeTotal = getBurnTime(this.items.get(1));
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
    }
    
    @Override
    protected CompoundNBT writeToTileNBT(CompoundNBT compound) {
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    @Override
    public void tick() {
        boolean burningAtStart = this.isBurning();
        boolean shouldMarkDirty = false;
        
        if (burningAtStart) {
            this.burnTime--;
        }
        if (!this.world.isRemote) {
            ItemStack inputStack = this.items.get(0);
            ItemStack fuelStack = this.items.get(1);
            if (this.isBurning() || !fuelStack.isEmpty() && !inputStack.isEmpty()) {
                if (!this.isBurning() && this.canCalcinate(inputStack)) {
                    this.burnTime = getBurnTime(fuelStack);
                    this.burnTimeTotal = this.burnTime;
                    if (this.isBurning()) {
                        shouldMarkDirty = true;
                        if (fuelStack.hasContainerItem()) {
                            this.items.set(1, fuelStack.getContainerItem());
                        } else if (!fuelStack.isEmpty()) {
                            fuelStack.shrink(1);
                            if (fuelStack.isEmpty()) {
                                this.items.set(1, fuelStack.getContainerItem());
                            }
                        }
                    }
                }
                
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
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }
            
            if (burningAtStart != this.isBurning()) {
                shouldMarkDirty = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(CalcinatorBlock.LIT, Boolean.valueOf(this.isBurning())), 0x3);
            }
        }
        if (shouldMarkDirty) {
            this.markDirty();
        }
    }

    protected void doCalcination() {
        ItemStack inputStack = this.items.get(0);
        if (!inputStack.isEmpty() && this.canCalcinate(inputStack)) {
            List<ItemStack> currentOutputs = this.items.subList(2, this.items.size());
            List<ItemStack> newOutputs = this.getCalcinationOutput(inputStack, false);
            List<ItemStack> mergedOutputs = ItemUtils.mergeItemStackLists(currentOutputs, newOutputs);
            for (int index = 0; index < Math.min(mergedOutputs.size(), OUTPUT_CAPACITY); index++) {
                ItemStack out = mergedOutputs.get(index);
                this.items.set(index + 2, (out == null ? ItemStack.EMPTY : out));
            }
            inputStack.shrink(1);
        }
    }

    protected int getCookTimeTotal() {
        return 200;
    }

    protected static int getBurnTime(ItemStack fuelStack) {
        if (fuelStack.isEmpty()) {
            return 0;
        } else {
            Item item = fuelStack.getItem();
            int ret = fuelStack.getBurnTime();
            int burnTime = ret == -1 ? AbstractFurnaceTileEntity.getBurnTimes().getOrDefault(item, 0) : ret;
            return ForgeEventFactory.getItemBurnTime(fuelStack, burnTime);
        }
    }
    
    public static boolean isFuel(ItemStack stack) {
        return getBurnTime(stack) > 0;
    }

    protected boolean canCalcinate(ItemStack inputStack) {
        if (inputStack != null && !inputStack.isEmpty()) {
            SourceList sources = AffinityManager.getAffinities(inputStack, this.world);
            if (sources == null || sources.isEmpty()) {
                return false;
            } else {
                List<ItemStack> currentOutputs = this.items.subList(2, this.items.size());
                List<ItemStack> newOutputs = this.getCalcinationOutput(inputStack, true);
                List<ItemStack> mergedOutputs = ItemUtils.mergeItemStackLists(currentOutputs, newOutputs);
                return (mergedOutputs.size() <= OUTPUT_CAPACITY);
            }
        } else {
            return false;
        }
    }

    @Nonnull
    protected List<ItemStack> getCalcinationOutput(ItemStack inputStack, boolean alwaysGenerateDregs) {
        List<ItemStack> output = new ArrayList<>();
        SourceList sources = AffinityManager.getAffinities(inputStack, this.world);
        if (sources != null && !sources.isEmpty()) {
            for (Source source : Source.SORTED_SOURCES) {
                int amount = sources.getAmount(source);
                if (amount >= EssenceType.DUST.getAffinity()) {
                    int count = amount / EssenceType.DUST.getAffinity();
                    amount = amount % EssenceType.DUST.getAffinity();
                    ItemStack stack = EssenceItem.getEssence(EssenceType.DUST, source, count);
                    if (!stack.isEmpty()) {
                        output.add(stack);
                    }
                } else if (amount > 0 && (alwaysGenerateDregs || this.world.rand.nextInt(EssenceType.DUST.getAffinity()) < amount)) {
                    ItemStack stack = EssenceItem.getEssence(EssenceType.DUST, source);
                    if (!stack.isEmpty()) {
                        output.add(stack);
                    }
                }
            }
        }
        return output;
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
        return new CalcinatorContainer(windowId, playerInv, this, this.calcinatorData);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(stack, slotStack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if (index == 0 && !flag) {
            this.cookTimeTotal = this.getCookTimeTotal();
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }
}
