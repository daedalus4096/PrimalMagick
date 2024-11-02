package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.misc.BlockSwapper;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base wand transformation that triggers a block swapper.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractWandTransform implements IWandTransform {
    protected final ItemStack result;
    protected final AbstractRequirement<?> requirement;

    public AbstractWandTransform(@Nonnull ItemStack result, @Nullable AbstractRequirement<?> requirement) {
        this.result = result;
        this.requirement = requirement;
    }

    @Override
    public boolean isValid(Level world, Player player, BlockPos pos) {
        return this.requirement == null || this.requirement.isMetBy(player);
    }

    @Override
    public void execute(Level world, Player player, BlockPos pos) {
        // Enqueue a block swapper to be executed on the world next tick
        Services.EVENTS.firePlayerCraftingEvent(player, this.result, new FakeInventory(1));
        BlockState state = world.getBlockState(pos);
        BlockSwapper.enqueue(world, new BlockSwapper(pos, state, this.result, player));
    }
}
