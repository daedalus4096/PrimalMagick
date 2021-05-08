package com.verdantartifice.primalmagic.common.crafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Wand transformation that turns a block of a given tag into something else.
 * 
 * @author Daedalus4096
 */
public class WandTransformBlockTag extends AbstractWandTransform {
    protected final ITag<Block> target;
    
    public WandTransformBlockTag(@Nonnull ITag<Block> target, @Nonnull ItemStack result, @Nullable CompoundResearchKey research) {
        super(result, research);
        this.target = target;
    }

    @Override
    public boolean isValid(World world, PlayerEntity player, BlockPos pos) {
        // The block at the given world position must be in the expected tag and the given player must know the right research
        return super.isValid(world, player, pos) && world.getBlockState(pos).getBlock().isIn(this.target);
    }
}
