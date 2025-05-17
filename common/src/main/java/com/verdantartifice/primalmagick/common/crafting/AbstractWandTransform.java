package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.misc.BlockSwapper;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Base wand transformation that triggers a block swapper.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractWandTransform implements IWandTransform {
    protected final ItemStack result;
    protected final AbstractRequirement<?> requirement;
    protected final Map<Block, MutableComponent> similarBlocks = new HashMap<>();
    protected final Map<TagKey<Block>, MutableComponent> similarTags = new HashMap<>();

    public AbstractWandTransform(@Nonnull ItemStack result, @Nullable AbstractRequirement<?> requirement) {
        this.result = result;
        this.requirement = requirement;
    }

    @Override
    public boolean isValid(Level world, Player player, BlockPos pos) {
        return this.requirement == null || this.requirement.isMetBy(player);
    }

    public void addSimilar(Block block, MutableComponent component) {
        this.similarBlocks.put(block, component);
    }

    public void addSimilar(TagKey<Block> tag, MutableComponent component) {
        this.similarTags.put(tag, component);
    }

    @Override
    public boolean isSimilar(Level world, Player player, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return this.similarBlocks.keySet().stream().anyMatch(state::is) || this.similarTags.keySet().stream().anyMatch(state::is);
    }

    @Override
    public void showSimilarityWarning(Level world, Player player, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        MutableComponent toShow = this.similarBlocks.get(state.getBlock());
        if (toShow == null) {
            toShow = this.similarTags.entrySet().stream()
                    .filter(entry -> state.is(entry.getKey()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(null);
        }
        if (toShow != null) {
            player.sendSystemMessage(toShow.withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public void execute(Level world, Player player, BlockPos pos) {
        // Enqueue a block swapper to be executed on the world next tick
        Services.EVENTS.firePlayerCraftingEvent(player, this.result, new FakeInventory(1));
        BlockState state = world.getBlockState(pos);
        BlockSwapper.enqueue(world, new BlockSwapper(pos, state, this.result, player));
    }
}
