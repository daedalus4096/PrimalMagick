package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.menus.HoneyExtractorMenu;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Definition of a honey extractor tile entity.  Performs the extraction for the corresponding block.
 * 
 * @see com.verdantartifice.primalmagick.common.blocks.devices.HoneyExtractorBlock
 * @author Daedalus4096
 */
public abstract class HoneyExtractorTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider, IManaContainer {
    private static final Logger LOGGER = LogManager.getLogger();

    protected static final int INPUT_INV_INDEX = 0;
    protected static final int OUTPUT_INV_INDEX = 1;
    protected static final int WAND_INV_INDEX = 2;
    
    protected int spinTime;
    protected int spinTimeTotal;
    protected ManaStorage manaStorage;
    
    protected LazyOptional<IManaStorage<?>> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    
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
                return HoneyExtractorTileEntity.this.manaStorage.getManaStored(Sources.SKY);
            case 3:
                return HoneyExtractorTileEntity.this.manaStorage.getMaxManaStored(Sources.SKY);
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
        super(BlockEntityTypesPM.HONEY_EXTRACTOR.get(), pos, state);
        this.manaStorage = new ManaStorage(10000, 100, 100, Sources.SKY);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.spinTime = compound.getInt("SpinTime");
        this.spinTimeTotal = compound.getInt("SpinTimeTotal");
        ManaStorage.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), compound.get("ManaStorage")).resultOrPartial(msg -> {
            LOGGER.error("Failed to decode mana storage: {}", msg);
        }).ifPresent(mana -> mana.copyInto(this.manaStorage));
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("SpinTime", this.spinTime);
        compound.putInt("SpinTimeTotal", this.spinTimeTotal);
        ManaStorage.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), this.manaStorage).resultOrPartial(msg -> {
            LOGGER.error("Failed to encode mana storage: {}", msg);
        }).ifPresent(encoded -> compound.put("ManaStorage", encoded));
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

    public static void tick(Level level, BlockPos pos, BlockState state, HoneyExtractorTileEntity entity) {
        boolean shouldMarkDirty = false;

        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.getItem(WAND_INV_INDEX, 0);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
                IWand wand = (IWand)wandStack.getItem();
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Sources.SKY) - entity.manaStorage.getManaStored(Sources.SKY);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Sources.SKY, centimanaToTransfer, level.registryAccess())) {
                    entity.manaStorage.receiveMana(Sources.SKY, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }
            
            // Process ingredients
            ItemStack honeycombStack = entity.getItem(INPUT_INV_INDEX, 0);
            ItemStack bottleStack = entity.getItem(INPUT_INV_INDEX, 1);
            if (!honeycombStack.isEmpty() && !bottleStack.isEmpty() && entity.manaStorage.getManaStored(Sources.SKY) >= entity.getManaCost()) {
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
        if (!honeycombStack.isEmpty() && !bottleStack.isEmpty() && this.canSpin() && this.manaStorage.getManaStored(Sources.SKY) >= this.getManaCost()) {
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
            this.manaStorage.extractMana(Sources.SKY, this.getManaCost(), false);
        }
    }

    @Override
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        ItemStack slotStack = this.getItem(invIndex, slotIndex);
        super.setItem(invIndex, slotIndex, stack);
        if (invIndex == INPUT_INV_INDEX && (stack.isEmpty() || !ItemStack.isSameItemSameComponents(stack, slotStack))) {
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
        for (Source source : Sources.getAllSorted()) {
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
        return this.manaStorage.getMaxManaStored(Sources.SKY);
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
    protected Optional<Integer> getInventoryIndexForFace(Direction face) {
        return switch (face) {
            case UP -> Optional.of(INPUT_INV_INDEX);
            case DOWN -> Optional.of(OUTPUT_INV_INDEX);
            default -> Optional.of(WAND_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<IItemHandlerPM> createHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> switch (slot) {
                    case 0 -> stack.is(Items.HONEYCOMB);
                    case 1 -> stack.is(Items.GLASS_BOTTLE);
                    default -> false;
                }).build());
        
        // Create fuel handler
        retVal.set(WAND_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(WAND_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> stack.getItem() instanceof IWand)
                .build());

        // Create output handler
        retVal.set(OUTPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(OUTPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> false)
                .build());
        
        return retVal;
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput pComponentInput) {
        super.applyImplicitComponents(pComponentInput);
        pComponentInput.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).copyInto(this.manaStorage);
    }

    @Override
    protected void collectImplicitComponents(Builder pComponents) {
        super.collectImplicitComponents(pComponents);
        pComponents.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), this.manaStorage);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag pTag) {
        pTag.remove("ManaStorage");
    }
}
