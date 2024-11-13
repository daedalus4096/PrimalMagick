package com.verdantartifice.primalmagick.common.tiles.mana;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
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
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Definition of an auto-charger tile entity.  Provides the siphoning functionality for the
 * corresponding block.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.mana.AutoChargerBlock
 */
public class AutoChargerTileEntity extends AbstractTileSidedInventoryPM {
    protected static final int INPUT_INV_INDEX = 0;
    
    protected final Set<BlockPos> fontLocations = new HashSet<>();
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
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("ChargeTime", this.chargeTime);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutoChargerTileEntity entity) {
        ItemStack chargeStack = entity.getItem(INPUT_INV_INDEX, 0);
        if (!level.isClientSide) {
            if (entity.chargeTime % 20 == 0) {
                // Scan surroundings for mana fonts once a second
                entity.scanSurroundings();
            }
            
            // Channel each nearby font
            Vec3 chargerCenter = Vec3.atCenterOf(pos);
            for (BlockPos fontPos : entity.fontLocations) {
                if (level.getBlockEntity(fontPos) instanceof AbstractManaFontTileEntity tile) {
                    if (chargeStack.getItem() instanceof IWand) {
                        // NOTE: Normally this method is called with a count that's descending, but the method doesn't actually care
                        tile.onWandUseTick(chargeStack, level, null, chargerCenter, entity.chargeTime);
                    } else if (chargeStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
                        if (entity.chargeTime % 5 == 0) {
                            chargeStack.update(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY, manaCap -> {
                                tile.doSiphon(manaCap, level, null, chargerCenter, 100);    // TODO Get the stack's max charge rate somehow
                                return manaCap;
                            });
                        }
                    }
                }
            }
            
            entity.chargeTime++;
        }
    }
    
    @SuppressWarnings("deprecation")
    protected void scanSurroundings() {
        BlockPos pos = this.getBlockPos();
        if (Services.LEVEL.isAreaLoaded(this.level, pos, 5)) {
            this.fontLocations.clear();
            Iterable<BlockPos> positions = BlockPos.betweenClosed(pos.offset(-5, -5, -5), pos.offset(5, 5, 5));
            for (BlockPos searchPos : positions) {
                if (this.level.getBlockState(searchPos).getBlock() instanceof AbstractManaFontBlock) {
                    this.fontLocations.add(searchPos.immutable());
                }
            }
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
    protected Optional<Integer> getInventoryIndexForFace(Direction face) {
        return Optional.of(INPUT_INV_INDEX);
    }

    @Override
    protected NonNullList<IItemHandlerPM> createHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> stack.getItem() instanceof IWand || stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()))
                .build());

        return retVal;
    }
}
