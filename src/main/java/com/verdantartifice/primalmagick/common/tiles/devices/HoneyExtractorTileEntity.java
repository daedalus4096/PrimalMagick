package com.verdantartifice.primalmagick.common.tiles.devices;

import java.util.OptionalInt;

import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.menus.HoneyExtractorMenu;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Definition of a honey extractor tile entity.  Performs the extraction for the corresponding block.
 * 
 * @see {@link com.verdantartifice.primalmagick.common.blocks.devices.HoneyExtractorBlock}
 * @author Daedalus4096
 */
public class HoneyExtractorTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider, IManaContainer {
    protected static final int INPUT_INV_INDEX = 0;
    protected static final int OUTPUT_INV_INDEX = 1;
    protected static final int WAND_INV_INDEX = 2;
    
    protected int spinTime;
    protected int spinTimeTotal;
    protected ManaStorage manaStorage;
    
    protected LazyOptional<IManaStorage> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    
    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData extractorData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return HoneyExtractorTileEntity.this.spinTime;
            case 1:
                return HoneyExtractorTileEntity.this.spinTimeTotal;
            case 2:
                return HoneyExtractorTileEntity.this.manaStorage.getManaStored(Source.SKY);
            case 3:
                return HoneyExtractorTileEntity.this.manaStorage.getMaxManaStored(Source.SKY);
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            // Don't set mana storage values
            switch (index) {
            case 0:
                HoneyExtractorTileEntity.this.spinTime = value;
                break;
            case 1:
                HoneyExtractorTileEntity.this.spinTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    
    public HoneyExtractorTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.HONEY_EXTRACTOR.get(), pos, state);
        this.manaStorage = new ManaStorage(10000, 100, 100, Source.SKY);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.spinTime = compound.getInt("SpinTime");
        this.spinTimeTotal = compound.getInt("SpinTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("SpinTime", this.spinTime);
        compound.putInt("SpinTimeTotal", this.spinTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new HoneyExtractorMenu(windowId, playerInv, this.getBlockPos(), this, this.extractorData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
    
    protected int getSpinTimeTotal() {
        return 100;
    }
    
    protected int getManaCost() {
        return 10;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.spinTimeTotal = this.getSpinTimeTotal();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HoneyExtractorTileEntity entity) {
        boolean shouldMarkDirty = false;

        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.getItem(WAND_INV_INDEX, 0);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
                IWand wand = (IWand)wandStack.getItem();
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Source.SKY) - entity.manaStorage.getManaStored(Source.SKY);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.SKY, centimanaToTransfer)) {
                    entity.manaStorage.receiveMana(Source.SKY, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }
            
            // Process ingredients
            ItemStack honeycombStack = entity.getItem(INPUT_INV_INDEX, 0);
            ItemStack bottleStack = entity.getItem(INPUT_INV_INDEX, 1);
            if (!honeycombStack.isEmpty() && !bottleStack.isEmpty() && entity.manaStorage.getManaStored(Source.SKY) >= entity.getManaCost()) {
                // If spinnable input is in place, process it
                if (entity.canSpin()) {
                    entity.spinTime++;
                    if (entity.spinTime == entity.spinTimeTotal) {
                        entity.spinTime = 0;
                        entity.spinTimeTotal = entity.getSpinTimeTotal();
                        entity.doExtraction();
                        shouldMarkDirty = true;
                    }
                } else {
                    entity.spinTime = 0;
                }
            } else if (entity.spinTime > 0) {
                // Decay any spin progress
                entity.spinTime = Mth.clamp(entity.spinTime - 2, 0, entity.spinTimeTotal);
            }
        }
        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }

    protected boolean canSpin() {
        ItemStack honeyOutput = this.getItem(OUTPUT_INV_INDEX, 0);
        ItemStack beeswaxOutput = this.getItem(OUTPUT_INV_INDEX, 1);
        return (honeyOutput.getCount() < this.itemHandlers.get(OUTPUT_INV_INDEX).getSlotLimit(0) &&
                honeyOutput.getCount() < honeyOutput.getMaxStackSize() &&
                beeswaxOutput.getCount() < this.itemHandlers.get(OUTPUT_INV_INDEX).getSlotLimit(1) &&
                beeswaxOutput.getCount() < beeswaxOutput.getMaxStackSize());
    }
    
