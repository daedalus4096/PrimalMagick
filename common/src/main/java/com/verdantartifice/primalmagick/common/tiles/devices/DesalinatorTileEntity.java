package com.verdantartifice.primalmagick.common.tiles.devices;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.capabilities.IFluidHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
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
        // TODO Stub
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
        // TODO Stub
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
