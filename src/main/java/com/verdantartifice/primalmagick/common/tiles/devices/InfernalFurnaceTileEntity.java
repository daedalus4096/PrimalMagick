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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
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
import net.minecraft.world.level.block.state.BlockState;
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
    
    public static void tick(Level level, BlockPos pos, BlockState state, InfernalFurnaceTileEntity entity) {
        // TODO
    }
    
    private boolean canBurn(RegistryAccess registryAccess, @Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int maxFurnaceStackSize) {
        // TODO
        return false;
    }
    
    private boolean burn(RegistryAccess registryAccess, @Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int maxFurnaceStackSize) {
        // TODO
        return false;
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
