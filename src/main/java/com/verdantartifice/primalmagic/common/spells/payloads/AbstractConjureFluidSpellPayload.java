package com.verdantartifice.primalmagic.common.spells.payloads;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractConjureFluidSpellPayload extends AbstractSpellPayload {
    protected final FlowingFluid fluid;
    
    public AbstractConjureFluidSpellPayload(FlowingFluid fluid) {
        super();
        this.fluid = fluid;
    }
    
    @Override
    public void execute(RayTraceResult target, Vec3d burstPoint, SpellPackage spell, World world, PlayerEntity caster) {
        if (target != null && target.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockTarget = (BlockRayTraceResult)target;
            BlockPos targetPos = blockTarget.getPos();
            ItemStack stack = this.getSimulatedItemStack(this.fluid);
            if (world.isBlockModifiable(caster, targetPos) && caster.canPlayerEdit(targetPos, blockTarget.getFace(), stack)) {
                BlockState state = world.getBlockState(targetPos);
                BlockPos placePos = (state.getBlock() instanceof ILiquidContainer && this.fluid == Fluids.WATER) ? targetPos : targetPos.offset(blockTarget.getFace());
                this.placeFluid(caster, world, placePos, blockTarget);
            }
        }
    }

    protected ItemStack getSimulatedItemStack(@Nonnull Fluid fluid) {
        if (fluid.isIn(FluidTags.WATER)) {
            return new ItemStack(Items.WATER_BUCKET);
        } else if (fluid.isIn(FluidTags.LAVA)) {
            return new ItemStack(Items.LAVA_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    protected void placeFluid(PlayerEntity player, World world, BlockPos pos, BlockRayTraceResult blockTarget) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        Material material = state.getMaterial();
        boolean isSolid = material.isSolid();
        boolean isReplaceable = material.isReplaceable();
        if (world.isAirBlock(pos) || !isSolid || isReplaceable || (block instanceof ILiquidContainer && ((ILiquidContainer)block).canContainFluid(world, pos, state, this.fluid))) {
            if (world.dimension.doesWaterVaporize() && this.fluid.isIn(FluidTags.WATER)) {
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                for (int index = 0; index < 8; index++) {
                    world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + world.rand.nextDouble(), pos.getY() + world.rand.nextDouble(), pos.getZ() + world.rand.nextDouble(), 0.0F, 0.0F, 0.0F);
                }
            } else if (block instanceof ILiquidContainer && this.fluid == Fluids.WATER) {
                ((ILiquidContainer)block).receiveFluid(world, pos, state, this.fluid.getStillFluidState(false));
            } else {
                if (!world.isRemote && (!isSolid || isReplaceable) && !material.isLiquid()) {
                    world.destroyBlock(pos, true);
                }
                world.setBlockState(pos, this.fluid.getDefaultState().getBlockState(), 0xB);
            }
        } else if (blockTarget != null) {
            this.placeFluid(player, world, blockTarget.getPos().offset(blockTarget.getFace()), null);
        }
    }
}
