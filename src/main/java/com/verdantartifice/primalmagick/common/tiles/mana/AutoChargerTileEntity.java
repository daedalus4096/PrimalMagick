package com.verdantartifice.primalmagick.common.tiles.mana;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of an auto-charger tile entity.  Provides the siphoning functionality for the
 * corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.mana.AutoChargerBlock}
 */
public class AutoChargerTileEntity extends TileInventoryPM {
    protected static final int[] SLOTS = new int[] { 0 };
    
    protected final Set<BlockPos> fontLocations = new HashSet<>();
    protected int chargeTime;

    public AutoChargerTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.AUTO_CHARGER.get(), pos, state, 1);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the charger's wand stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(0));
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
        ItemStack chargeStack = entity.getItem(0);
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

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, slotStack);
        if (index == 0 && !flag) {
            this.chargeTime = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
        return stack.getItem() instanceof IWand || stack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).isPresent();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
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
