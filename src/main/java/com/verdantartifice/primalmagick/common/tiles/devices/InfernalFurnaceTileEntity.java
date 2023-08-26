package com.verdantartifice.primalmagick.common.tiles.devices;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;
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
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;

public class InfernalFurnaceTileEntity extends TileInventoryPM implements MenuProvider, IManaContainer, StackedContentsCompatible {
    protected static final int SUPERCHARGE_MULTIPLIER = 4;
    protected static final int OUTPUT_SLOT_INDEX = 0;
    protected static final int INPUT_SLOT_INDEX = 1;
    protected static final int IGNYX_SLOT_INDEX = 2;
    protected static final int WAND_SLOT_INDEX = 3;
    protected static final int[] SLOTS_FOR_UP = new int[] { INPUT_SLOT_INDEX };
    protected static final int[] SLOTS_FOR_DOWN = new int[] { OUTPUT_SLOT_INDEX };
    protected static final int[] SLOTS_FOR_SIDES = new int[] { IGNYX_SLOT_INDEX };
    
    protected int superchargeTime;
    protected int superchargeTimeTotal;
    protected int processTime;
    protected int processTimeTotal;
    protected ManaStorage manaStorage;
    protected LazyOptional<IManaStorage> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData chamberData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return InfernalFurnaceTileEntity.this.processTime;
            case 1:
                return InfernalFurnaceTileEntity.this.processTimeTotal;
            case 2:
                return InfernalFurnaceTileEntity.this.manaStorage.getManaStored(Source.INFERNAL);
            case 3:
                return InfernalFurnaceTileEntity.this.manaStorage.getMaxManaStored(Source.INFERNAL);
            case 4:
                return InfernalFurnaceTileEntity.this.superchargeTime;
            case 5:
                return InfernalFurnaceTileEntity.this.superchargeTimeTotal;
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            // Don't set mana storage values
            switch (index) {
            case 0:
                InfernalFurnaceTileEntity.this.processTime = value;
                break;
            case 1:
                InfernalFurnaceTileEntity.this.processTimeTotal = value;
                break;
            case 4:
                InfernalFurnaceTileEntity.this.superchargeTime = value;
                break;
            case 5:
                InfernalFurnaceTileEntity.this.superchargeTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };
    