    protected void doExtraction() {
        ItemStack honeycombStack = this.getItem(INPUT_INV_INDEX, 0);
        ItemStack bottleStack = this.getItem(INPUT_INV_INDEX, 1);
        if (!honeycombStack.isEmpty() && !bottleStack.isEmpty() && this.canSpin() && this.manaStorage.getManaStored(Source.SKY) >= this.getManaCost()) {
            ItemStack honeyStack = this.getItem(OUTPUT_INV_INDEX, 0);
            if (honeyStack.isEmpty()) {
                this.setItem(OUTPUT_INV_INDEX, 0, new ItemStack(Items.HONEY_BOTTLE));
            } else {
                honeyStack.grow(1);
            }
            
            ItemStack beeswaxStack = this.getItem(OUTPUT_INV_INDEX, 1);
            if (beeswaxStack.isEmpty()) {
                this.setItem(OUTPUT_INV_INDEX, 1, new ItemStack(ItemsPM.BEESWAX.get()));
            } else {
                beeswaxStack.grow(1);
            }
            
            honeycombStack.shrink(1);
            bottleStack.shrink(1);
            this.manaStorage.extractMana(Source.SKY, this.getManaCost(), false);
        }
    }

    @Override
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        ItemStack slotStack = this.getItem(invIndex, slotIndex);
        super.setItem(invIndex, slotIndex, stack);
        if (invIndex == INPUT_INV_INDEX && (stack.isEmpty() || !ItemStack.isSameItemSameTags(stack, slotStack))) {
            this.spinTimeTotal = this.getSpinTimeTotal();
            this.spinTime = 0;
            this.setChanged();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == PrimalMagickCapabilities.MANA_STORAGE) {
            return this.manaStorageOpt.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.manaStorageOpt.invalidate();
    }

    @Override
    public int getMana(Source source) {
        return this.manaStorage.getManaStored(source);
    }

    @Override
    public SourceList getAllMana() {
        SourceList.Builder mana = SourceList.builder();
        for (Source source : Source.SORTED_SOURCES) {
            int amount = this.manaStorage.getManaStored(source);
            if (amount > 0) {
                mana.with(source, amount);
            }
        }
        return mana.build();
    }

    @Override
    public int getMaxMana() {
        // TODO Fix up
        return this.manaStorage.getMaxManaStored(Source.SKY);
    }

    @Override
    public void setMana(Source source, int amount) {
        this.manaStorage.setMana(source, amount);
        this.setChanged();
        this.syncTile(true);
    }

    @Override
    public void setMana(SourceList mana) {
        this.manaStorage.setMana(mana);
        this.setChanged();
        this.syncTile(true);
    }

    @Override
    protected int getInventoryCount() {
        return 3;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX, OUTPUT_INV_INDEX -> 2;
            case WAND_INV_INDEX -> 1;
            default -> 0;
        };
    }

    @Override
    protected OptionalInt getInventoryIndexForFace(Direction face) {
        return switch (face) {
            case UP -> OptionalInt.of(INPUT_INV_INDEX);
            case DOWN -> OptionalInt.of(OUTPUT_INV_INDEX);
            default -> OptionalInt.of(WAND_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<ItemStackHandler> createHandlers() {
        NonNullList<ItemStackHandler> retVal = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandlerPM(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(INPUT_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0) {
                    return stack.is(Items.HONEYCOMB);
                } else if (slot == 1) {
                    return stack.is(Items.GLASS_BOTTLE);
                } else {
                    return false;
                }
            }
        });
        
        // Create fuel handler
        retVal.set(WAND_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(WAND_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem() instanceof IWand;
            }
        });

        // Create output handler
        retVal.set(OUTPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(OUTPUT_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return false;
            }
        });
        
        return retVal;
    }

    @Override
    protected void loadLegacyItems(NonNullList<ItemStack> legacyItems) {
        // Slots 0-1 were the input item stacks
        this.setItem(INPUT_INV_INDEX, 0, legacyItems.get(0));
        this.setItem(INPUT_INV_INDEX, 1, legacyItems.get(1));
        
        // Slots 2-3 were the output item stacks
        this.setItem(OUTPUT_INV_INDEX, 0, legacyItems.get(2));
        this.setItem(OUTPUT_INV_INDEX, 1, legacyItems.get(3));
        
        // Slot 4 was the wand item stack
        this.setItem(WAND_INV_INDEX, 0, legacyItems.get(4));
    }
}
