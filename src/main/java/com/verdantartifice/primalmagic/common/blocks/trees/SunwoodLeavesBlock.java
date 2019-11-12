package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.BlockRenderLayer;

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
}
