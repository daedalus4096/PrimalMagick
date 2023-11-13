package com.verdantartifice.primalmagick.common.tiles.mana;

import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Definition of an auto-charger tile entity.  Provides the siphoning functionality for the
 * corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.mana.AutoChargerBlock}
 */
public class AutoChargerTileEntity extends AbstractTileSidedInventoryPM {
    protected static final int INPUT_INV_INDEX = 0;
    
    protected final Set<BlockPos> fontLocations = new HashSet<>();
    protected int chargeTime;

    public AutoChargerTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.AUTO_CHARGER.get(), pos, state);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices(int inventoryIndex) {
        // Sync the charger's wand stack for client rendering use
        return inventoryIndex == INPUT_INV_INDEX ? ImmutableSet.of(Integer.valueOf(0)) : ImmutableSet.of();
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.chargeTime = compound.getInt("ChargeTime");
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
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
                    } else {
                        chargeStack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).ifPresent(manaCap -> {
                            if (entity.chargeTime % 5 == 0) {
                                tile.doSiphon(manaCap, level, null, chargerCenter, 100);    // TODO Get the stack's max charge rate somehow
                            }
                        });
                    }
                }
            }
            
            entity.chargeTime++;
        }
    }
    
    @SuppressWarnings("deprecation")
    protected void scanSurroundings() {
        BlockPos pos = this.getBlockPos();
        if (this.level.isAreaLoaded(pos, 5)) {
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
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, slotStack);
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
    protected OptionalInt getInventoryIndexForFace(Direction face) {
        return OptionalInt.of(INPUT_INV_INDEX);
    }

    @Override
    protected NonNullList<ItemStackHandler> createHandlers() {
        NonNullList<ItemStackHandler> retVal = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandlerPM(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(INPUT_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem() instanceof IWand || stack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).isPresent();
            }
        });

        return retVal;
    }

    @Override
    protected void loadLegacyItems(NonNullList<ItemStack> legacyItems) {
        // Slot 0 was the input item stack
        this.setItem(INPUT_INV_INDEX, 0, legacyItems.get(0));
    }
}
