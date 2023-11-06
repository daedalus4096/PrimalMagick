package com.verdantartifice.primalmagick.common.tiles.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.blocks.crafting.CalcinatorBlock;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a proper calcinator tile entity.  Provides the melting functionality for the corresponding
 * device.  Works quickly and generates essence based on item affinity, with a chance for dregs on low-affinity
 * items.
 * 
 * @author Daedalus4096
 */
public class CalcinatorTileEntity extends AbstractCalcinatorTileEntity {
    public CalcinatorTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.CALCINATOR.get(), pos, state);
    }
    
    @Override
    protected int getCookTimeTotal() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof CalcinatorBlock calcinatorBlock) {
            return switch (calcinatorBlock.getDeviceTier()) {
                case BASIC -> 160;
                case ENCHANTED -> 120;
                case FORBIDDEN -> 80;
                case HEAVENLY -> 40;
                default -> 200;
            };
        } else {
            throw new IllegalStateException("Unknown block type " + block);
        }
    }

    @Override
    @Nonnull
    protected List<ItemStack> getCalcinationOutput(ItemStack inputStack, boolean alwaysGenerateDregs) {
        List<ItemStack> output = new ArrayList<>();
        EssenceType maxEssenceType = this.getMaxOutputEssenceType();
        AffinityManager.getInstance().getAffinityValues(inputStack, this.level).filter(Predicate.not(SourceList::isEmpty)).ifPresent(sources -> {
            for (Source source : Source.SORTED_SOURCES) {
                int amount = sources.getAmount(source);
                if (amount > 0) {
                    EssenceType currentEssenceType = maxEssenceType;
                    while (currentEssenceType != null) {
                        if (amount >= currentEssenceType.getAffinity()) {
                            // Generate output for each affinity multiple in the input stack
                            int count = (amount / currentEssenceType.getAffinity());
                            ItemStack stack = this.getOutputEssence(currentEssenceType, source, count);
                            if (!stack.isEmpty()) {
                                output.add(stack);
                                break;
                            }
                        } else {
                            currentEssenceType = currentEssenceType.getDowngrade();
                        }
                    }
                    if (currentEssenceType == null && amount > 0 && (alwaysGenerateDregs || this.level.random.nextInt(EssenceType.DUST.getAffinity()) < amount)) {
                        // If the item's affinity is too low for guaranteed essence, give a random chance of generating a dust anyway
                        ItemStack stack = this.getOutputEssence(EssenceType.DUST, source, 1);
                        if (!stack.isEmpty()) {
                            output.add(stack);
                        }
                    }
                }
            }
        });
        return output;
    }
    
    @Nonnull
    protected EssenceType getMaxOutputEssenceType() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof CalcinatorBlock calcinatorBlock) {
            return switch (calcinatorBlock.getDeviceTier()) {
                case BASIC -> EssenceType.DUST;
                case ENCHANTED -> EssenceType.SHARD;
                case FORBIDDEN -> EssenceType.CRYSTAL;
                case HEAVENLY -> EssenceType.CLUSTER;
                default -> EssenceType.DUST;
            };
        } else {
            throw new IllegalStateException("Unknown block type " + block);
        }
    }
}
