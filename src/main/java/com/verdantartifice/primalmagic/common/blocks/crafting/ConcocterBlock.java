package com.verdantartifice.primalmagic.common.blocks.crafting;

import java.util.Random;

import com.verdantartifice.primalmagic.common.misc.HarvestLevel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

/**
 * Block definition for a concocter.  Used to craft multi-dose potions.
 * 
 * @author Daedalus4096
 */
public class ConcocterBlock extends Block {
    public static final BooleanProperty HAS_BOTTLE = BlockStateProperties.HAS_BOTTLE_0;
    protected static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D), Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 14.0D, 9.0D));

    public ConcocterBlock() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(0.5F).harvestTool(ToolType.PICKAXE).harvestLevel(HarvestLevel.WOOD.getLevel()).setLightLevel(state -> {
            return 1;
        }).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(HAS_BOTTLE, false));
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(HAS_BOTTLE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.getX() + 0.4D + (double)rand.nextFloat() * 0.2D;
        double d1 = (double)pos.getY() + 0.7D + (double)rand.nextFloat() * 0.3D;
        double d2 = (double)pos.getZ() + 0.4D + (double)rand.nextFloat() * 0.2D;
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}
