package com.verdantartifice.primalmagic.common.tiles.mana;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.common.containers.WandChargerContainer;
import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class WandChargerTileEntity extends TileInventoryPM implements ITickableTileEntity, INamedContainerProvider {
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
        super(TileEntityTypesPM.WAND_CHARGER, 2);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        return ImmutableSet.of(Integer.valueOf(1));
    }
    
    @Override
    protected void readFromTileNBT(CompoundNBT compound) {
        this.chargeTime = compound.getInt("ChargeTime");
        this.chargeTimeTotal = compound.getInt("ChargeTimeTotal");
    }
    
    @Override
    protected CompoundNBT writeToTileNBT(CompoundNBT compound) {
        compound.putInt("ChargeTime", this.chargeTime);
        compound.putInt("ChargeTimeTotal", this.chargeTimeTotal);
        return compound;
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
        return new WandChargerContainer(windowId, playerInv, this, this.chargerData);
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
            this.syncTile(false);
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
            wand.addMana(wandStack, essence.getSource(), this.getManaForEssenceType(essence.getEssenceType()));
            inputStack.shrink(1);
        }
    }
    
    protected int getManaForEssenceType(EssenceType type) {
        switch (type) {
        case DUST:
            return 1;
        case SHARD:
            return 10;
        case CRYSTAL:
            return 100;
        case CLUSTER:
            return 1000;
        default:
            return 0;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
        ItemStack slotStack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(stack, slotStack);
        if (index == 0 && !flag) {
            this.chargeTimeTotal = this.getChargeTimeTotal();
            this.chargeTime = 0;
            this.markDirty();
        }
    }
}