    public InfernalFurnaceTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.INFERNAL_FURNACE.get(), pos, state, 4);
        this.manaStorage = new ManaStorage(10000, 100, 100, Source.INFERNAL);
        this.quickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.processTime = compound.getInt("ProcessTime");
        this.processTimeTotal = compound.getInt("ProcessTimeTotal");
        this.superchargeTime = compound.getInt("SuperchargeTime");
        this.superchargeTimeTotal = compound.getInt("SuperchargeTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ProcessTime", this.processTime);
        compound.putInt("ProcessTimeTotal", this.processTimeTotal);
        compound.putInt("SuperchargeTime", this.superchargeTime);
        compound.putInt("SuperchargeTimeTotal", this.superchargeTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
    
    private boolean isLit() {
        return this.processTime > 0;
    }
    
    private boolean isCharged() {
        int current = this.getMana(Source.INFERNAL);
        return current > 0 && current >= getManaNeeded(this.getLevel(), this);
    }
    
    private boolean isSupercharged() {
        return this.superchargeTime > 0;
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, InfernalFurnaceTileEntity entity) {
        boolean shouldMarkDirty = false;
        boolean startedLit = entity.isLit();
        
        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.items.get(WAND_SLOT_INDEX);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Source.INFERNAL) - entity.manaStorage.getManaStored(Source.INFERNAL);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.INFERNAL, centimanaToTransfer)) {
                    entity.manaStorage.receiveMana(Source.INFERNAL, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }
        }
         
        if (entity.isSupercharged()) {
            --entity.superchargeTime;
        }
        
        ItemStack fuelStack = entity.items.get(IGNYX_SLOT_INDEX);
        boolean inputPopulated = !entity.items.get(INPUT_SLOT_INDEX).isEmpty();
        boolean fuelPopulated = !fuelStack.isEmpty();
        if (entity.isCharged() && inputPopulated) {
            Recipe<?> recipe = inputPopulated ? entity.quickCheck.getRecipeFor(entity, level).orElse(null) : null;
            int furnaceMaxStackSize = entity.getMaxStackSize();
            
            // Handle supercharge burn
            if (!entity.isSupercharged() && fuelPopulated && entity.canBurn(level.registryAccess(), recipe, entity.items, furnaceMaxStackSize)) {
                entity.superchargeTimeTotal = entity.getSuperchargeDuration(fuelStack);
                entity.superchargeTime = entity.superchargeTimeTotal;
                if (entity.isSupercharged()) {
                    shouldMarkDirty = true;
                    if (fuelStack.hasCraftingRemainingItem()) {
                        entity.items.set(IGNYX_SLOT_INDEX, fuelStack.getCraftingRemainingItem());
                    } else {
                        fuelStack.shrink(1);
                        if (fuelStack.isEmpty()) {
                            entity.items.set(IGNYX_SLOT_INDEX, ItemStack.EMPTY);
                        }
                    }
                }
            }
            
            // Process the item being smelted
            if (entity.isCharged() && entity.canBurn(level.registryAccess(), recipe, entity.items, furnaceMaxStackSize)) {
                entity.processTime += (entity.isSupercharged() ? SUPERCHARGE_MULTIPLIER : 1);
                if (entity.processTime >= entity.processTimeTotal) {
                    entity.processTime = 0;
                    entity.processTimeTotal = getTotalCookTime(level, entity);
                    if (entity.burn(level.registryAccess(), recipe, entity.items, furnaceMaxStackSize)) {
                        // TODO Set recipe used
                    }
                    shouldMarkDirty = true;
                }
            } else {
                entity.processTime = 0;
            }
        } else if (!entity.isCharged() && entity.processTime > 0) {
            // Decay progress if not enough mana is available
            entity.processTime = Mth.clamp(entity.processTime - 2, 0, entity.processTimeTotal);
        }
        
        // Update the block's LIT blockstate property if needed
        if (startedLit != entity.isLit()) {
            shouldMarkDirty = true;
            state = state.setValue(BlockStateProperties.LIT, entity.isLit());
            level.setBlock(pos, state, Block.UPDATE_ALL);
        }
        
        // Notify the world a block change has occurred if needed
        if (shouldMarkDirty) {
            setChanged(level, pos, state);
        }
   }
    
    private boolean canBurn(RegistryAccess registryAccess, @Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int maxFurnaceStackSize) {
        if (!items.get(INPUT_SLOT_INDEX).isEmpty() && recipe != null) {
            @SuppressWarnings("unchecked")
            ItemStack recipeOutput = ((Recipe<WorldlyContainer>)recipe).assemble(this, registryAccess);
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                ItemStack existingOutput = items.get(OUTPUT_SLOT_INDEX);
                if (existingOutput.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(recipeOutput, existingOutput)) {
                    return false;
                } else if (existingOutput.getCount() + recipeOutput.getCount() <= maxFurnaceStackSize && existingOutput.getCount() + recipeOutput.getCount() <= existingOutput.getMaxStackSize()) {
                    return true;
                } else {
                    return existingOutput.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }
    
    private boolean burn(RegistryAccess registryAccess, @Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int maxFurnaceStackSize) {
        if (recipe != null && this.canBurn(registryAccess, recipe, items, maxFurnaceStackSize)) {
            ItemStack inputStack = items.get(INPUT_SLOT_INDEX);
            @SuppressWarnings("unchecked")
            ItemStack recipeOutput = ((Recipe<WorldlyContainer>)recipe).assemble(this, registryAccess);
            ItemStack existingOutput = items.get(OUTPUT_SLOT_INDEX);
            if (existingOutput.isEmpty()) {
                items.set(OUTPUT_SLOT_INDEX, recipeOutput.copy());
            } else if (ItemStack.isSameItem(recipeOutput, existingOutput)) {
                existingOutput.grow(recipeOutput.getCount());
            }
            
            if (this.manaStorage.canExtract(Source.INFERNAL)) {
                this.manaStorage.extractMana(Source.INFERNAL, getManaNeeded(this.getLevel(), this), false);
            }
            inputStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }
    
    protected int getSuperchargeDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            // Shorten based on speed boost
            return ForgeHooks.getBurnTime(pFuel, RecipeType.SMELTING) / SUPERCHARGE_MULTIPLIER;
        }
    }

    private static int getTotalCookTime(Level pLevel, InfernalFurnaceTileEntity pBlockEntity) {
        return pBlockEntity.quickCheck.getRecipeFor(pBlockEntity, pLevel).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }
    
    private static int getManaNeeded(Level pLevel, InfernalFurnaceTileEntity pBlockEntity) {
        // Return one centimana per one hundred ticks of cooking time of the current recipe, or zero if no recipe is active
        return pBlockEntity.items.get(INPUT_SLOT_INDEX).isEmpty() ? 0 : pBlockEntity.quickCheck.getRecipeFor(pBlockEntity, pLevel).map(AbstractCookingRecipe::getCookingTime).orElse(0) / 100;
    }
    
    public static boolean isSuperchargeFuel(ItemStack pStack) {
        return pStack.is(ItemTagsPM.INFERNAL_SUPERCHARGE_FUEL);
    }

    @Override
    public void fillStackedContents(StackedContents pHelper) {
        for (ItemStack stack : this.items) {
            pHelper.accountStack(stack);
        }
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
        return this.manaStorage.getMaxManaStored(Source.INFERNAL);
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
