package com.verdantartifice.primalmagic.common.blocks.misc;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.Constants;

/**
 * Block definition for the glow field.  An invisible, intangible block that provides light which
 * may fade over time.
 * 
 * @author Daedalus4096
 */
public class GlowFieldBlock extends Block {
    public static final IntegerProperty LIGHT = IntegerProperty.create("light", 1, 15);
    public static final BooleanProperty FADING = BooleanProperty.create("fading");
    
    public GlowFieldBlock() {
        super(Block.Properties.of(Material.AIR).strength(-1, 3600000).lightLevel((state) -> { return state.getValue(LIGHT); }).noDrops().noOcclusion().randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(LIGHT, Integer.valueOf(15)).setValue(FADING, Boolean.FALSE));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(LIGHT, FADING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        // Don't show a selection highlight when mousing over the field
        return Shapes.empty();
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
    
    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        // Don't work with the creative pick-block feature, as this block has no corresponding item block
        return ItemStack.EMPTY;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rng) {
        if (state.getValue(FADING)) {
            if (state.getValue(LIGHT) <= 1) {
                level.removeBlock(pos, false);
            } else {
                level.setBlock(pos, state.setValue(LIGHT, state.getValue(LIGHT) - 1), Constants.BlockFlags.BLOCK_UPDATE);
            }
        }
    }
}
