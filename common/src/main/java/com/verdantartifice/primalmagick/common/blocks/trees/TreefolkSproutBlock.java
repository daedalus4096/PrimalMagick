package com.verdantartifice.primalmagick.common.blocks.trees;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * Block definition for a treefolk sprout, which grows into a treefolk entity.
 * 
 * @author Daedalus4096
 */
public class TreefolkSproutBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<BushBlock> CODEC = simpleCodec(TreefolkSproutBlock::new);
    
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
    
    public TreefolkSproutBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STAGE);
    }

    @Override
    public void randomTick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (pLevel.getMaxLocalRawBrightness(pPos.above()) >= 9 && pRandom.nextInt(7) == 0 && Services.LEVEL.isAreaLoaded(pLevel, pPos, 1)) {
            this.advanceTree(pLevel, pPos, pState, pRandom);
        }
    }
    
    public void advanceTree(ServerLevel pLevel, BlockPos pPos, BlockState pState, RandomSource pRandom) {
        if (pState.getValue(STAGE) == 0) {
            pLevel.setBlock(pPos, pState.cycle(STAGE), UPDATE_INVISIBLE);
        } else {
            pLevel.removeBlock(pPos, false);
            TreefolkEntity entity = EntityTypesPM.TREEFOLK.get().create(pLevel, EntitySpawnReason.BREEDING);
            if (entity != null) {
                entity.setBaby(true);
                entity.snapTo(Vec3.atBottomCenterOf(pPos));
                pLevel.addFreshEntityWithPassengers(entity);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        return pRandom.nextDouble() < 0.45D;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.advanceTree(pLevel, pPos, pState, pRandom);
    }

    @Override
    @NotNull
    public MapCodec<BushBlock> codec() {
        return CODEC;
    }
}
