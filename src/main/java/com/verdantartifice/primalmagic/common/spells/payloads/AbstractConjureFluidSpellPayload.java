package com.verdantartifice.primalmagic.common.spells.payloads;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.util.RayTraceUtils;

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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
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
        if (target != null) {
            ItemStack stack = this.getSimulatedItemStack(this.fluid);
            if (target.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult blockTarget = (BlockRayTraceResult)target;
                BlockPos targetPos = blockTarget.getPos();
                if (world.isBlockModifiable(caster, targetPos) && caster.canPlayerEdit(targetPos, blockTarget.getFace(), stack)) {
                    BlockState state = world.getBlockState(targetPos);
                    BlockPos placePos = (state.getBlock() instanceof ILiquidContainer && this.fluid == Fluids.WATER) ? targetPos : targetPos.offset(blockTarget.getFace());
                    this.placeFluid(caster, world, placePos, blockTarget);
                }
            } else if (target.getType() == RayTraceResult.Type.ENTITY) {
                BlockRayTraceResult blockTarget = RayTraceUtils.getBlockResultFromEntityResult((EntityRayTraceResult)target);
                if (world.isBlockModifiable(caster, blockTarget.getPos()) && caster.canPlayerEdit(blockTarget.getPos(), blockTarget.getFace(), stack)) {
                    this.placeFluid(caster, world, blockTarget.getPos(), blockTarget);
                }
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
                // Do nothing for water in the Nether or similar dimensions
                return;
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
