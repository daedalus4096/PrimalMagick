package com.verdantartifice.primalmagic.common.tiles.mana;

import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class WandChargerTileEntity extends TilePM implements ITickableTileEntity, INamedContainerProvider, IInventory {
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    protected int chargeTime;
    protected int chargeTimeTotal;
    
    protected final IIntArray chargerData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return WandChargerTileEntity.this.chargeTime;
            case 1:
                return WandChargerTileEntity.this.chargeTimeTotal;
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
            case 0:
                WandChargerTileEntity.this.chargeTime = value;
                break;
            case 1:
                WandChargerTileEntity.this.chargeTimeTotal = value;
                break;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };
    
    public WandChargerTileEntity() {
        super(TileEntityTypesPM.WAND_CHARGER);
    }
    
    @Override
    protected void readFromTileNBT(CompoundNBT compound) {
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.items);
        this.chargeTime = compound.getInt("ChargeTime");
        this.chargeTimeTotal = compound.getInt("ChargeTimeTotal");
    }
    
    @Override
    protected CompoundNBT writeToTileNBT(CompoundNBT compound) {
        compound.putInt("ChargeTime", this.chargeTime);
        compound.putInt("ChargeTimeTotal", this.chargeTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }

    @Override
    public void tick() {
        boolean shouldMarkDirty = false;

        if (!this.world.isRemote) {
            ItemStack inputStack = this.items.get(0);
            ItemStack wandStack = this.items.get(1);
            if (!inputStack.isEmpty() && !wandStack.isEmpty()) {
                if (this.canCharge()) {
                    this.chargeTime++;
                    if (this.chargeTime >= this.chargeTimeTotal) {
                        this.chargeTime = 0;
                        this.chargeTimeTotal = this.getChargeTimeTotal();
                        this.doCharge();
                        shouldMarkDirty = true;
                    }
                } else {
                    this.chargeTime = 0;
                }
            } else if (this.chargeTime > 0) {
                this.chargeTime = MathHelper.clamp(this.chargeTime - 2, 0, this.chargeTimeTotal);
            }
        }
        
        if (shouldMarkDirty) {
            this.markDirty();
        }
    }
    
    protected int getChargeTimeTotal() {
        return 200;
    }
    
    protected boolean canCharge() {
        ItemStack inputStack = this.items.get(0);
        ItemStack wandStack = this.items.get(1);
        if (inputStack != null && !inputStack.isEmpty() && inputStack.getItem() instanceof EssenceItem &&
            wandStack != null && !wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
            EssenceItem essence = (EssenceItem)inputStack.getItem();
            IWand wand = (IWand)wandStack.getItem();
            return (wand.getMana(wandStack, essence.getSource()) < wand.getMaxMana(wandStack));
        } else {
            return false;
        }
    }
    
    protected void doCharge() {
        ItemStack inputStack = this.items.get(0);
        ItemStack wandStack = this.items.get(1);
        if (this.canCharge()) {
            EssenceItem essence = (EssenceItem)inputStack.getItem();
            IWand wand = (IWand)wandStack.getItem();
            wand.addMana(wandStack, essence.getSource(), essence.getEssenceType().getAffinity());
            inputStack.shrink(1);
        }
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
            this.chargeTimeTotal = this.getChargeTimeTotal();
            this.chargeTime = 0;
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
