package com.verdantartifice.primalmagick.common.tiles.mana;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.menus.WandChargerMenu;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
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

import java.util.Optional;
import java.util.Set;

/**
 * Definition of a wand charger tile entity.  Provides the recharge and wand interaction functionality
 * for the corresponding block.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.mana.WandChargerBlock
 */
public class WandChargerTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider {
    public static final int INPUT_INV_INDEX = 0;
    public static final int CHARGE_INV_INDEX = 1;
    
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
        super(BlockEntityTypesPM.WAND_CHARGER.get(), pos, state);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices(int inventoryIndex) {
        // Sync the charger's wand input/output stack for client rendering use
        return inventoryIndex == CHARGE_INV_INDEX ? ImmutableSet.of(0) : ImmutableSet.of();
    }
    
    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.chargeTime = compound.getInt("ChargeTime");
        this.chargeTimeTotal = compound.getInt("ChargeTimeTotal");
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("ChargeTime", this.chargeTime);
        compound.putInt("ChargeTimeTotal", this.chargeTimeTotal);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new WandChargerMenu(windowId, playerInv, this.getBlockPos(), this, this.chargerData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WandChargerTileEntity entity) {
        boolean shouldMarkDirty = false;
        
        if (!level.isClientSide) {
            ItemStack inputStack = entity.getItem(INPUT_INV_INDEX, 0);
            ItemStack chargeStack = entity.getItem(CHARGE_INV_INDEX, 0);
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
        ItemStack inputStack = this.getItem(INPUT_INV_INDEX, 0);
        ItemStack chargeStack = this.getItem(CHARGE_INV_INDEX, 0);
        if (inputStack != null && !inputStack.isEmpty() && inputStack.getItem() instanceof EssenceItem essence && chargeStack != null && !chargeStack.isEmpty()) {
            if (chargeStack.getItem() instanceof IWand wand) {
                // The wand can be charged if it and an essence are slotted, and the wand is not at max mana for the essence's source
                return (wand.getMana(chargeStack, essence.getSource()) < wand.getMaxMana(chargeStack));
            } else if (chargeStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
                // The mana storage item can be charged if it and an essence are slotted, and the storage is not at max mana for the essence's source
                ManaStorage manaCap = chargeStack.get(DataComponentsPM.CAPABILITY_MANA_STORAGE.get());
                return (manaCap.getManaStored(essence.getSource()) < manaCap.getMaxManaStored(essence.getSource()));
            }
        }
        return false;
    }
    
    protected void doCharge() {
        ItemStack inputStack = this.getItem(INPUT_INV_INDEX, 0);
        ItemStack chargeStack = this.getItem(CHARGE_INV_INDEX, 0);
        if (this.canCharge()) {
            EssenceItem essence = (EssenceItem)inputStack.getItem();
            if (chargeStack.getItem() instanceof IWand wand) {
                wand.addRealMana(chargeStack, essence.getSource(), essence.getEssenceType().getManaEquivalent());
            } else if (chargeStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
                chargeStack.update(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY, manaCap -> {
                    manaCap.receiveMana(essence.getSource(), 100 * essence.getEssenceType().getManaEquivalent(), false);
                    return manaCap;
                });
                chargeStack.set(DataComponentsPM.LAST_UPDATED.get(), System.currentTimeMillis());   // FIXME Is there a better way of marking this stack as dirty?
            }
            inputStack.shrink(1);
        }
    }
    
    @Override
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        ItemStack slotStack = this.getItem(invIndex, slotIndex);
        super.setItem(invIndex, slotIndex, stack);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, slotStack);
        if (invIndex == INPUT_INV_INDEX && !flag) {
            this.chargeTimeTotal = this.getChargeTimeTotal();
            this.chargeTime = 0;
            this.setChanged();
        }
    }

    @Override
    protected int getInventoryCount() {
        return 2;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX, CHARGE_INV_INDEX -> 1;
            default -> 0;
        };
    }

    @Override
    public Optional<Integer> getInventoryIndexForFace(Direction face) {
        return switch (face) {
            case UP -> Optional.of(INPUT_INV_INDEX);
            case DOWN -> Optional.empty();
            default -> Optional.of(CHARGE_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<IItemHandlerPM> createHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> stack.getItem() instanceof EssenceItem)
                .build());
        
        // Create charge handler
        retVal.set(CHARGE_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(CHARGE_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> (stack.getItem() instanceof IWand) || stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()))
                .build());

        return retVal;
    }
}
