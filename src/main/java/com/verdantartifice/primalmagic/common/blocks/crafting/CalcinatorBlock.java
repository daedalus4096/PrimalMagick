package com.verdantartifice.primalmagic.common.blocks.crafting;

import java.util.Random;

import com.verdantartifice.primalmagic.common.misc.DeviceTier;
import com.verdantartifice.primalmagic.common.misc.ITieredDevice;
import com.verdantartifice.primalmagic.common.tiles.crafting.CalcinatorTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A proper calcinator, one of four tiers.  Generates essence by melting other items.
 * 
 * @author Daedalus4096
 */
public class CalcinatorBlock extends AbstractCalcinatorBlock implements ITieredDevice {
    protected final DeviceTier tier;
    
    public CalcinatorBlock(DeviceTier tier) {
        super();
        this.tier = tier;
    }

    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CalcinatorTileEntity();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            // Add smoke and flame at calcinator front
            Direction direction = stateIn.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double d3 = 0.5D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = axis == Direction.Axis.X ? (double)direction.getXOffset() * d3 : d4;
            double d6 = ((rand.nextDouble() * 3.0D) + 3.0D) / 16.0D;
            double d7 = axis == Direction.Axis.Z ? (double)direction.getZOffset() * d3 : d4;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }
}
