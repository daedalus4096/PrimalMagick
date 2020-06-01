package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Random;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Block definition for a bloodletter.  Bloodletters serve as props in magical rituals; cutting
 * yourself on them at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class BloodletterBlock extends Block implements IRitualPropBlock {
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/bloodletter"));
    
    public BloodletterBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));
        this.setDefaultState(this.getDefaultState().with(FILLED, Boolean.FALSE));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }
    
    @Override
    public float getStabilityBonus(World world, BlockPos pos) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getSymmetryPenalty(World world, BlockPos pos) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isPropActivated(BlockState state, World world, BlockPos pos) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getPropTranslationKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float getUsageStabilityBonus() {
        // TODO Auto-generated method stub
        return 0;
    }
}
