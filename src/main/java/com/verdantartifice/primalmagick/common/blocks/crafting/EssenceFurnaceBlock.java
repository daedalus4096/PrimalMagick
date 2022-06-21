package com.verdantartifice.primalmagick.common.blocks.crafting;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.crafting.EssenceFurnaceTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The most rudimentary type of calcinator, it doesn't even warrant the name.  It works slower and only
 * produces a single dust per source, regardless of affinity.
 * 
 * @author Daedalus4096
 */
public class EssenceFurnaceBlock extends AbstractCalcinatorBlock {
    protected static final VoxelShape SHAPE = Block.box(1D, 0D, 1D, 15D, 12D, 15D);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EssenceFurnaceTileEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TileEntityTypesPM.ESSENCE_FURNACE.get(), EssenceFurnaceTileEntity::tick);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if (stateIn.getValue(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            // Add smoke and flame at calcinator front
            Direction direction = stateIn.getValue(FACING);
            Direction.Axis axis = direction.getAxis();
            double d3 = 0.5D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
            double d6 = ((rand.nextDouble() * 3.0D) + 3.0D) / 16.0D;
            double d7 = axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypesPM.INFERNAL_FLAME.get(), d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            
            // Add smoke at calcinator smokestack
            double d8 = rand.nextDouble() * 0.2D - 0.1D;
            double d9 = rand.nextDouble() * 0.2D - 0.1D;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d8, d1 + 1.0D, d2 + d9, 0.0D, 0.0D, 0.0D);
        }
    }
}
