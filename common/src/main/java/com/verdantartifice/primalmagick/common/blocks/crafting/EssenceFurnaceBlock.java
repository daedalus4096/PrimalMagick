package com.verdantartifice.primalmagick.common.blocks.crafting;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * The most rudimentary type of calcinator, it doesn't even warrant the name.  It works slower and only
 * produces a single dust per source, regardless of affinity.
 * 
 * @author Daedalus4096
 */
public class EssenceFurnaceBlock extends AbstractCalcinatorBlock {
    public static final MapCodec<EssenceFurnaceBlock> CODEC = simpleCodec(EssenceFurnaceBlock::new);
    
    protected static final VoxelShape SHAPE = Block.box(1D, 0D, 1D, 15D, 12D, 15D);
    
    public EssenceFurnaceBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return Services.BLOCK_ENTITY_PROTOTYPES.essenceFurnace().create(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityTypesPM.ESSENCE_FURNACE.get(), Services.BLOCK_ENTITY_TICKERS.essenceFurnace());
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (stateIn.getValue(LIT)) {
            double d0 = pos.getX() + 0.5D;
            double d1 = pos.getY();
            double d2 = pos.getZ() + 0.5D;
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

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
