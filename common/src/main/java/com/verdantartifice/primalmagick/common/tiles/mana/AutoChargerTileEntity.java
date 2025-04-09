package com.verdantartifice.primalmagick.common.tiles.mana;

import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.mana.network.IManaConsumer;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.Set;

/**
 * Definition of an auto-charger tile entity.  Provides the siphoning functionality for the
 * corresponding block.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.mana.AutoChargerBlock
 */
public abstract class AutoChargerTileEntity extends AbstractTileSidedInventoryPM implements IManaConsumer {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected static final int INPUT_INV_INDEX = 0;

    protected final RouteTable routeTable = new RouteTable();
    protected int chargeTime;

    public AutoChargerTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.AUTO_CHARGER.get(), pos, state);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices(int inventoryIndex) {
        // Sync the charger's wand stack for client rendering use
        return inventoryIndex == INPUT_INV_INDEX ? ImmutableSet.of(Integer.valueOf(0)) : ImmutableSet.of();
    }
    
    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.chargeTime = compound.getInt("ChargeTime");

        // Deserialize the tile's mana networking route table
        if (this.getLevel() != null) {
            this.routeTable.deserializeNBT(registries, compound.get("RouteTable"), this.getLevel());
        } else {
            LOGGER.warn("Unable to load route table, no level present");
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("ChargeTime", this.chargeTime);

        // Serialize the tile's mana networking route table
        this.routeTable.serializeNBT(registries).ifPresent(tag -> compound.put("RouteTable", tag));
    }

    @Override
    public int receiveMana(@NotNull Source source, int maxReceive, boolean simulate) {
        ItemStack chargeStack = this.getItem(INPUT_INV_INDEX, 0);
        if (!this.getLevel().isClientSide() && chargeStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
            MutableInt actualReceived = new MutableInt(0);
            chargeStack.update(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY, manaCap -> {
                actualReceived.setValue(manaCap.receiveMana(source, maxReceive, simulate));
                return manaCap;
            });
            return actualReceived.intValue();
        }
        return 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutoChargerTileEntity entity) {
        if (!level.isClientSide) {
            ItemStack chargeStack = entity.getItem(INPUT_INV_INDEX, 0);
            if (entity.chargeTime % 5 == 0 && chargeStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
                final ManaStorage manaStorage = chargeStack.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY);
                final int throughput = entity.getManaThroughput();
                Sources.getAllSorted().stream()
                        .filter(manaStorage::canReceive)
                        .forEach(s -> entity.doSiphon(level, s, Math.min(throughput, manaStorage.getMaxManaStored(s) - manaStorage.getManaStored(s))));
            }
            entity.chargeTime++;
        }
    }
    
    public ItemStack getItem() {
        return this.getItem(INPUT_INV_INDEX, 0);
    }
    
    public ItemStack getSyncedStack() {
        return this.syncedInventories.get(INPUT_INV_INDEX).get(0);
    }
    
    public void setItem(ItemStack stack) {
        this.setItem(INPUT_INV_INDEX, 0, stack);
    }

    @Override
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        ItemStack slotStack = this.getItem(invIndex, slotIndex);
        super.setItem(invIndex, slotIndex, stack);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, slotStack);
        if (invIndex == INPUT_INV_INDEX && !flag) {
            this.chargeTime = 0;
            this.setChanged();
        }
    }

    @Override
    protected int getInventoryCount() {
        return 1;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return inventoryIndex == INPUT_INV_INDEX ? 1 : 0;
    }

    @Override
    public Optional<Integer> getInventoryIndexForFace(@NotNull Direction face) {
        return Optional.of(INPUT_INV_INDEX);
    }

    @Override
    protected NonNullList<IItemHandlerPM> createHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()))
                .build());

        return retVal;
    }

    @Override
    public int getNetworkRange() {
        return 5;
    }

    @Override
    public boolean canConsume(@NotNull Source source) {
        return true;
    }

    @Override
    public int getManaThroughput() {
        // Get acceptable throughput from contained charge stack
        // TODO Get the stack's max charge rate somehow, instead of siphoning one point of mana at a time for non-wands
        ItemStack chargeStack = this.getItem(INPUT_INV_INDEX, 0);
        return chargeStack.getItem() instanceof IWand wand ? wand.getSiphonAmount(chargeStack) : 100;
    }

    @Override
    public @NotNull RouteTable getRouteTable() {
        return this.routeTable;
    }
}
