package com.verdantartifice.primalmagick.common.tiles.crafting;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an essence furnace tile entity.  Provides the melting functionality for the corresponding
 * device.  Works slowly and generates little essence, with no dreg chance.
 * 
 * @author Daedalus4096
 */
public class EssenceFurnaceTileEntity extends AbstractCalcinatorTileEntity {
    public EssenceFurnaceTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.ESSENCE_FURNACE.get(), pos, state);
    }
    
    @Override
    protected int getCookTimeTotal() {
        return 200;
    }

    @Override
    @Nonnull
    protected List<ItemStack> getCalcinationOutput(ItemStack inputStack, boolean alwaysGenerateDregs) {
        List<ItemStack> output = new ArrayList<>();
        SourceList sources = AffinityManager.getInstance().getAffinityValues(inputStack, this.level);
        if (sources != null && !sources.isEmpty()) {
            for (Source source : Source.SORTED_SOURCES) {
                int amount = sources.getAmount(source);
                if (amount >= EssenceType.DUST.getAffinity()) {
                    // Generate output for each affinity multiple in the input stack
                    ItemStack stack = this.getOutputEssence(EssenceType.DUST, source, 1);
                    if (!stack.isEmpty()) {
                        output.add(stack);
                    }
                }
            }
        }
        return output;
    }
}
