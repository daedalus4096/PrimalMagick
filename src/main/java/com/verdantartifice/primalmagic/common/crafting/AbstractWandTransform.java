package com.verdantartifice.primalmagic.common.crafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.misc.BlockSwapper;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.hooks.BasicEventHooks;

/**
 * Base wand transformation that triggers a block swapper.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractWandTransform implements IWandTransform {
    protected final ItemStack result;
    protected final CompoundResearchKey research;

    public AbstractWandTransform(@Nonnull ItemStack result, @Nullable CompoundResearchKey research) {
        this.result = result;
        this.research = research;
    }

    @Override
    public boolean isValid(Level world, Player player, BlockPos pos) {
        return this.research == null || this.research.isKnownBy(player);
    }

    @Override
    public void execute(Level world, Player player, BlockPos pos) {
        // Enqueue a block swapper to be executed on the world next tick
        BasicEventHooks.firePlayerCraftingEvent(player, this.result, new FakeInventory(1));
        BlockState state = world.getBlockState(pos);
        BlockSwapper.enqueue(world, new BlockSwapper(pos, state, this.result, player));
    }
}
