package com.verdantartifice.primalmagic.common.crafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.misc.BlockSwapper;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.hooks.BasicEventHooks;

/**
 * Wand transformation that turns a block into something else.
 * 
 * @author Daedalus4096
 */
public class WandTransformBlock implements IWandTransform {
    protected final Block target;
    protected final ItemStack result;
    protected final CompoundResearchKey research;
    
    public WandTransformBlock(@Nonnull Block target, @Nonnull ItemStack result, @Nullable CompoundResearchKey research) {
        this.target = target;
        this.result = result;
        this.research = research;
    }

    @Override
    public boolean isValid(World world, PlayerEntity player, BlockPos pos) {
        // The expected block type must be at the given world position and the given player must know the right research
        return (world.getBlockState(pos).getBlock() == this.target) && (this.research == null || this.research.isKnownBy(player));
    }

    @Override
    public void execute(World world, PlayerEntity player, BlockPos pos) {
        // Enqueue a block swapper to be executed on the world next tick
        BasicEventHooks.firePlayerCraftingEvent(player, this.result, new FakeInventory(1));
        BlockState state = world.getBlockState(pos);
        BlockSwapper.enqueue(world, new BlockSwapper(pos, state, this.result, player));
    }
}
