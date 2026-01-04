package com.verdantartifice.primalmagick.common.tiles.mana;

import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import CriteriaTriggersPM;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.mana.network.IManaConsumer;
import com.verdantartifice.primalmagick.common.mana.network.RouteManager;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.tiles.base.IOwnedTileEntity;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
public abstract class AutoChargerTileEntity extends AbstractTileSidedInventoryPM implements IManaConsumer, IOwnedTileEntity {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected static final int INPUT_INV_INDEX = 0;

    protected int chargeTime;
    protected EntityReference<Player> owner;

    public AutoChargerTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.AUTO_CHARGER.get(), pos, state);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices(int inventoryIndex) {
        // Sync the charger's wand stack for client rendering use
        return inventoryIndex == INPUT_INV_INDEX ? ImmutableSet.of(0) : ImmutableSet.of();
    }
    
    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.chargeTime = input.getIntOr("ChargeTime", 0);
        this.owner = EntityReference.read(input, "Owner");
    }
    
    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("ChargeTime", this.chargeTime);
        EntityReference.store(this.owner, output, "Owner");
    }

    @Override
    public void setTileOwner(@Nullable Player owner) {
        this.owner = EntityReference.of(owner);
    }

    @Override
    @Nullable
    public Player getTileOwner() {
        Level level = this.getLevel();
        return level != null ? EntityReference.getPlayer(this.owner, level) : null;
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        // Before the block entity is removed, invalidate its route table and drop its inventory
        super.preRemoveSideEffects(pos, state);
        this.getRouteTable().invalidate();
        this.dropContents(this.getLevel(), pos);
    }

    @Override
    public int receiveMana(@NotNull Source source, int maxReceive, boolean simulate) {
        Level level = this.getLevel();
        ItemStack chargeStack = this.getItem(INPUT_INV_INDEX, 0);
        if (level != null && !level.isClientSide() && chargeStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
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
        if (!level.isClientSide()) {
            ItemStack chargeStack = entity.getItem(INPUT_INV_INDEX, 0);
            if (entity.chargeTime % 5 == 0 && chargeStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
                final ManaStorage manaStorage = chargeStack.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY);
                final int throughput = entity.getManaThroughput();
                final int totalSiphoned = Sources.streamSorted()
                        .filter(manaStorage::canReceive)
                        .mapToInt(s -> entity.doSiphon(entity.getTileOwner(), level, s, Math.min(throughput, manaStorage.getMaxManaStored(s) - manaStorage.getManaStored(s))))
                        .sum();
                if (entity.getTileOwner() instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggersPM.MANA_NETWORK_SIPHON.get().trigger(serverPlayer, totalSiphoned);
                }
            }
            entity.chargeTime++;
        }
    }
    
    public ItemStack getItem() {
        return this.getItem(INPUT_INV_INDEX, 0);
    }
    
    public ItemStack getSyncedStack() {
        return this.syncedInventories.get(INPUT_INV_INDEX).getFirst();
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
    protected NonNullList<IItemHandlerPM> createItemHandlers() {
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
    @NotNull
    public RouteTable getRouteTable() {
        return RouteManager.getRouteTable(this.getLevel());
    }
}
