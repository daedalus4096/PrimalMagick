package com.verdantartifice.primalmagick.common.crafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * Wand transformation that turns a block of a given tag into something else.
 * 
 * @author Daedalus4096
 */
public class WandTransformBlockTag extends AbstractWandTransform {
    protected final TagKey<Block> target;
    
    public WandTransformBlockTag(@Nonnull TagKey<Block> target, @Nonnull ItemStack result, @Nullable CompoundResearchKey research) {
        super(result, research);
        this.target = target;
    }

    @Override
    public boolean isValid(Level world, Player player, BlockPos pos) {
        // The block at the given world position must be in the expected tag and the given player must know the right research
        return super.isValid(world, player, pos) && world.getBlockState(pos).is(this.target);
    }
}
