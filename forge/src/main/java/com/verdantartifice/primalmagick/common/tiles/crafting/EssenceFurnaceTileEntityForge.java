package com.verdantartifice.primalmagick.common.tiles.crafting;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Definition of an essence furnace tile entity.  Provides the melting functionality for the corresponding
 * device.  Works slowly and generates little essence, with no dreg chance.
 * 
 * @author Daedalus4096
 */
public class EssenceFurnaceTileEntityForge extends AbstractCalcinatorTileEntityForge {
    public EssenceFurnaceTileEntityForge(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.ESSENCE_FURNACE.get(), pos, state);
    }
    
    @Override
    protected int getCookTimeTotal() {
        return 200;
    }

    @Override
    @Nonnull
    protected List<ItemStack> getCalcinationOutput(ItemStack inputStack, boolean alwaysGenerateDregs) {
        List<ItemStack> output = new ArrayList<>();
        AffinityManager.getInstance().getAffinityValues(inputStack, this.level).filter(Predicate.not(SourceList::isEmpty)).ifPresent(sources -> {
            Sources.getAllSorted().stream()
                    .filter(s -> sources.getAmount(s) >= EssenceType.DUST.getAffinity())
                    .map(s -> this.getOutputEssence(EssenceType.DUST, s, 1))
                    .filter(Predicate.not(ItemStack::isEmpty))
                    .forEach(output::add);
        });
        return output;
    }
}
