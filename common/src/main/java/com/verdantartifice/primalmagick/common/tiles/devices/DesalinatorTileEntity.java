package com.verdantartifice.primalmagick.common.tiles.devices;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.capabilities.IFluidHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.menus.DesalinatorMenu;
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
import net.minecraft.core.component.DataComponentMap;
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
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;

/**
 * Definition of a desalinator tile entity.  Performs the boiling for the corresponding block.
 *
 * @see com.verdantartifice.primalmagick.common.blocks.devices.DesalinatorBlock
 * @author Daedalus4096
 */
public abstract class DesalinatorTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider, IManaContainer {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int INPUT_INV_INDEX = 0;
    public static final int OUTPUT_INV_INDEX = 1;
    public static final int WAND_INV_INDEX = 2;
    protected static final int REQUIRED_WATER_AMOUNT = 1000;

    protected int boilTime;
    protected int boilTimeTotal;
    protected ManaStorage manaStorage;
    protected IFluidHandlerPM waterTank;

    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> DesalinatorTileEntity.this.boilTime;
                case 1 -> DesalinatorTileEntity.this.boilTimeTotal;
                case 2 -> DesalinatorTileEntity.this.manaStorage.getManaStored(Sources.SUN);
                case 3 -> DesalinatorTileEntity.this.manaStorage.getMaxManaStored(Sources.SUN);
                case 4 -> DesalinatorTileEntity.this.waterTank.getFluidInTank(0).getAmount();
                case 5 -> DesalinatorTileEntity.this.waterTank.getTankCapacity(0);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            // Don't set mana storage values
            switch (index) {
                case 0:
                    DesalinatorTileEntity.this.boilTime = value;
                    break;
                case 1:
                    DesalinatorTileEntity.this.boilTimeTotal = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    public DesalinatorTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.DESALINATOR.get(), pos, state);
        this.manaStorage = new ManaStorage(2000, 200, 200, Sources.SUN);
        this.waterTank = Services.FLUID_HANDLERS.create(4000, fs -> fs.is(Fluids.WATER));
    }

    public IManaStorage<?> getUncachedManaStorage() {
        return this.manaStorage;
    }

    public IFluidHandlerPM getUncachedFluidHandler() {
        return this.waterTank;
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.boilTime = compound.getInt("BoilTime");
        this.boilTimeTotal = compound.getInt("BoilTimeTotal");
        ManaStorage.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), compound.get("ManaStorage")).resultOrPartial(msg -> {
            LOGGER.error("Failed to decode mana storage: {}", msg);
        }).ifPresent(mana -> mana.copyManaInto(this.manaStorage));
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("BoilTime", this.boilTime);
        compound.putInt("BoilTimeTotal", this.boilTimeTotal);
        ManaStorage.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), this.manaStorage).resultOrPartial(msg -> {
            LOGGER.error("Failed to encode mana storage: {}", msg);
        }).ifPresent(encoded -> compound.put("ManaStorage", encoded));
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new DesalinatorMenu(windowId, playerInv, this.getBlockPos(), this, this.containerData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    protected int getBoilTimeTotal() {
        return 200;
    }

    protected int getManaCost() {
        return 200;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DesalinatorTileEntity entity) {
        boolean shouldMarkDirty = false;

        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.getItem(WAND_INV_INDEX, 0);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Sources.SUN) - entity.manaStorage.getManaStored(Sources.SUN);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Sources.SUN, centimanaToTransfer, level.registryAccess())) {
                    entity.manaStorage.receiveMana(Sources.SUN, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }

            // Fill the internal water tank from any inserted containers
            ItemStack inputStack = entity.getItem(INPUT_INV_INDEX, 0);
            if (!inputStack.isEmpty()) {
                IFluidStackPM fluidStack = getFluidStackForInput(inputStack);
                ItemStack containerStack = getContainerForInput(inputStack);
                if (!fluidStack.isEmpty() && entity.canFill(containerStack)) {
                    entity.doFill(fluidStack, containerStack);
                }
            }

            // Process ingredients
            if (!entity.waterTank.drain(REQUIRED_WATER_AMOUNT, true).isEmpty() && entity.manaStorage.getManaStored(Sources.SUN) >= entity.getManaCost()) {
                // If boilable input is in place and the outputs are clear, process it
                if (entity.canBoil()) {
                    entity.boilTime++;
                    if (entity.boilTime >= entity.boilTimeTotal) {
                        entity.boilTime = 0;
                        entity.boilTimeTotal = entity.getBoilTimeTotal();
                        entity.doBoil();
                        shouldMarkDirty = true;
                    }
                } else {
                    entity.boilTime = 0;
                }
            } else if (entity.boilTime > 0) {
                // Decay any boil progress
                entity.boilTime = Mth.clamp(entity.boilTime - 2, 0, entity.boilTimeTotal);
            }
        }

        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }

    protected static IFluidStackPM getFluidStackForInput(ItemStack stack) {
        if (stack.is(Items.WATER_BUCKET)) {
            return Services.FLUIDS.makeFluidStack(Fluids.WATER, 1000);
        } else if (stack.is(Items.POTION) || stack.is(ItemsPM.CONCOCTION.get())) {
            return Services.FLUIDS.makeFluidStack(Fluids.WATER, 250);
        } else {
            return Services.FLUIDS.emptyStack();
        }
    }

    protected static ItemStack getContainerForInput(ItemStack stack) {
        if (stack.is(Items.WATER_BUCKET) && stack.getItem().getCraftingRemainingItem() != null) {
            return new ItemStack(stack.getItem().getCraftingRemainingItem());
        } else if (stack.is(Items.POTION)) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else if (stack.is(ItemsPM.CONCOCTION.get())) {
            return new ItemStack(ItemsPM.SKYGLASS_FLASK.get());
        } else {
            return ItemStack.EMPTY;
        }
    }

    protected boolean canFill(ItemStack containerStack) {
        ItemStack containerOutput = this.getItem(OUTPUT_INV_INDEX, 2);
        return containerOutput.isEmpty() ||
                (containerOutput.is(containerStack.getItem()) &&
                        containerOutput.getCount() < this.itemHandlers.get(OUTPUT_INV_INDEX).getSlotLimit(2) &&
                        containerOutput.getCount() < containerOutput.getMaxStackSize());
    }

    protected void doFill(IFluidStackPM fluidStack, ItemStack containerStack) {
        // Fill the water tank by the given amount
        this.waterTank.fill(fluidStack, false);

        // Add the given empty fluid container to the output slot
        ItemStack outputStack = this.getItem(OUTPUT_INV_INDEX, 2);
        if (outputStack.isEmpty()) {
            this.setItem(OUTPUT_INV_INDEX, 2, containerStack);
        } else {
            outputStack.grow(1);
        }

        // Shrink the input container slot
        ItemStack inputStack = this.getItem(INPUT_INV_INDEX, 0);
        inputStack.shrink(1);
        if (inputStack.isEmpty()) {
            this.setItem(INPUT_INV_INDEX, 0, ItemStack.EMPTY);
        }
    }

    protected boolean canBoil() {
        ItemStack saltOutput = this.getItem(OUTPUT_INV_INDEX, 0);
        ItemStack essenceOutput = this.getItem(OUTPUT_INV_INDEX, 1);
        return (saltOutput.getCount() < this.itemHandlers.get(OUTPUT_INV_INDEX).getSlotLimit(0) &&
                saltOutput.getCount() < saltOutput.getMaxStackSize() &&
                essenceOutput.getCount() < this.itemHandlers.get(OUTPUT_INV_INDEX).getSlotLimit(1) &&
                essenceOutput.getCount() < essenceOutput.getMaxStackSize());
    }

    protected void doBoil() {
        // TODO Add salt output

        // Add sea dust output
        ItemStack essenceOutput = this.getItem(OUTPUT_INV_INDEX, 1);
        if (essenceOutput.isEmpty()) {
            this.setItem(OUTPUT_INV_INDEX, 1, new ItemStack(ItemsPM.ESSENCE_DUST_SEA.get()));
        } else {
            essenceOutput.grow(1);
        }

        // Drain water
        this.waterTank.drain(REQUIRED_WATER_AMOUNT, false);

        // Consume mana
        this.manaStorage.extractMana(Sources.SUN, this.getManaCost(), false);
    }

    @Override
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        ItemStack slotStack = this.getItem(invIndex, slotIndex);
        super.setItem(invIndex, slotIndex, stack);
        if (invIndex == INPUT_INV_INDEX && (stack.isEmpty() || !ItemStack.isSameItemSameComponents(stack, slotStack))) {
            this.boilTimeTotal = this.getBoilTimeTotal();
            this.boilTime = 0;
            this.setChanged();
        }
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
        return this.manaStorage.getMaxManaStored(Sources.SUN);
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
            case INPUT_INV_INDEX, WAND_INV_INDEX -> 1;
            case OUTPUT_INV_INDEX -> 3;
            default -> 0;
        };
    }

    @Override
    public Optional<Integer> getInventoryIndexForFace(@NotNull Direction face) {
        return switch (face) {
            case DOWN -> Optional.of(OUTPUT_INV_INDEX);
            default -> Optional.of(INPUT_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<IItemHandlerPM> createItemHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));

        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> stack.is(Items.WATER_BUCKET))
                .build());

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
        pComponentInput.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).copyManaInto(this.manaStorage);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        super.collectImplicitComponents(pComponents);
        pComponents.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), this.manaStorage);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag pTag) {
        pTag.remove("ManaStorage");
    }
}
