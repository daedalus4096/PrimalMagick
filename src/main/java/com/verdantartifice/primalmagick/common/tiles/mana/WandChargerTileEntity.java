package com.verdantartifice.primalmagick.common.tiles.mana;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.menus.WandChargerMenu;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a wand charger tile entity.  Provides the recharge and wand interaction functionality
 * for the corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.mana.WandChargerBlock}
 */
public class WandChargerTileEntity extends TileInventoryPM implements MenuProvider {
    protected static final int[] SLOTS_FOR_UP = new int[] { 0 };
    protected static final int[] SLOTS_FOR_DOWN = new int[0];
    protected static final int[] SLOTS_FOR_SIDES = new int[] { 1 };
    
    protected int chargeTime;
    protected int chargeTimeTotal;
    
    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData chargerData = new ContainerData() {
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
        public int getCount() {
            return 2;
        }
    };
    
    public WandChargerTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.WAND_CHARGER.get(), pos, state, 2);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the charger's wand input/output stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(1));
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.chargeTime = compound.getInt("ChargeTime");
        this.chargeTimeTotal = compound.getInt("ChargeTimeTotal");
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ChargeTime", this.chargeTime);
        compound.putInt("ChargeTimeTotal", this.chargeTimeTotal);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new WandChargerMenu(windowId, playerInv, this, this.chargerData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WandChargerTileEntity entity) {
        boolean shouldMarkDirty = false;
        
        if (!level.isClientSide) {
            ItemStack inputStack = entity.items.get(0);
            ItemStack chargeStack = entity.items.get(1);
            if (!inputStack.isEmpty() && !chargeStack.isEmpty()) {
                if (entity.canCharge()) {
                    // If there's an essence in the input slot and the slotted wand isn't full, do the charge
                    entity.chargeTime++;
                    if (entity.chargeTime >= entity.chargeTimeTotal) {
                        entity.chargeTime = 0;
                        entity.chargeTimeTotal = entity.getChargeTimeTotal();
                        entity.doCharge();
                        shouldMarkDirty = true;
                    }
                } else {
                    entity.chargeTime = 0;
                }
            } else if (entity.chargeTime > 0) {
                // Decay any charging progress if the charger isn't populated
                entity.chargeTime = Mth.clamp(entity.chargeTime - 2, 0, entity.chargeTimeTotal);
            }
        }
        
        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }
    
    protected int getChargeTimeTotal() {
        return 200;
    }
    
    protected boolean canCharge() {
        ItemStack inputStack = this.items.get(0);
        ItemStack chargeStack = this.items.get(1);
        if (inputStack != null && !inputStack.isEmpty() && inputStack.getItem() instanceof EssenceItem essence && chargeStack != null && !chargeStack.isEmpty()) {
            if (chargeStack.getItem() instanceof IWand wand) {
                // The wand can be charged if it and an essence are slotted, and the wand is not at max mana for the essence's source
                return (wand.getMana(chargeStack, essence.getSource()) < wand.getMaxMana(chargeStack));
            } else if (chargeStack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).isPresent()) {
                // The mana storage item can be charged if it and an essence are slotted, and the storage is not at max mana for the essence's source
                IManaStorage manaCap = chargeStack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).orElseThrow(IllegalArgumentException::new);
                return (manaCap.getManaStored(essence.getSource()) < manaCap.getMaxManaStored(essence.getSource()));
            }
        }
        return false;
    }
    
    protected void doCharge() {
        ItemStack inputStack = this.items.get(0);
        ItemStack chargeStack = this.items.get(1);
        if (this.canCharge()) {
            EssenceItem essence = (EssenceItem)inputStack.getItem();
            if (chargeStack.getItem() instanceof IWand wand) {
                wand.addRealMana(chargeStack, essence.getSource(), essence.getEssenceType().getManaEquivalent());
            } else {
                IManaStorage manaCap = chargeStack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).orElseThrow(IllegalArgumentException::new);
                manaCap.receiveMana(essence.getSource(), 100 * essence.getEssenceType().getManaEquivalent(), false);
                chargeStack.getOrCreateTag().put("LastUpdated", LongTag.valueOf(System.currentTimeMillis()));   // FIXME Is there a better way of marking this stack as dirty?
            }
            inputStack.shrink(1);
        }
    }
    
    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, slotStack);
        if (index == 0 && !flag) {
            this.chargeTimeTotal = this.getChargeTimeTotal();
            this.chargeTime = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
        if (slotIndex == 0) {
            return stack.getItem() instanceof EssenceItem;
        } else {
            return stack.getItem() instanceof IWand;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return SLOTS_FOR_UP;
        } else if (side == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }
}
