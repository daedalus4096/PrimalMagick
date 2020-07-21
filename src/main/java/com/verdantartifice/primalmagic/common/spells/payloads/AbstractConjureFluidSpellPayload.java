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
import net.minecraftforge.common.util.Constants;

/**
 * Base class for a fluid conjuration spell.  Creates a source block of fluid at the designated point.
 * Works similarly to placing fluid from a bucket.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractConjureFluidSpellPayload extends AbstractSpellPayload {
    protected final FlowingFluid fluid;
    
    public AbstractConjureFluidSpellPayload(FlowingFluid fluid) {
        super();
        this.fluid = fluid;
    }
    
    @Override
    public void execute(RayTraceResult target, Vec3d burstPoint, SpellPackage spell, World world, PlayerEntity caster, ItemStack spellSource) {
        if (target != null) {
            ItemStack stack = this.getSimulatedItemStack(this.fluid);
            if (target.getType() == RayTraceResult.Type.BLOCK) {
                // If the target is a block, place the fluid at the block position if it's a fluid container,
                // or at the adjacent block position if it's not.
                BlockRayTraceResult blockTarget = (BlockRayTraceResult)target;
                BlockPos targetPos = blockTarget.getPos();
                if (world.isBlockModifiable(caster, targetPos) && caster.canPlayerEdit(targetPos, blockTarget.getFace(), stack)) {
                    // Only place if the caster is allowed to modify the target location
                    BlockState state = world.getBlockState(targetPos);
                    BlockPos placePos = (state.getBlock() instanceof ILiquidContainer && this.fluid == Fluids.WATER) ? targetPos : targetPos.offset(blockTarget.getFace());
                    this.placeFluid(caster, world, placePos, blockTarget);
                }
            } else if (target.getType() == RayTraceResult.Type.ENTITY) {
                // If the target is an entity, place the fluid at the entity's position
                BlockRayTraceResult blockTarget = RayTraceUtils.getBlockResultFromEntityResult((EntityRayTraceResult)target);
                if (world.isBlockModifiable(caster, blockTarget.getPos()) && caster.canPlayerEdit(blockTarget.getPos(), blockTarget.getFace(), stack)) {
                    // Only place if the caster is allowed to modify the target location
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
                // If the block is a liquid container and we're dealing with water, place the water into the container
                ((ILiquidContainer)block).receiveFluid(world, pos, state, this.fluid.getStillFluidState(false));
            } else {
                // Destroy the existing block at the target location if fluid would replace it
                if (!world.isRemote && (!isSolid || isReplaceable) && !material.isLiquid()) {
                    world.destroyBlock(pos, true);
                }
                
                // Set the fluid block into the world
                world.setBlockState(pos, this.fluid.getDefaultState().getBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
        } else if (blockTarget != null) {
            // If we can't place the fluid at the given position, place it instead at the adjacent position defined by the raytrace result
            this.placeFluid(player, world, blockTarget.getPos().offset(blockTarget.getFace()), null);
        }
    }
}
