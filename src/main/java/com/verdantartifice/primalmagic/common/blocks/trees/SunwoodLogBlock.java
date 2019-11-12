package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.BlockRenderLayer;

public class SunwoodLogBlock extends LogBlock {
    public static final EnumProperty<TimePhase> PHASE = EnumProperty.create("phase", TimePhase.class);
    
    public SunwoodLogBlock() {
        super(MaterialColor.GOLD, Block.Properties.create(Material.WOOD, MaterialColor.GOLD).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        this.setRegistryName(PrimalMagic.MODID, "sunwood_log");
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
    public boolean isSolid(BlockState state) {
        return state.get(PHASE) == TimePhase.FULL;
    }
}
