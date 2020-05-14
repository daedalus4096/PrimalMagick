package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Random;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Block definition for a ritual candle.  Ritual candles serve as props in magical rituals; lighting
 * them at the right time can allow a ritual to progress to the next stage.  They can also be dyed
 * different colors.
 * 
 * @author Daedalus4096
 */
public class RitualCandleBlock extends Block implements ISaltPowered {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/ritual_candle"));

    protected final DyeColor color;
    
    public RitualCandleBlock(DyeColor colorIn, Block.Properties properties) {
        super(properties);
        this.color = colorIn;
        this.setDefaultState(this.getDefaultState().with(LIT, Boolean.FALSE));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(LIT);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(BlockState state) {
        // Only give off light if the candle is lit
        return state.get(LIT) ? super.getLightValue(state) : 0;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        // Show flame particles if lit
        if (stateIn.get(LIT)) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.7D;
            double z = pos.getZ() + 0.5D;
            worldIn.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
        
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos) || this.isBlockSaltPowered(worldIn, pos.up())) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }
    
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}
