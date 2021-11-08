package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.containers.DissolutionChamberContainer;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Definition of a dissolution chamber tile entity.  Performs the processing for the corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.devices.DissolutionChamberBlock}
 */
public class DissolutionChamberTileEntity extends TileInventoryPM implements MenuProvider, IManaContainer, StackedContentsCompatible {
    protected static final int OUTPUT_SLOT_INDEX = 0;
    protected static final int INPUT_SLOT_INDEX = 1;
    protected static final int WAND_SLOT_INDEX = 2;
    protected static final int[] SLOTS_FOR_UP = new int[] { INPUT_SLOT_INDEX };
    protected static final int[] SLOTS_FOR_DOWN = new int[] { OUTPUT_SLOT_INDEX };
    protected static final int[] SLOTS_FOR_SIDES = new int[] { WAND_SLOT_INDEX };
    
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
        super(TileEntityTypesPM.DISSOLUTION_CHAMBER.get(), pos, state, 3);
        this.manaStorage = new ManaStorage(10000, 100, 100, Source.EARTH);
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.processTime = compound.getInt("ProcessTime");
        this.processTimeTotal = compound.getInt("ProcessTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("ProcessTime", this.processTime);
        compound.putInt("ProcessTimeTotal", this.processTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
        return super.save(compound);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new DissolutionChamberContainer(windowId, playerInv, this, this.chamberData);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }

    protected int getProcessTimeTotal() {
        return 200;
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, DissolutionChamberTileEntity entity) {
        boolean shouldMarkDirty = false;
        
        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.items.get(WAND_SLOT_INDEX);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Source.EARTH) - entity.manaStorage.getManaStored(Source.EARTH);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.EARTH, centimanaToTransfer)) {
                    entity.manaStorage.receiveMana(Source.EARTH, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }

            SimpleContainer testInv = new SimpleContainer(entity.items.get(INPUT_SLOT_INDEX));
            IDissolutionRecipe recipe = level.getServer().getRecipeManager().getRecipeFor(RecipeTypesPM.DISSOLUTION, testInv, level).orElse(null);
            if (entity.canDissolve(testInv, recipe)) {
                entity.processTime++;
                if (entity.processTime >= entity.processTimeTotal) {
                    entity.processTime = 0;
                    entity.processTimeTotal = entity.getProcessTimeTotal();
                    entity.doDissolve(testInv, recipe);
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
    
    protected boolean canDissolve(Container inputInv, IDissolutionRecipe recipe) {
        if (!inputInv.isEmpty() && recipe != null) {
            ItemStack output = recipe.getResultItem();
            if (output.isEmpty()) {
                return false;
            } else if (this.getMana(Source.EARTH) < (100 * recipe.getManaCosts().getAmount(Source.EARTH))) {
                return false;
            } else {
                ItemStack currentOutput = this.items.get(OUTPUT_SLOT_INDEX);
                if (currentOutput.isEmpty()) {
                    return true;
                } else if (!currentOutput.sameItem(output)) {
                    return false;
                } else if (currentOutput.getCount() + output.getCount() <= this.getMaxStackSize() && currentOutput.getCount() + output.getCount() <= currentOutput.getMaxStackSize()) {
                    return true;
                } else {
                    return currentOutput.getCount() + output.getCount() <= output.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }
    
    protected void doDissolve(Container inputInv, IDissolutionRecipe recipe) {
        if (recipe != null && this.canDissolve(inputInv, recipe)) {
            ItemStack recipeOutput = recipe.assemble(inputInv);
            ItemStack currentOutput = this.items.get(OUTPUT_SLOT_INDEX);
            if (currentOutput.isEmpty()) {
                this.items.set(OUTPUT_SLOT_INDEX, recipeOutput);
            } else if (recipeOutput.getItem() == currentOutput.getItem() && ItemStack.tagMatches(recipeOutput, currentOutput)) {
                currentOutput.grow(recipeOutput.getCount());
            }
            
            for (int index = 0; index < inputInv.getContainerSize(); index++) {
                ItemStack stack = inputInv.getItem(index);
                if (!stack.isEmpty()) {
                    stack.shrink(1);
                }
            }
            this.setMana(Source.EARTH, this.getMana(Source.EARTH) - (100 * recipe.getManaCosts().getAmount(Source.EARTH)));
        }
    }
    
    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        if (index == 0 && (stack.isEmpty() || !stack.sameItem(slotStack) || !ItemStack.tagMatches(stack, slotStack))) {
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
        SourceList mana = new SourceList();
        for (Source source : Source.SORTED_SOURCES) {
            int amount = this.manaStorage.getManaStored(source);
            if (amount > 0) {
                mana.add(source, amount);
            }
        }
        return mana;
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
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
        if (slotIndex == WAND_SLOT_INDEX) {
            return stack.getItem() instanceof IWand;
        } else if (slotIndex == INPUT_SLOT_INDEX) {
            return true;
        } else {
            return false;
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

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        for (ItemStack stack : this.items) {
            stackedContents.accountStack(stack);
        }
    }
}
