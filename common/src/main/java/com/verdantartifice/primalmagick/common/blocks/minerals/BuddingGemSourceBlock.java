package com.verdantartifice.primalmagick.common.blocks.minerals;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

/**
 * Definition of a block that grows gem clusters like amethyst geodes.  Unlike amethyst geodes, these
 * blocks are imperfect and have a chance to decay every time they create or grow a bud, eventually
 * decaying into inert gem blocks.
 * 
 * @author Daedalus4096
 */
public class BuddingGemSourceBlock extends Block {
    protected static final int GROWTH_CHANCE = 5;
    
    protected final GemBudType gemType;
    protected final Supplier<Block> initialBudSupplier;
    protected final Supplier<Block> decayBlockSupplier;
    protected final float decayChance;
    
    public BuddingGemSourceBlock(GemBudType gemType, Supplier<Block> initialBudSupplier, Supplier<Block> decayBlockSupplier, float decayChance, Block.Properties properties) {
        super(properties);
        this.gemType = gemType;
        this.initialBudSupplier = initialBudSupplier;
        this.decayBlockSupplier = decayBlockSupplier;
        this.decayChance = decayChance;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(GROWTH_CHANCE) == 0) {
            Direction dir = Direction.values()[pRandom.nextInt(Direction.values().length)];
            BlockPos targetPos = pPos.relative(dir);
            BlockState targetState = pLevel.getBlockState(targetPos);
            Block newBlock = null;
            
            // Determine what the new cluster should be
            if (targetState.isAir() || (targetState.is(Blocks.WATER) && targetState.getFluidState().getAmount() == FluidState.AMOUNT_FULL)) {
                newBlock = this.initialBudSupplier.get();
            } else if (targetState.getBlock() instanceof BuddingGemClusterBlock cluster && cluster.getGemBudType() == this.gemType && 
                    targetState.getValue(BuddingGemClusterBlock.FACING) == dir && cluster.getNextGemBlock().isPresent()) {
                newBlock = BlocksPM.get(cluster.getNextGemBlock().get());
            }
            
            if (newBlock instanceof BuddingGemClusterBlock newCluster) {
                // Update the world with the new cluster
                BlockState newClusterState = newCluster.defaultBlockState()
                        .setValue(BuddingGemClusterBlock.FACING, dir)
                        .setValue(BuddingGemClusterBlock.WATERLOGGED, targetState.getFluidState().getType() == Fluids.WATER);
                pLevel.setBlockAndUpdate(targetPos, newClusterState);
                
                if (pRandom.nextFloat() < this.decayChance) {
                    // Decay the source block
                    pLevel.setBlockAndUpdate(pPos, this.decayBlockSupplier.get().defaultBlockState());
                }
            }
        }
    }
}
