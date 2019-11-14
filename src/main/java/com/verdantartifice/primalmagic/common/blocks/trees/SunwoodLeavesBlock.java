package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Random;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class SunwoodLeavesBlock extends LeavesBlock {
    public static final EnumProperty<TimePhase> PHASE = EnumProperty.create("phase", TimePhase.class);
    
    public SunwoodLeavesBlock() {
        super(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT));
        this.setRegistryName(PrimalMagic.MODID, "sunwood_leaves");
        this.setDefaultState(this.getDefaultState().with(PHASE, TimePhase.FULL));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(PHASE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        TimePhase phase = TimePhase.getSunPhase(context.getWorld());
        return super.getStateForPlacement(context).with(PHASE, phase);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public void randomTick(BlockState state, World worldIn, BlockPos pos, Random random) {
        super.randomTick(state, worldIn, pos, random);
        TimePhase newPhase = TimePhase.getSunPhase(worldIn);
        if (newPhase != state.get(PHASE)) {
            worldIn.setBlockState(pos, state.with(PHASE, newPhase), 0x3);
        }
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        BlockState state = super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        TimePhase newPhase = TimePhase.getSunPhase(worldIn);
        if (newPhase != state.get(PHASE)) {
            state = state.with(PHASE, newPhase);
        }
        return state;
    }
    
    @Override
    public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
        if (blockState.get(PHASE) == TimePhase.FULL) {
            return this.blockHardness;
        } else {
            return blockState.get(PHASE).getHardness();
        }
    }
    
    @Override
    public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, Entity exploder, Explosion explosion) {
        if (state.get(PHASE) == TimePhase.FULL) {
            return this.blockResistance;
        } else {
            return state.get(PHASE).getResistance();
        }
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(PHASE).getLightLevel();
    }
}
