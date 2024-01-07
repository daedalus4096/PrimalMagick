package com.verdantartifice.primalmagick.common.tiles.devices;

import java.util.OptionalInt;

import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.menus.DissolutionChamberMenu;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Definition of a dissolution chamber tile entity.  Performs the processing for the corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.devices.DissolutionChamberBlock}
 */
public class DissolutionChamberTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider, IManaContainer, StackedContentsCompatible {
    protected static final int OUTPUT_INV_INDEX = 0;
    protected static final int INPUT_INV_INDEX = 1;
    protected static final int WAND_INV_INDEX = 2;
    
    protected int processTime;
    protected int processTimeTotal;
    protected ManaStorage manaStorage;
    protected LazyOptional<IManaStorage> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);

    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData chamberData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return DissolutionChamberTileEntity.this.processTime;
            case 1:
                return DissolutionChamberTileEntity.this.processTimeTotal;
            case 2:
                return DissolutionChamberTileEntity.this.manaStorage.getManaStored(Source.EARTH);
            case 3:
                return DissolutionChamberTileEntity.this.manaStorage.getMaxManaStored(Source.EARTH);
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            // Don't set mana storage values
            switch (index) {
            case 0:
                DissolutionChamberTileEntity.this.processTime = value;
                break;
            case 1:
                DissolutionChamberTileEntity.this.processTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    
    public DissolutionChamberTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.DISSOLUTION_CHAMBER.get(), pos, state);
        this.manaStorage = new ManaStorage(25600, 100, 100, Source.EARTH);
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.processTime = compound.getInt("ProcessTime");
        this.processTimeTotal = compound.getInt("ProcessTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ProcessTime", this.processTime);
        compound.putInt("ProcessTimeTotal", this.processTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new DissolutionChamberMenu(windowId, playerInv, this.getBlockPos(), this, this.chamberData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    protected int getProcessTimeTotal() {
        return 100;
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        this.processTimeTotal = this.getProcessTimeTotal();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DissolutionChamberTileEntity entity) {
        boolean shouldMarkDirty = false;
        
        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.getItem(WAND_INV_INDEX, 0);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Source.EARTH) - entity.manaStorage.getManaStored(Source.EARTH);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.EARTH, centimanaToTransfer)) {
                    entity.manaStorage.receiveMana(Source.EARTH, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }

            SimpleContainer testInv = new SimpleContainer(entity.getItem(INPUT_INV_INDEX, 0));
            RecipeHolder<IDissolutionRecipe> recipe = level.getServer().getRecipeManager().getRecipeFor(RecipeTypesPM.DISSOLUTION.get(), testInv, level).orElse(null);
            if (entity.canDissolve(testInv, level.registryAccess(), recipe)) {
                entity.processTime++;
                if (entity.processTime >= entity.processTimeTotal) {
                    entity.processTime = 0;
                    entity.processTimeTotal = entity.getProcessTimeTotal();
                    entity.doDissolve(testInv, level.registryAccess(), recipe);
                    shouldMarkDirty = true;
                }
            } else {
                entity.processTime = Mth.clamp(entity.processTime - 2, 0, entity.processTimeTotal);
            }
        }

        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }
    
    protected boolean canDissolve(Container inputInv, RegistryAccess registryAccess, RecipeHolder<IDissolutionRecipe> recipe) {
        if (!inputInv.isEmpty() && recipe != null) {
            ItemStack output = recipe.value().getResultItem(registryAccess);
            if (output.isEmpty()) {
                return false;
            } else if (this.getMana(Source.EARTH) < (100 * recipe.value().getManaCosts().getAmount(Source.EARTH))) {
                return false;
            } else {
                ItemStack currentOutput = this.getItem(OUTPUT_INV_INDEX, 0);
                if (currentOutput.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(currentOutput, output)) {
                    return false;
                } else if (currentOutput.getCount() + output.getCount() <= this.itemHandlers.get(OUTPUT_INV_INDEX).getSlotLimit(0) && 
                        currentOutput.getCount() + output.getCount() <= currentOutput.getMaxStackSize()) {
                    return true;
                } else {
                    return currentOutput.getCount() + output.getCount() <= output.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }
    
    protected void doDissolve(Container inputInv, RegistryAccess registryAccess, RecipeHolder<IDissolutionRecipe> recipe) {
        if (recipe != null && this.canDissolve(inputInv, registryAccess, recipe)) {
            ItemStack recipeOutput = recipe.value().assemble(inputInv, registryAccess);
            ItemStack currentOutput = this.getItem(OUTPUT_INV_INDEX, 0);
            if (currentOutput.isEmpty()) {
                this.setItem(OUTPUT_INV_INDEX, 0, recipeOutput);
            } else if (ItemStack.isSameItemSameTags(recipeOutput, currentOutput)) {
                currentOutput.grow(recipeOutput.getCount());
            }
            
            for (int index = 0; index < inputInv.getContainerSize(); index++) {
                ItemStack stack = inputInv.getItem(index);
                if (!stack.isEmpty()) {
                    stack.shrink(1);
                }
            }
            this.setMana(Source.EARTH, this.getMana(Source.EARTH) - (100 * recipe.value().getManaCosts().getAmount(Source.EARTH)));
        }
    }
    
    @Override
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        ItemStack slotStack = this.getItem(invIndex, slotIndex);
        super.setItem(invIndex, slotIndex, stack);
        if (invIndex == INPUT_INV_INDEX && (stack.isEmpty() || !ItemStack.isSameItemSameTags(stack, slotStack))) {
            this.processTimeTotal = this.getProcessTimeTotal();
            this.processTime = 0;
            this.setChanged();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == PrimalMagickCapabilities.MANA_STORAGE) {
            return this.manaStorageOpt.cast();
        } else {
            return super.getCapability(cap, side);
        }
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
        return this.manaStorage.getMaxManaStored(Source.EARTH);
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
    public void fillStackedContents(StackedContents stackedContents) {
        for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
            for (int slotIndex = 0; slotIndex < this.getInventorySize(invIndex); slotIndex++) {
                stackedContents.accountStack(this.getItem(invIndex, slotIndex));
            }
        }
    }

    @Override
    protected int getInventoryCount() {
        return 3;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX, OUTPUT_INV_INDEX, WAND_INV_INDEX -> 1;
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
        retVal.set(INPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(INPUT_INV_INDEX), this));
        
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
        // Slot 0 was the output item stack
        this.setItem(OUTPUT_INV_INDEX, 0, legacyItems.get(0));
        
        // Slot 1 was the input item stack
        this.setItem(INPUT_INV_INDEX, 0, legacyItems.get(1));
        
        // Slot 2 was the wand item stack
        this.setItem(WAND_INV_INDEX, 0, legacyItems.get(2));
    }
}
